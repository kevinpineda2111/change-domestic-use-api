package com.claro.amx.cufjava.change_domestic_use_api.repository.impl;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.ParameterCharValue;
import com.claro.amx.cufjava.change_domestic_use_api.repository.ParameterRepository;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import com.claro.amx.cufjava.change_domestic_use_api.util.RepositoryAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ParameterRepositoryImpl implements ParameterRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RepositoryAccess repositoryAccess;

    @Value("${user-schema}")
    private String schema;

    @Override
    public ParameterCharValue getCharParameter(String parameterName) throws BusinessException {
        return repositoryAccess.execute(
                () -> executeGetCharParameter(parameterName),
                parameterName,
                "Error al obtener parámetro " + parameterName,
                Constants.MODULE_PARAMETER_REPO
        );
    }

    private ParameterCharValue executeGetCharParameter(String parameterName) {
        final var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName(schema)
                .withCatalogName(Constants.SP_PACKAGE_PA_TIF_PARAMETERS)
                .withFunctionName(Constants.SP_F_GET_CHAR)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.SP_RESULT_KEY, Types.NUMERIC),
                        new SqlParameter(Constants.SP_PARAM_PIN_PARAMETER, Types.VARCHAR),
                        new SqlOutParameter(Constants.SP_PARAM_POUT_CHAR_VALUE, Types.VARCHAR),
                        new SqlOutParameter(Constants.SP_PARAM_POUT_ERR_MSG, Types.VARCHAR)
                );

        var params = Map.of(Constants.SP_PARAM_PIN_PARAMETER, parameterName);
        Map<String, Object> result = jdbcCall.execute(params);

        var returnValue = result.get(Constants.SP_RESULT_KEY);
        int resultCode = returnValue != null ? ((Number) returnValue).intValue() : -1;
        var charValue = (String) result.get(Constants.SP_PARAM_POUT_CHAR_VALUE);
        var errMsg = (String) result.get(Constants.SP_PARAM_POUT_ERR_MSG);

        return ParameterCharValue.builder()
                .result(resultCode)
                .charValue(charValue)
                .errorMessage(errMsg)
                .build();
    }
}
