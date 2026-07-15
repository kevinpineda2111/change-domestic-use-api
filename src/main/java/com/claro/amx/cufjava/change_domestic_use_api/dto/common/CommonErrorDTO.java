package com.claro.amx.cufjava.change_domestic_use_api.dto.common;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonErrorDTO {
    private String code;
    private String message;
}

