package com.claro.amx.cufjava.change_domestic_use_api.controller.impl;

import com.claro.amx.cufjava.change_domestic_use_api.controller.ValidatePendingTramitesController;
import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidatePendingTramitesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidatePendingTramitesResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.service.ValidatePendingTramitesService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ValidatePendingTramitesControllerImpl implements ValidatePendingTramitesController {

    private final ValidatePendingTramitesService validatePendingTramitesService;

    @Override
    public ResponseEntity<ChangeDomesticUseResponseDTO<ValidatePendingTramitesResponseDTO>> validatePendingTramites(
            HttpHeaders headers, ValidatePendingTramitesRequestDTO request) {
        log.info("[ValidatePendingTramitesController] validatePendingTramites - lineNumber={}",
                request.getLineNumber());
        try {
            var result = validatePendingTramitesService.validatePendingTramites(request);
            return Utils.responseChangeDomesticUse(result);
        } catch (BusinessException e) {
            return Utils.responseFromBusinessException(e);
        } catch (Exception e) {
            log.error("[ValidatePendingTramitesController] Unexpected error: {}", e.getMessage(), e);
            return Utils.responseFromException(e);
        }
    }
}
