package com.claro.amx.cufjava.change_domestic_use_api.service;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidatePendingTramitesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidatePendingTramitesResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;

public interface ValidatePendingTramitesService {

    /**
     * Validates that a line has no pending tramites by calling F_VALIDA_TRAMITE.
     * If P_PENDIENTE = 'Y', throws a BusinessException.
     *
     * @param request the validation request with the line number to check
     * @return ValidatePendingTramitesResponseDTO with the result
     * @throws BusinessException if there are pending tramites or an error occurs
     */
    ValidatePendingTramitesResponseDTO validatePendingTramites(ValidatePendingTramitesRequestDTO request)
            throws BusinessException;
}
