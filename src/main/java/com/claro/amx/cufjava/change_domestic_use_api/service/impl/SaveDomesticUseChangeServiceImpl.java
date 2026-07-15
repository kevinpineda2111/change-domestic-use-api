package com.claro.amx.cufjava.change_domestic_use_api.service.impl;

import com.claro.amx.cufjava.change_domestic_use_api.client.CommonRestClient;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.SaveDomesticUseChangeRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.SaveDomesticUseChangeResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.repository.LevantaPresuspRepository;
import com.claro.amx.cufjava.change_domestic_use_api.repository.PresuspeRepository;
import com.claro.amx.cufjava.change_domestic_use_api.service.SaveDomesticUseChangeService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor

public class SaveDomesticUseChangeServiceImpl implements SaveDomesticUseChangeService {

    private final CommonRestClient commonRestClient;
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



        // Step 1: Validate DDI <-> DDN is NOT allowed directly
        validateDdiDdnChange(previousDomesticUse, newDomesticUse);

        // Step 2: Get all FIJA lines in the same address

        var lineInfoResponse = commonRestClient.getLineInfo(null,lineNumber);//agregar el httpheader en el metodo
        if (lineInfoResponse.getBody().getCommonResponseDTO().getStatus().equals(Constants.RESULT_NOT_OK)) {
            throw new BusinessException(
                    Constants.CODE_SERVER_ERROR,
                    "Error al obtener líneas FIJA en el domicilio de la línea " + lineNumber +
                            ": " + lineInfoResponse.getBody().getCommonResponseDTO().getError().getMessage(),
                    Constants.MODULE_SAVE_DOMESTIC_USE
            );
        }



        var processedLines = new ArrayList<String>();



        // Step 4a: If newDomesticUse = "5" (SOLO ENTRANTE) -> call PRESUSPE for each line
        if (Constants.DOMESTIC_USE_SOLO_ENTRANTE.equals(newDomesticUse)) {


            for (var entity : lineInfoResponse.getBody().getLinesInAddress()) {
                var currentLine = entity.getLineNumber();


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

            }
        }
        // Step 4b: If previousDomesticUse = "5" and new is "1" (DDI) or "2" (DDN) -> LEVANTA_PRESUSP
        else if (Constants.DOMESTIC_USE_SOLO_ENTRANTE.equals(previousDomesticUse)
                && (Constants.DOMESTIC_USE_DDI.equals(newDomesticUse)
                || Constants.DOMESTIC_USE_DDN.equals(newDomesticUse))) {



            for (var entity : lineInfoResponse.getBody().getLinesInAddress()) {
                var currentLine = entity.getLineNumber();


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

            }
        } else {

        }



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
