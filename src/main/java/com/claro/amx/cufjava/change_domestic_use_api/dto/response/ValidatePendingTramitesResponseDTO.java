package com.claro.amx.cufjava.change_domestic_use_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatePendingTramitesResponseDTO {

    private String lineNumber;
    private boolean hasPendingTramites;
    private String message;
}
