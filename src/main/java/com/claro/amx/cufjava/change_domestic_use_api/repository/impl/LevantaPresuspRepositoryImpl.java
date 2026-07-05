package com.claro.amx.cufjava.change_domestic_use_api.repository.impl;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.StoredProcResult;
import com.claro.amx.cufjava.change_domestic_use_api.repository.LevantaPresuspRepository;
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
public class LevantaPresuspRepositoryImpl implements LevantaPresuspRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RepositoryAccess repositoryAccess;

    @Value("${user-schema}")
    private String schema;

    @Override
    public StoredProcResult callLevantaPresusp(String lineNumber, String actId, String reasonId,
                                               Long ticklerId, String description) throws BusinessException {
        log.debug("[LevantaPresuspRepository] Calling {} for lineNumber={}, actId={}, reasonId={}, ticklerId={}",
                Constants.SP_LEVANTA_PRESUSP, lineNumber, actId, reasonId, ticklerId);

        return repositoryAccess.execute(
                () -> executeLevantaPresusp(lineNumber, actId, reasonId, ticklerId, description),
                lineNumber,
                "Error al ejecutar LEVANTA_PRESUSP para la línea",
                Constants.MODULE_LEVANTA_PRESUSP_REPO
        );
    }

    private StoredProcResult executeLevantaPresusp(String lineNumber, String actId, String reasonId,
                                                   Long ticklerId, String description) {
        final var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName(schema)
                .withFunctionName(Constants.SP_LEVANTA_PRESUSP)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.SP_RESULT_KEY, Types.NUMERIC),
                        new SqlParameter(Constants.SP_PARAM_P_CELLULAR_NUMBER, Types.VARCHAR),
                        new SqlParameter(Constants.SP_PARAM_P_ACT_ID, Types.VARCHAR),
                        new SqlParameter(Constants.SP_PARAM_P_RSN_ID, Types.VARCHAR),
                        new SqlParameter(Constants.SP_PARAM_P_TCK, Types.NUMERIC),
                        new SqlParameter(Constants.SP_PARAM_P_DESCRIPCION, Types.VARCHAR)
                );

        var params = new HashMap<String, Object>();
        params.put(Constants.SP_PARAM_P_CELLULAR_NUMBER, lineNumber);
        params.put(Constants.SP_PARAM_P_ACT_ID, actId);
        params.put(Constants.SP_PARAM_P_RSN_ID, reasonId);
        params.put(Constants.SP_PARAM_P_TCK, ticklerId);
        params.put(Constants.SP_PARAM_P_DESCRIPCION, description);

        Map<String, Object> result = jdbcCall.execute(params);

        var returnValue = result.get(Constants.SP_RESULT_KEY);
        int resultCode = returnValue != null ? ((Number) returnValue).intValue() : -1;

        log.debug("[LevantaPresuspRepository] Result: result={}", resultCode);

        return StoredProcResult.builder()
                .result(resultCode)
                .errorMessage(resultCode != Constants.SP_RESULT_OK
                        ? "LEVANTA_PRESUSP retornó código de error: " + resultCode
                        : null)
                .build();
    }
}
