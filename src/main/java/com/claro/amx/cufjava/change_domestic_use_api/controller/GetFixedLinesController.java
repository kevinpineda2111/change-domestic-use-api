package com.claro.amx.cufjava.change_domestic_use_api.controller;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.GetFixedLinesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.GetFixedLinesResponseDTO;
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

@Tag(name = "Fixed Lines", description = "Consulta de líneas fijas en domicilio")
@RequestMapping("/v1/fixed-lines")
public interface GetFixedLinesController {

    @Operation(
            summary = "Obtener líneas FIJA en domicilio",
            description = "Obtiene todas las líneas IF, TF e IPTV activas en el mismo domicilio que la línea de referencia, " +
                    "asociadas a la cuenta indicada. Excluye líneas en estado Inactivo (I) o Cancelado (C)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Líneas encontradas",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron líneas",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class)))
    })
    @PostMapping("/in-address")
    ResponseEntity<ChangeDomesticUseResponseDTO<GetFixedLinesResponseDTO>> getFixedLinesInAddress(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody GetFixedLinesRequestDTO request);
}
