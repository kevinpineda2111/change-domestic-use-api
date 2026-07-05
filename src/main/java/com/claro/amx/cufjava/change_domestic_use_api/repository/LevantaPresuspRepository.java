package com.claro.amx.cufjava.change_domestic_use_api.repository;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.StoredProcResult;

public interface LevantaPresuspRepository {

    /**
     * Calls LEVANTA_PRESUSP to lift a pre-suspension from a line.
     *
     * @param lineNumber    the line number (P_CELLULAR_NUMBER)
     * @param actId         the activity ID (P_ACT_ID), typically 'CMBUD'
     * @param reasonId      the reason code (P_RSN_ID)
     * @param ticklerId     the tickler/case number (P_TCK)
     * @param description   description of the action (P_DESCRIPCION)
     * @return StoredProcResult with the return code and optional error message
     * @throws BusinessException if an error occurs calling the stored procedure
     */
    StoredProcResult callLevantaPresusp(String lineNumber, String actId, String reasonId,
                                        Long ticklerId, String description) throws BusinessException;
}
