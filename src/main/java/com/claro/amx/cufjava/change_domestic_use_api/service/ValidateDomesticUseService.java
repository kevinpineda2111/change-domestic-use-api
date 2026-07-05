package com.claro.amx.cufjava.change_domestic_use_api.service;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;

public interface ValidateDomesticUseService {

    /**
     * Validates if the domestic use change is allowed for the given country and business type.
     * <p>
     * Rules from WHEN-NEW-FORM-INSTANCE:
     * - IPTV23: countries where change is NOT allowed (e.g. PY, UY)
     * - IPTV24: business types where change IS allowed (e.g. #IPTV#IF#TF#)
     *
     * @param request the validation request with country and business type
     * @return ValidateDomesticUseResponseDTO with allowed flag and reason
     * @throws BusinessException if an error occurs or validation fails
     */
    ValidateDomesticUseResponseDTO validateDomesticUse(ValidateDomesticUseRequestDTO request)
            throws BusinessException;
}
