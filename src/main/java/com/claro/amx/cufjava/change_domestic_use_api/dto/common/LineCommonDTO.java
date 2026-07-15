package com.claro.amx.cufjava.change_domestic_use_api.dto.common;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LineCommonDTO {
    private String lineNumber;
    private String accountId;
    private String lineStatus;
    private String lineBusinessType;
    private String serialNumber;
    private String category;
}
