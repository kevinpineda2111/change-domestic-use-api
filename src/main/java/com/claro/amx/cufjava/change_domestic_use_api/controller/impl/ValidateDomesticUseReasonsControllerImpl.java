package com.claro.amx.cufjava.change_domestic_use_api.controller.impl;

import com.claro.amx.cufjava.change_domestic_use_api.controller.ValidateDomesticUseReasonsController;
import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseReasonsRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseReasonsResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.service.ValidateDomesticUseReasonsService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidateDomesticUseReasonsControllerImpl implements ValidateDomesticUseReasonsController {

    private final ValidateDomesticUseReasonsService validateDomesticUseReasonsService;

    @Override
    public ResponseEntity<ChangeDomesticUseResponseDTO<ValidateDomesticUseReasonsResponseDTO>> validateReasons(
            HttpHeaders headers, ValidateDomesticUseReasonsRequestDTO request) {
        try {
            var result = validateDomesticUseReasonsService.validateReasons(request);
            return Utils.responseChangeDomesticUse(result);
        } catch (BusinessException e) {
            return Utils.responseFromBusinessException(e);
        } catch (Exception e) {
            return Utils.responseFromException(e);
        }
    }
}
