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
public class SaveDomesticUseChangeResponseDTO {

    private boolean success;
    private String message;
    private String accountId;
    private String referenceLineNumber;
    private String previousDomesticUse;
    private String newDomesticUse;
    private List<String> processedLines;
    private int totalProcessedLines;
}
