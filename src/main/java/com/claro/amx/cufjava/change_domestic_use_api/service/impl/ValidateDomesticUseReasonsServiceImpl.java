package com.claro.amx.cufjava.change_domestic_use_api.service.impl;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseReasonsRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseReasonsResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.repository.ParameterRepository;
import com.claro.amx.cufjava.change_domestic_use_api.service.ValidateDomesticUseReasonsService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import com.claro.amx.cufjava.change_domestic_use_api.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateDomesticUseReasonsServiceImpl implements ValidateDomesticUseReasonsService {

    private final ParameterRepository parameterRepository;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public ValidateDomesticUseReasonsResponseDTO validateReasons(ValidateDomesticUseReasonsRequestDTO request)
            throws BusinessException {

        var businessType = request.getBusinessType() != null ? request.getBusinessType().toUpperCase() : "";
        var reasonId = request.getReasonId() != null ? request.getReasonId().toUpperCase() : "";

        log.info("[ValidateDomesticUseReasonsService] Validating reasonId={} for businessType={}", reasonId, businessType);

        // Only validate for FIJA business types (IF, TF, IPTV)
        if (!Utils.isFijaBusinessType(businessType)) {
            log.debug("[ValidateDomesticUseReasonsService] BusinessType {} is not FIJA type - skipping PNTLF check",
                    businessType);
            return ValidateDomesticUseReasonsResponseDTO.builder()
                    .allowed(true)
                    .reasonId(reasonId)
                    .businessType(businessType)
                    .message("Razón permitida para el tipo de negocio " + businessType)
                    .build();
        }

        // Get PNTLF parameter containing forbidden reasons for IF/TF/IPTV
        var pntlfResult = parameterRepository.getCharParameter(Constants.PARAM_PNTLF);
        if (!pntlfResult.isSuccess()) {
            throw new BusinessException(
                    Constants.CODE_SERVER_ERROR,
                    "Error al obtener parámetro PNTLF: " + pntlfResult.getErrorMessage(),
                    Constants.MODULE_VALIDATE_REASONS
            );
        }

        var pntlfValue = pntlfResult.getCharValue();
        log.debug("[ValidateDomesticUseReasonsService] PNTLF value={}", pntlfValue);

        if (pntlfValue != null && !pntlfValue.isBlank()) {
            // PNTLF format: #PEDBAJ#PBCOR# - check if reason is in the forbidden list
            var forbiddenReasons = pntlfValue.toUpperCase();
            if (forbiddenReasons.contains("#" + reasonId + "#")) {
                log.warn("[ValidateDomesticUseReasonsService] ReasonId {} is forbidden by PNTLF={} for businessType={}",
                        reasonId, pntlfValue, businessType);
                throw new BusinessException(
                        Constants.CODE_BAD_REQUEST,
                        "La razón " + reasonId + " no está permitida para líneas " + businessType +
                                " (PNTLF). Razones no permitidas: " + pntlfValue,
                        Constants.MODULE_VALIDATE_REASONS
                );
            }
        } else {
            // Fallback: check hardcoded forbidden reasons
            if (Constants.REASON_PEDBAJ.equals(reasonId) || Constants.REASON_PBCOR.equals(reasonId)) {
                throw new BusinessException(
                        Constants.CODE_BAD_REQUEST,
                        "La razón " + reasonId + " no está permitida para líneas " + businessType,
                        Constants.MODULE_VALIDATE_REASONS
                );
            }
        }

        log.info("[ValidateDomesticUseReasonsService] ReasonId={} is allowed for businessType={}", reasonId, businessType);
        return ValidateDomesticUseReasonsResponseDTO.builder()
                .allowed(true)
                .reasonId(reasonId)
                .businessType(businessType)
                .message("La razón " + reasonId + " es válida para el tipo de negocio " + businessType)
                .build();
    }
}
