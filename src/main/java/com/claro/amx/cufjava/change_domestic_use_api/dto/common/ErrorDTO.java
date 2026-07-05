package com.claro.amx.cufjava.change_domestic_use_api.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

    private String code;
    private String message;
    private String source;
    private String timestamp;
}
