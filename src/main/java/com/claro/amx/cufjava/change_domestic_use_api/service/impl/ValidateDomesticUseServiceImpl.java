package com.claro.amx.cufjava.change_domestic_use_api.service.impl;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.repository.ParameterRepository;
import com.claro.amx.cufjava.change_domestic_use_api.service.ValidateDomesticUseService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ValidateDomesticUseServiceImpl implements ValidateDomesticUseService {

    private final ParameterRepository parameterRepository;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public ValidateDomesticUseResponseDTO validateDomesticUse(ValidateDomesticUseRequestDTO request)
            throws BusinessException {

        var country = request.getCountry() != null ? request.getCountry().toUpperCase() : "";
        var businessType = request.getBusinessType() != null ? request.getBusinessType().toUpperCase() : "";

        // 1. Check IPTV23: countries where domestic use change is NOT allowed
        var iptv23Result = parameterRepository.getCharParameter(Constants.PARAM_IPTV23);
        if (!iptv23Result.isSuccess()) {
            throw new BusinessException(
                    Constants.CODE_SERVER_ERROR,
                    "Error al obtener parámetro IPTV23: " + iptv23Result.getErrorMessage(),
                    Constants.MODULE_VALIDATE_DOMESTIC_USE
            );
        }

        var iptv23Value = iptv23Result.getCharValue();

        if (iptv23Value != null && !iptv23Value.isBlank()) {
            // IPTV23 contains countries separated by commas or included in a delimited string
            var forbiddenCountries = iptv23Value.replace("#", ",");
            for (var forbiddenCountry : forbiddenCountries.split(",")) {
                if (!forbiddenCountry.isBlank() && forbiddenCountry.trim().equalsIgnoreCase(country)) {
                    return ValidateDomesticUseResponseDTO.builder()
                            .allowed(false)
                            .reason("El país " + country +
                                    " no permite cambio de uso doméstico en líneas FIJA (IPTV23)")
                            .build();
                }
            }
        }

        // 2. Check IPTV24: business types where domestic use change IS allowed
        var iptv24Result = parameterRepository.getCharParameter(Constants.PARAM_IPTV24);
        if (!iptv24Result.isSuccess()) {
            throw new BusinessException(
                    Constants.CODE_SERVER_ERROR,
                    "Error al obtener parámetro IPTV24: " + iptv24Result.getErrorMessage(),
                    Constants.MODULE_VALIDATE_DOMESTIC_USE
            );
        }

        var iptv24Value = iptv24Result.getCharValue();

        if (iptv24Value != null && !iptv24Value.isBlank()) {
            // IPTV24 format: #IPTV#IF#TF# - check if business type is included
            var allowedTypes = iptv24Value.toUpperCase();
            if (!allowedTypes.contains("#" + businessType + "#")) {
                return ValidateDomesticUseResponseDTO.builder()
                        .allowed(false)
                        .reason("El tipo de negocio " + businessType +
                                " no permite cambio de uso doméstico (IPTV24)")
                        .build();
            }
        } else {
            // If IPTV24 is empty, fall back to hardcoded check
            if (!Constants.BUSINESS_TYPE_IF.equals(businessType)
                    && !Constants.BUSINESS_TYPE_TF.equals(businessType)
                    && !Constants.BUSINESS_TYPE_IPTV.equals(businessType)) {
                return ValidateDomesticUseResponseDTO.builder()
                        .allowed(false)
                        .reason("El tipo de negocio " + businessType +
                                " no es válido para cambio de uso doméstico FIJA")
                        .build();
            }
        }

        return ValidateDomesticUseResponseDTO.builder()
                .allowed(true)
                .reason("El cambio de uso doméstico es permitido")
                .build();
    }
}
