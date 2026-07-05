package com.claro.amx.cufjava.change_domestic_use_api.service.impl;

import com.claro.amx.cufjava.change_domestic_use_api.dto.request.GetFixedLinesRequestDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.FixedLineDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.response.GetFixedLinesResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.repository.FixedLineInAddressRepository;
import com.claro.amx.cufjava.change_domestic_use_api.service.GetFixedLinesInAddressService;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetFixedLinesInAddressServiceImpl implements GetFixedLinesInAddressService {

    private final FixedLineInAddressRepository fixedLineInAddressRepository;

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public GetFixedLinesResponseDTO getFixedLinesInAddress(GetFixedLinesRequestDTO request) throws BusinessException {
        log.info("[GetFixedLinesInAddressService] Getting lines for accountId={}, lineNumber={}",
                request.getAccountId(), request.getLineNumber());

        try {
            var entities = fixedLineInAddressRepository.getFixedLinesInAddress(
                    request.getAccountId(), request.getLineNumber());

            if (entities == null || entities.isEmpty()) {
                log.warn("[GetFixedLinesInAddressService] No lines found for accountId={}, lineNumber={}",
                        request.getAccountId(), request.getLineNumber());
                throw new BusinessException(
                        Constants.CODE_NOT_FOUND,
                        "No se encontraron líneas FIJA activas en el domicilio de la línea " + request.getLineNumber(),
                        Constants.MODULE_GET_FIXED_LINES
                );
            }

            List<FixedLineDTO> lines = entities.stream()
                    .map(e -> FixedLineDTO.builder()
                            .lineNumber(e.getLineNumber())
                            .businessType(e.getBusinessType())
                            .status(e.getStatus())
                            .build())
                    .toList();

            log.info("[GetFixedLinesInAddressService] Found {} lines for accountId={}",
                    lines.size(), request.getAccountId());

            return GetFixedLinesResponseDTO.builder()
                    .accountId(request.getAccountId())
                    .referenceLineNumber(request.getLineNumber())
                    .lines(lines)
                    .totalLines(lines.size())
                    .build();

        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            log.error("[GetFixedLinesInAddressService] Unexpected error: {}", e.getMessage(), e);
            throw new BusinessException(
                    Constants.CODE_SERVER_ERROR,
                    "Error al obtener líneas FIJA en el domicilio: " + e.getMessage(),
                    Constants.MODULE_GET_FIXED_LINES
            );
        }
    }
}
