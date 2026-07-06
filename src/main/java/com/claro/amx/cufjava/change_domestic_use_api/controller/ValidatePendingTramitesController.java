package com.claro.amx.cufjava.change_domestic_use_api.controller;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidatePendingTramitesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidatePendingTramitesResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Validate Pending Tramites", description = "Validación de trámites pendientes en línea FIJA")
@RequestMapping("/tramites")
public interface ValidatePendingTramitesController {

    @Operation(
            summary = "Validar trámites pendientes",
            description = "Verifica si una línea tiene trámites pendientes mediante F_VALIDA_TRAMITE. " +
                    "Si existen trámites pendientes (P_PENDIENTE = 'Y'), retorna error de negocio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validación completada - sin trámites pendientes",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Línea con trámites pendientes",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class)))
    })
    @PostMapping("/validate-pending")
    ResponseEntity<ChangeDomesticUseResponseDTO<ValidatePendingTramitesResponseDTO>> validatePendingTramites(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody ValidatePendingTramitesRequestDTO request);
}
