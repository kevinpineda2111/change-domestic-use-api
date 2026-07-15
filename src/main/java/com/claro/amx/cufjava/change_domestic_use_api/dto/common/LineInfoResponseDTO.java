package com.claro.amx.cufjava.change_domestic_use_api.dto.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LineInfoResponseDTO {
    private LineCommonDTO lineInfo;
    private List<LinesInAddressDTO> linesInAddress;
    private CommonResponseDTO commonResponseDTO;
}

