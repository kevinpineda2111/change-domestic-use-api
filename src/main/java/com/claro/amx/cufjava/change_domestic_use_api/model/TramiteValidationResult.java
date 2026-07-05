package com.claro.amx.cufjava.change_domestic_use_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TramiteValidationResult {

    /**
     * Return value from F_VALIDA_TRAMITE (0 = OK, -1 = error).
     */
    private int result;

    /**
     * P_PENDIENTE: 'Y' if there is a pending tramite, 'N' otherwise.
     */
    private String pending;

    /**
     * P_ERR_NUM: error number if result != 0.
     */
    private Integer errorNumber;

    /**
     * P_ERR_MSJ: error message if result != 0.
     */
    private String errorMessage;

    public boolean hasPendingTramite() {
        return "Y".equalsIgnoreCase(pending);
    }

    public boolean isSuccess() {
        return result == 0;
    }
}
