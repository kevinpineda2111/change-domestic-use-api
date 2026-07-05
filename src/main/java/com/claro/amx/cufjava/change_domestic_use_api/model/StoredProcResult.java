package com.claro.amx.cufjava.change_domestic_use_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoredProcResult {

    /**
     * Return value from stored procedure/function (0 = OK, negative = error).
     */
    private int result;

    /**
     * Optional error message returned by the stored procedure.
     */
    private String errorMessage;

    public boolean isSuccess() {
        return result == 0;
    }
}
