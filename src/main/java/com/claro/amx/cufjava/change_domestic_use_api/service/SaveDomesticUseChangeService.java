package com.claro.amx.cufjava.change_domestic_use_api.service;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.SaveDomesticUseChangeRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.SaveDomesticUseChangeResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;

public interface SaveDomesticUseChangeService {

    /**
     * Executes the full domestic use change flow for FIJA lines.
     * <p>
     * Flow:
     * 1. Validate DDI↔DDN is not allowed directly
     * 2. Get all FIJA lines in the same address
     * 3. For each line: validate no pending tramites (F_VALIDA_TRAMITE)
     * 4a. If newDomesticUse = "5" (SOLO ENTRANTE): call PRESUSPE for each line
     * 4b. If previousDomesticUse = "5" and newDomesticUse in ["1","2"]: call LEVANTA_PRESUSP for each line
     *
     * @param request the save request with all required data
     * @return SaveDomesticUseChangeResponseDTO with the operation result
     * @throws BusinessException if validation fails or any stored procedure returns an error
     */
    SaveDomesticUseChangeResponseDTO saveDomesticUseChange(SaveDomesticUseChangeRequestDTO request)
            throws BusinessException;
}
