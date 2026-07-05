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
public class ValidateDomesticUseReasonsRequestDTO {

    @NotBlank(message = "El tipo de negocio es requerido")
    private String businessType;

    @NotBlank(message = "El ID de razón es requerido")
    private String reasonId;
}
