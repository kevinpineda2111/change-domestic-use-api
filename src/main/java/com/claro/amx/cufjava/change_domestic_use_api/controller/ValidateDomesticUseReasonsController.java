package com.claro.amx.cufjava.change_domestic_use_api.controller;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseReasonsRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseReasonsResponseDTO;
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

@Tag(name = "Validate Domestic Use Reasons", description = "Validación de razones de cambio de uso doméstico por tipo de negocio")
@RequestMapping("/domestic-use")
public interface ValidateDomesticUseReasonsController {

    @Operation(
            summary = "Validar razones de cambio de uso doméstico",
            description = "Valida que la razón seleccionada sea permitida para el tipo de negocio. " +
                    "Para líneas IF, TF e IPTV, las razones contenidas en el parámetro PNTLF " +
                    "(e.g. #PEDBAJ#PBCOR#) no están permitidas."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Razón válida",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Razón no permitida para el tipo de negocio",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ChangeDomesticUseResponseDTO.class)))
    })
    @PostMapping("/validate-reasons")
    ResponseEntity<ChangeDomesticUseResponseDTO<ValidateDomesticUseReasonsResponseDTO>> validateReasons(
            @RequestHeader HttpHeaders headers,
            @Valid @RequestBody ValidateDomesticUseReasonsRequestDTO request);
}
