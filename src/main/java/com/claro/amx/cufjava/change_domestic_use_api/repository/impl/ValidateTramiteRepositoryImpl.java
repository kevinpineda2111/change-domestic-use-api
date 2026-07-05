package com.claro.amx.cufjava.change_domestic_use_api.repository.impl;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.model.TramiteValidationResult;
import com.claro.amx.cufjava.change_domestic_use_api.repository.ValidateTramiteRepository;
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
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ValidateTramiteRepositoryImpl implements ValidateTramiteRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RepositoryAccess repositoryAccess;

    @Value("${user-schema}")
    private String schema;

    @Override
    public TramiteValidationResult validateTramite(String lineNumber) throws BusinessException {
        log.debug("[ValidateTramiteRepository] Calling {} for lineNumber={}", Constants.SP_F_VALIDA_TRAMITE, lineNumber);

        return repositoryAccess.execute(
                () -> executeValidateTramite(lineNumber),
                lineNumber,
                "Error al validar trámites pendientes para la línea",
                Constants.MODULE_VALIDA_TRAMITE_REPO
        );
    }

    private TramiteValidationResult executeValidateTramite(String lineNumber) {
        final var jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName(schema)
                .withFunctionName(Constants.SP_F_VALIDA_TRAMITE)
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlOutParameter(Constants.SP_RESULT_KEY, Types.NUMERIC),
                        new SqlParameter(Constants.SP_PARAM_CELLULAR_NUMBER, Types.VARCHAR),
                        new SqlOutParameter(Constants.SP_PARAM_PENDIENTE, Types.VARCHAR),
                        new SqlOutParameter(Constants.SP_PARAM_ERR_NUM, Types.NUMERIC),
                        new SqlOutParameter(Constants.SP_PARAM_ERR_MSJ, Types.VARCHAR)
                );

        var params = Map.of(Constants.SP_PARAM_CELLULAR_NUMBER, lineNumber);
        Map<String, Object> result = jdbcCall.execute(params);

        var returnValue = result.get(Constants.SP_RESULT_KEY);
        int resultCode = returnValue != null ? ((Number) returnValue).intValue() : -1;
        var pending = (String) result.get(Constants.SP_PARAM_PENDIENTE);
        var errNumRaw = result.get(Constants.SP_PARAM_ERR_NUM);
        Integer errNum = errNumRaw != null ? ((Number) errNumRaw).intValue() : null;
        var errMsj = (String) result.get(Constants.SP_PARAM_ERR_MSJ);

        log.debug("[ValidateTramiteRepository] Result: result={}, pending={}, errNum={}, errMsj={}",
                resultCode, pending, errNum, errMsj);

        return TramiteValidationResult.builder()
                .result(resultCode)
                .pending(pending)
                .errorNumber(errNum)
                .errorMessage(errMsj)
                .build();
    }
}
