package com.claro.amx.cufjava.change_domestic_use_api.repository;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.StoredProcResult;

public interface PresuspeRepository {

    /**
     * Calls PRESUSPE to pre-suspend a line (set to "solo entrante").
     *
     * @param lineNumber   the line number (CELULAR)
     * @param ticklerId    the tickler/case number (TCK)
     * @param reason       the reason code (RAZON)
     * @param userConnect  the connected user (P_USER_CONECT)
     * @return StoredProcResult with the return code and optional error message
     * @throws BusinessException if an error occurs calling the stored procedure
     */
    StoredProcResult callPresuspe(String lineNumber, Long ticklerId, String reason, String userConnect)
            throws BusinessException;
}
