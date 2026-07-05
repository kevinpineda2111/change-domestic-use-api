package com.claro.amx.cufjava.change_domestic_use_api.controller.impl;

import com.claro.amx.cufjava.change_domestic_use_api.controller.GetFixedLinesController;
import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.GetFixedLinesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.GetFixedLinesResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.service.GetFixedLinesInAddressService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GetFixedLinesControllerImpl implements GetFixedLinesController {

    private final GetFixedLinesInAddressService getFixedLinesInAddressService;

    @Override
    public ResponseEntity<ChangeDomesticUseResponseDTO<GetFixedLinesResponseDTO>> getFixedLinesInAddress(
            HttpHeaders headers, GetFixedLinesRequestDTO request) {
        log.info("[GetFixedLinesController] getFixedLinesInAddress - accountId={}, lineNumber={}",
                request.getAccountId(), request.getLineNumber());
        try {
            var result = getFixedLinesInAddressService.getFixedLinesInAddress(request);
            return Utils.responseChangeDomesticUse(result);
        } catch (BusinessException e) {
            return Utils.responseFromBusinessException(e);
        } catch (Exception e) {
            log.error("[GetFixedLinesController] Unexpected error: {}", e.getMessage(), e);
            return Utils.responseFromException(e);
        }
    }
}
