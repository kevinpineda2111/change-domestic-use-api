package com.claro.amx.cufjava.change_domestic_use_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateDomesticUseResponseDTO {

    private boolean allowed;
    private String reason;
}
