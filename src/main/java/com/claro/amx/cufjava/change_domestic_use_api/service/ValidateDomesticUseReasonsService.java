package com.claro.amx.cufjava.change_domestic_use_api.service;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseReasonsRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseReasonsResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;

public interface ValidateDomesticUseReasonsService {

    /**
     * Validates that the given reason is allowed for the business type.
     * <p>
     * Rule from WHEN-VALIDATE_ITEMS CCR_RSN_ID:
     * - For IF/TF/IPTV: reasons in PNTLF parameter (#PEDBAJ#PBCOR#) are NOT allowed.
     *
     * @param request the validation request with businessType and reasonId
     * @return ValidateDomesticUseReasonsResponseDTO with allowed flag
     * @throws BusinessException if an error occurs or reason is not allowed
     */
    ValidateDomesticUseReasonsResponseDTO validateReasons(ValidateDomesticUseReasonsRequestDTO request)
            throws BusinessException;
}
