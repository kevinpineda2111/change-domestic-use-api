package com.claro.amx.cufjava.change_domestic_use_api.repository;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.TramiteValidationResult;

public interface ValidateTramiteRepository {

    /**
     * Calls F_VALIDA_TRAMITE to check if a line has pending tramites.
     *
     * @param lineNumber the line number (cellular number) to validate
     * @return TramiteValidationResult with pending status and error info
     * @throws BusinessException if an error occurs calling the stored procedure
     */
    TramiteValidationResult validateTramite(String lineNumber) throws BusinessException;
}
