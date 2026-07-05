package com.claro.amx.cufjava.change_domestic_use_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFixedLinesRequestDTO {

    @NotBlank(message = "El ID de cuenta es requerido")
    private String accountId;

    @NotBlank(message = "El número de línea es requerido")
    private String lineNumber;
}
