package com.claro.amx.cufjava.change_domestic_use_api.service.impl;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidatePendingTramitesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidatePendingTramitesResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.repository.ValidateTramiteRepository;
import com.claro.amx.cufjava.change_domestic_use_api.service.ValidatePendingTramitesService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidatePendingTramitesServiceImpl implements ValidatePendingTramitesService {

    private final ValidateTramiteRepository validateTramiteRepository;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public ValidatePendingTramitesResponseDTO validatePendingTramites(ValidatePendingTramitesRequestDTO request)
            throws BusinessException {

        var lineNumber = request.getLineNumber();
        log.info("[ValidatePendingTramitesService] Validating pending tramites for lineNumber={}", lineNumber);

        var validationResult = validateTramiteRepository.validateTramite(lineNumber);

        if (!validationResult.isSuccess()) {
            throw new BusinessException(
                    Constants.CODE_SERVER_ERROR,
                    "Error en F_VALIDA_TRAMITE para línea " + lineNumber +
                            ": " + validationResult.getErrorMessage(),
                    Constants.MODULE_VALIDATE_TRAMITES
            );
        }

        if (validationResult.hasPendingTramite()) {
            log.warn("[ValidatePendingTramitesService] Line {} has pending tramites", lineNumber);
            throw new BusinessException(
                    Constants.CODE_BAD_REQUEST,
                    "La línea " + lineNumber + " tiene trámites pendientes. No se puede realizar el cambio de uso doméstico.",
                    Constants.MODULE_VALIDATE_TRAMITES
            );
        }

        log.info("[ValidatePendingTramitesService] No pending tramites for lineNumber={}", lineNumber);
        return ValidatePendingTramitesResponseDTO.builder()
                .lineNumber(lineNumber)
                .hasPendingTramites(false)
                .message("La línea no tiene trámites pendientes")
                .build();
    }
}
