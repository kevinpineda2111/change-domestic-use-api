package com.claro.amx.cufjava.change_domestic_use_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFixedLinesResponseDTO {

    private String accountId;
    private String referenceLineNumber;
    private List<FixedLineDTO> lines;
    private int totalLines;
}
