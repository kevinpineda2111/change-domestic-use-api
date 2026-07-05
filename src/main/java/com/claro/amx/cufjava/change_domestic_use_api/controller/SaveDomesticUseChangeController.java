package com.claro.amx.cufjava.change_domestic_use_api.controller;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.SaveDomesticUseChangeRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.SaveDomesticUseChangeResponseDTO;
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

@Tag(name = "Save Domestic Use Change", description = "Guardado del cambio de uso doméstico (BOTON GRABAR)")
@RequestMapping("/v1/domestic-use")
public interface SaveDomesticUseChangeController {

    @Operation(
            summary = "Guardar cambio de uso doméstico",
            description = """
                    Ejecuta el flujo completo de cambio de uso doméstico para líneas FIJA (IF, TF, IPTV).

                    Flujo:
                    1. Valida que no se cambie directamente entre DDI (1) y DDN (2)
                    2. Obtiene todas las líneas FIJA en el mismo domicilio
                    3. Para cada línea: valida trámites pendientes (F_VALIDA_TRAMITE)
                    4a. Si nuevo uso = '5' (SOLO ENTRANTE): llama PRESUSPE por cada línea
                    4b. Si uso anterior = '5' y nuevo = '1'(DDI) o '2'(DDN): llama LEVANTA_PRESUSP por cada línea
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cambio guardado exitosamente",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida o regla de negocio violada",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron líneas en el domicilio",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class)))
    })
    @PostMapping("/save")
    ResponseEntity<ChangeDomesticUseResponseDTO<SaveDomesticUseChangeResponseDTO>> saveDomesticUseChange(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody SaveDomesticUseChangeRequestDTO request);
}
