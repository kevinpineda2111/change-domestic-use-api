package com.claro.amx.cufjava.change_domestic_use_api.util;

import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RepositoryAccess {

    /**
     * Executes a repository call wrapping any exception into a BusinessException.
     *
     * @param repositoryCall the supplier that calls the repository
     * @param parameter      optional parameter info to include in error message
     * @param errorMessage   base error message
     * @param module         calling module name for tracing
     * @param <T>            return type
     * @return result of the repository call
     * @throws BusinessException if any exception occurs
     */
    public <T> T execute(Supplier<T> repositoryCall,
                         String parameter,
                         String errorMessage,
                         String module) throws BusinessException {
        try {
            return repositoryCall.get();
        } catch (Exception e) {
            var paramPart = (parameter != null && !parameter.isBlank()) ? " - " + parameter : "";
            var fullMessage = errorMessage + paramPart + " - " + e.getMessage();
            throw new BusinessException(Constants.CODE_SERVER_ERROR, fullMessage, module);
        }
    }

    /**
     * Executes a repository call with no parameter context.
     */
    public <T> T execute(Supplier<T> repositoryCall,
                         String errorMessage,
                         String module) throws BusinessException {
        return execute(repositoryCall, null, errorMessage, module);
    }
}
