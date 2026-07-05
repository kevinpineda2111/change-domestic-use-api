package com.claro.amx.cufjava.change_domestic_use_api.service.impl;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.SaveDomesticUseChangeRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.SaveDomesticUseChangeResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.repository.FixedLineInAddressRepository;
import com.claro.amx.cufjava.change_domestic_use_api.repository.LevantaPresuspRepository;
import com.claro.amx.cufjava.change_domestic_use_api.repository.PresuspeRepository;
import com.claro.amx.cufjava.change_domestic_use_api.repository.ValidateTramiteRepository;
import com.claro.amx.cufjava.change_domestic_use_api.service.SaveDomesticUseChangeService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveDomesticUseChangeServiceImpl implements SaveDomesticUseChangeService {

    private final FixedLineInAddressRepository fixedLineInAddressRepository;
    private final ValidateTramiteRepository validateTramiteRepository;
    private final PresuspeRepository presuspeRepository;
    private final LevantaPresuspRepository levantaPresuspRepository;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public SaveDomesticUseChangeResponseDTO saveDomesticUseChange(SaveDomesticUseChangeRequestDTO request)
            throws BusinessException {

        var previousDomesticUse = request.getPreviousDomesticUse();
        var newDomesticUse = request.getNewDomesticUse();
        var accountId = request.getAccountId();
        var lineNumber = request.getLineNumber();
        var ticklerId = request.getTicklerId();
        var reasonId = request.getReasonId();
        var userId = request.getUserId();

        log.info("[SaveDomesticUseChangeService] Starting domestic use change: " +
                        "lineNumber={}, accountId={}, previousUse={}, newUse={}",
                lineNumber, accountId, previousDomesticUse, newDomesticUse);

        // Step 1: Validate DDI <-> DDN is NOT allowed directly
        validateDdiDdnChange(previousDomesticUse, newDomesticUse);

        // Step 2: Get all FIJA lines in the same address
        log.debug("[SaveDomesticUseChangeService] Getting FIJA lines in address for accountId={}, lineNumber={}",
                accountId, lineNumber);
        var entities = fixedLineInAddressRepository.getFixedLinesInAddress(accountId, lineNumber);

        if (entities == null || entities.isEmpty()) {
            throw new BusinessException(
                    Constants.CODE_NOT_FOUND,
                    "No se encontraron líneas FIJA activas en el domicilio de la línea " + lineNumber,
                    Constants.MODULE_SAVE_DOMESTIC_USE
            );
        }

        log.info("[SaveDomesticUseChangeService] Found {} lines to process", entities.size());

        var processedLines = new ArrayList<String>();

        // Step 3: For each line, validate no pending tramites
        for (var entity : entities) {
            var currentLine = entity.getLineNumber();
            log.debug("[SaveDomesticUseChangeService] Validating tramites for line={}", currentLine);

            var tramiteResult = validateTramiteRepository.validateTramite(currentLine);

            if (!tramiteResult.isSuccess()) {
                throw new BusinessException(
                        Constants.CODE_SERVER_ERROR,
                        "Error en F_VALIDA_TRAMITE para línea " + currentLine +
                                ": " + tramiteResult.getErrorMessage(),
                        Constants.MODULE_SAVE_DOMESTIC_USE
                );
            }

            if (tramiteResult.hasPendingTramite()) {
                throw new BusinessException(
                        Constants.CODE_BAD_REQUEST,
                        "La línea " + currentLine + " tiene trámites pendientes. " +
                                "No se puede realizar el cambio de uso doméstico.",
                        Constants.MODULE_SAVE_DOMESTIC_USE
                );
            }
        }

        // Step 4a: If newDomesticUse = "5" (SOLO ENTRANTE) -> call PRESUSPE for each line
        if (Constants.DOMESTIC_USE_SOLO_ENTRANTE.equals(newDomesticUse)) {
            log.info("[SaveDomesticUseChangeService] New use is SOLO ENTRANTE (5) - calling PRESUSPE for all lines");

            for (var entity : entities) {
                var currentLine = entity.getLineNumber();
                log.debug("[SaveDomesticUseChangeService] Calling PRESUSPE for line={}", currentLine);

                var presuspeResult = presuspeRepository.callPresuspe(
                        currentLine, ticklerId, reasonId, userId);

                if (!presuspeResult.isSuccess()) {
                    throw new BusinessException(
                            Constants.CODE_SERVER_ERROR,
                            "PRESUSPE retornó error para la línea " + currentLine +
                                    ": código=" + presuspeResult.getResult() +
                                    (presuspeResult.getErrorMessage() != null
                                            ? ", mensaje=" + presuspeResult.getErrorMessage() : ""),
                            Constants.MODULE_SAVE_DOMESTIC_USE
                    );
                }

                processedLines.add(currentLine);
                log.debug("[SaveDomesticUseChangeService] PRESUSPE OK for line={}", currentLine);
            }
        }
        // Step 4b: If previousDomesticUse = "5" and new is "1" (DDI) or "2" (DDN) -> LEVANTA_PRESUSP
        else if (Constants.DOMESTIC_USE_SOLO_ENTRANTE.equals(previousDomesticUse)
                && (Constants.DOMESTIC_USE_DDI.equals(newDomesticUse)
                || Constants.DOMESTIC_USE_DDN.equals(newDomesticUse))) {

            log.info("[SaveDomesticUseChangeService] " +
                    "Previous use was SOLO ENTRANTE (5) and new is DDI/DDN - calling LEVANTA_PRESUSP for all lines");

            for (var entity : entities) {
                var currentLine = entity.getLineNumber();
                log.debug("[SaveDomesticUseChangeService] Calling LEVANTA_PRESUSP for line={}", currentLine);

                var levantaResult = levantaPresuspRepository.callLevantaPresusp(
                        currentLine,
                        Constants.DEFAULT_ACT_ID_CMBUD,
                        reasonId,
                        ticklerId,
                        Constants.LEVANTA_PRESUSP_DESCRIPCION
                );

                if (!levantaResult.isSuccess()) {
                    throw new BusinessException(
                            Constants.CODE_SERVER_ERROR,
                            "LEVANTA_PRESUSP retornó error para la línea " + currentLine +
                                    ": código=" + levantaResult.getResult() +
                                    (levantaResult.getErrorMessage() != null
                                            ? ", mensaje=" + levantaResult.getErrorMessage() : ""),
                            Constants.MODULE_SAVE_DOMESTIC_USE
                    );
                }

                processedLines.add(currentLine);
                log.debug("[SaveDomesticUseChangeService] LEVANTA_PRESUSP OK for line={}", currentLine);
            }
        } else {
            log.info("[SaveDomesticUseChangeService] No stored procedure needed for previousUse={}, newUse={}",
                    previousDomesticUse, newDomesticUse);
        }

        log.info("[SaveDomesticUseChangeService] Domestic use change completed successfully for {} lines",
                processedLines.size());

        return SaveDomesticUseChangeResponseDTO.builder()
                .success(true)
                .message("Cambio de uso doméstico realizado exitosamente")
                .accountId(accountId)
                .referenceLineNumber(lineNumber)
                .previousDomesticUse(previousDomesticUse)
                .newDomesticUse(newDomesticUse)
                .processedLines(processedLines)
                .totalProcessedLines(processedLines.size())
                .build();
    }

    /**
     * Validates that DDI (1) and DDN (2) cannot be changed directly to each other.
     * Only allowed: DDI/DDN <-> SOLO ENTRANTE (5).
     */
    private void validateDdiDdnChange(String previousDomesticUse, String newDomesticUse) throws BusinessException {
        var isDdiToDdn = Constants.DOMESTIC_USE_DDI.equals(previousDomesticUse)
                && Constants.DOMESTIC_USE_DDN.equals(newDomesticUse);
        var isDdnToDdi = Constants.DOMESTIC_USE_DDN.equals(previousDomesticUse)
                && Constants.DOMESTIC_USE_DDI.equals(newDomesticUse);

        if (isDdiToDdn || isDdnToDdi) {
            throw new BusinessException(
                    Constants.CODE_BAD_REQUEST,
                    "No se permite cambiar directamente entre DDI (1) y DDN (2). " +
                            "Solo se permite cambiar DDI/DDN a SOLO ENTRANTE (5) o viceversa.",
                    Constants.MODULE_SAVE_DOMESTIC_USE
            );
        }
    }
}
