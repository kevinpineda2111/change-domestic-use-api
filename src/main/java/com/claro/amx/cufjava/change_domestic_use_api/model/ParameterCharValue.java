package com.claro.amx.cufjava.change_domestic_use_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterCharValue {

    /**
     * Return value from PA_TIF_PARAMETERS.F_GET_CHAR (0 = OK, negative = error).
     */
    private int result;

    /**
     * POUT_CHAR_VALUE: the returned parameter value.
     */
    private String charValue;

    /**
     * POUT_ERR_MSG: error message if result != 0.
     */
    private String errorMessage;

    public boolean isSuccess() {
        return result == 0;
    }
}
