package com.claro.amx.cufjava.change_domestic_use_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateDomesticUseReasonsResponseDTO {

    private boolean allowed;
    private String reasonId;
    private String businessType;
    private String message;
}
