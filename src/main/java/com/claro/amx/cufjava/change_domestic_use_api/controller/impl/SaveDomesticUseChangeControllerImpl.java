package com.claro.amx.cufjava.change_domestic_use_api.controller.impl;

import com.claro.amx.cufjava.change_domestic_use_api.controller.SaveDomesticUseChangeController;
import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.SaveDomesticUseChangeRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.SaveDomesticUseChangeResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.service.SaveDomesticUseChangeService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SaveDomesticUseChangeControllerImpl implements SaveDomesticUseChangeController {

    private final SaveDomesticUseChangeService saveDomesticUseChangeService;

    @Override
    public ResponseEntity<ChangeDomesticUseResponseDTO<SaveDomesticUseChangeResponseDTO>> saveDomesticUseChange(
            HttpHeaders headers, SaveDomesticUseChangeRequestDTO request) {
        log.info("[SaveDomesticUseChangeController] saveDomesticUseChange - lineNumber={}, accountId={}, " +
                        "previousUse={}, newUse={}",
                request.getLineNumber(), request.getAccountId(),
                request.getPreviousDomesticUse(), request.getNewDomesticUse());
        try {
            var result = saveDomesticUseChangeService.saveDomesticUseChange(request);
            return Utils.responseChangeDomesticUse(result);
        } catch (BusinessException e) {
            return Utils.responseFromBusinessException(e);
        } catch (Exception e) {
            log.error("[SaveDomesticUseChangeController] Unexpected error: {}", e.getMessage(), e);
            return Utils.responseFromException(e);
        }
    }
}
