package com.claro.amx.cufjava.change_domestic_use_api.dto.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CommonResponseDTO {
    private String status;
    private String source;
    private String api;
    private CommonErrorDTO error;
}
