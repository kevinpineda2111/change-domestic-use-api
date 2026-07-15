package com.claro.amx.cufjava.change_domestic_use_api.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LinesInAddressDTO {
    private String lineNumber;
    private String lineBusinessType;
    private String lineStatus;
    private String lineType;

}
