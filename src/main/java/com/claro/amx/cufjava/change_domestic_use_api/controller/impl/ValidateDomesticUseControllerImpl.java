package com.claro.amx.cufjava.change_domestic_use_api.controller.impl;

import com.claro.amx.cufjava.change_domestic_use_api.controller.ValidateDomesticUseController;
import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.request.ValidateDomesticUseRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.ValidateDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.service.ValidateDomesticUseService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidateDomesticUseControllerImpl implements ValidateDomesticUseController {

    private final ValidateDomesticUseService validateDomesticUseService;

    @Override
    public ResponseEntity<ChangeDomesticUseResponseDTO<ValidateDomesticUseResponseDTO>> validateDomesticUse(
            HttpHeaders headers, ValidateDomesticUseRequestDTO request) {
        try {
            var result = validateDomesticUseService.validateDomesticUse(request);
            return Utils.responseChangeDomesticUse(result);
        } catch (BusinessException e) {
            return Utils.responseFromBusinessException(e);
        } catch (Exception e) {
            return Utils.responseFromException(e);
        }
    }
}
