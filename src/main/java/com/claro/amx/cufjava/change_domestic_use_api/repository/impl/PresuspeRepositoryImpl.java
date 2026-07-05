package com.claro.amx.cufjava.change_domestic_use_api.repository.impl;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.StoredProcResult;
import com.claro.amx.cufjava.change_domestic_use_api.repository.PresuspeRepository;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import com.claro.amx.cufjava.change_domestic_use_api.util.RepositoryAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PresuspeRepositoryImpl implements PresuspeRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RepositoryAccess repositoryAccess;

    @Value("${user-schema}")
    private String schema;

    @Override
    public StoredProcResult callPresuspe(String lineNumber, Long ticklerId, String reason, String userConnect)
            throws BusinessException {
        log.debug("[PresuspeRepository] Calling {} for lineNumber={}, ticklerId={}, reason={}",
                Constants.SP_PRESUSPE, lineNumber, ticklerId, reason);

        return repositoryAccess.execute(
                () -> executePresuspe(lineNumber, ticklerId, reason, userConnect),
                lineNumber,
                "Error al ejecutar PRESUSPE para la línea",
                Constants.MODULE_PRESUSPE_REPO
        );
    }

    private StoredProcResult executePresuspe(String lineNumber, Long ticklerId, String reason, String userConnect) {
        final var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName(schema)
                .withFunctionName(Constants.SP_PRESUSPE)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.SP_RESULT_KEY, Types.NUMERIC),
                        new SqlParameter(Constants.SP_PARAM_CELULAR, Types.VARCHAR),
                        new SqlParameter(Constants.SP_PARAM_TCK, Types.NUMERIC),
                        new SqlParameter(Constants.SP_PARAM_RAZON, Types.VARCHAR),
                        new SqlParameter(Constants.SP_PARAM_USER_CONECT, Types.VARCHAR),
                        new SqlOutParameter(Constants.SP_PARAM_POUT_MSG_ERR, Types.VARCHAR)
                );

        var params = new HashMap<String, Object>();
        params.put(Constants.SP_PARAM_CELULAR, lineNumber);
        params.put(Constants.SP_PARAM_TCK, ticklerId);
        params.put(Constants.SP_PARAM_RAZON, reason);
        params.put(Constants.SP_PARAM_USER_CONECT, userConnect);

        Map<String, Object> result = jdbcCall.execute(params);

        var returnValue = result.get(Constants.SP_RESULT_KEY);
        int resultCode = returnValue != null ? ((Number) returnValue).intValue() : -1;
        var errMsg = (String) result.get(Constants.SP_PARAM_POUT_MSG_ERR);

        log.debug("[PresuspeRepository] Result: result={}, errMsg={}", resultCode, errMsg);

        return StoredProcResult.builder()
                .result(resultCode)
                .errorMessage(errMsg)
                .build();
    }
}
