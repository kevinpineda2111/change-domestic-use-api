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
public class ValidatePendingTramitesRequestDTO {

    @NotBlank(message = "El número de línea es requerido")
    private String lineNumber;
}
