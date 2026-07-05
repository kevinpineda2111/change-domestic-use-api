package com.claro.amx.cufjava.change_domestic_use_api.repository;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.ParameterCharValue;

public interface ParameterRepository {

    /**
     * Calls PA_TIF_PARAMETERS.F_GET_CHAR to retrieve a configuration parameter value.
     *
     * @param parameterName the parameter name to look up (e.g. IPTV23, IPTV24, PNTLF)
     * @return ParameterCharValue with the result and value
     * @throws BusinessException if an error occurs calling the stored procedure
     */
    ParameterCharValue getCharParameter(String parameterName) throws BusinessException;
}
