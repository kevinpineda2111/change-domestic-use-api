package com.claro.amx.cufjava.change_domestic_use_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveDomesticUseChangeRequestDTO {

    @NotBlank(message = "El ID de cuenta es requerido")
    private String accountId;

    @NotBlank(message = "El número de línea es requerido")
    private String lineNumber;

    @NotBlank(message = "El uso doméstico anterior es requerido")
    private String previousDomesticUse;

    @NotBlank(message = "El nuevo uso doméstico es requerido")
    private String newDomesticUse;

    @NotNull(message = "El ID de tickler es requerido")
    private Long ticklerId;

    @NotBlank(message = "El ID de razón es requerido")
    private String reasonId;

    @NotBlank(message = "El ID de usuario es requerido")
    private String userId;

    private String businessType;
    private String country;
}
