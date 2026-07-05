package com.claro.amx.cufjava.change_domestic_use_api.controller;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseResponseDTO;
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

@Tag(name = "Validate Domestic Use", description = "Validación de cambio de uso doméstico por país y tipo de negocio")
@RequestMapping("/v1/domestic-use")
public interface ValidateDomesticUseController {

    @Operation(
            summary = "Validar cambio de uso doméstico",
            description = "Valida si se permite el cambio de uso doméstico para el país y tipo de negocio dados. " +
                    "Verifica parámetros IPTV23 (países no permitidos) e IPTV24 (tipos de negocio permitidos)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validación completada",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class)))
    })
    @PostMapping("/validate")
    ResponseEntity<ChangeDomesticUseResponseDTO<ValidateDomesticUseResponseDTO>> validateDomesticUse(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody ValidateDomesticUseRequestDTO request);
}
