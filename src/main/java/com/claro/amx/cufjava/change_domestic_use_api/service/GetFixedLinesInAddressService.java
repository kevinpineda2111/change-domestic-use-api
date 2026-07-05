package com.claro.amx.cufjava.change_domestic_use_api.service;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.GetFixedLinesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.GetFixedLinesResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;

public interface GetFixedLinesInAddressService {

    /**
     * Retrieves all FIJA lines (IF, TF, IPTV) in the same address as the given line number,
     * excluding lines in status 'I' (inactive) or 'C' (cancelled).
     *
     * @param request contains accountId and lineNumber
     * @return GetFixedLinesResponseDTO with the list of lines found
     * @throws BusinessException if an error occurs or no lines are found
     */
    GetFixedLinesResponseDTO getFixedLinesInAddress(GetFixedLinesRequestDTO request) throws BusinessException;
}
