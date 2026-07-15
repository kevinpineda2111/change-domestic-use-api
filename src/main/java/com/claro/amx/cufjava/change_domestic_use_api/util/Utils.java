package com.claro.amx.cufjava.change_domestic_use_api.util;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ErrorDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Utils {

    private Utils() {}
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Builds a successful ResponseEntity with a ChangeDomesticUseResponseDTO body.
     */
    public static <T> ResponseEntity<ChangeDomesticUseResponseDTO<T>> responseChangeDomesticUse(T data) {
        var response = ChangeDomesticUseResponseDTO.<T>builder()
                .code(Constants.CODE_SUCCESS)
                .message(Constants.MSG_SUCCESS)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Builds a ResponseEntity from a BusinessException.
     */
    public static <T> ResponseEntity<ChangeDomesticUseResponseDTO<T>> responseFromBusinessException(BusinessException ex) {
        var error = ErrorDTO.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .source(ex.getSource())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();

        var response = ChangeDomesticUseResponseDTO.<T>builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .error(error)
                .build();

        var httpStatus = switch (ex.getCode()) {
            case Constants.CODE_BAD_REQUEST -> HttpStatus.BAD_REQUEST;
            case Constants.CODE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return ResponseEntity.status(httpStatus).body(response);
    }

    /**
     * Builds a ResponseEntity for generic unexpected exceptions.
     */
    public static <T> ResponseEntity<ChangeDomesticUseResponseDTO<T>> responseFromException(Exception ex) {
        var error = ErrorDTO.builder()
                .code(Constants.CODE_SERVER_ERROR)
                .message(Constants.MSG_SERVER_ERROR + ": " + ex.getMessage())
                .source("UnexpectedException")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();

        var response = ChangeDomesticUseResponseDTO.<T>builder()
                .code(Constants.CODE_SERVER_ERROR)
                .message(Constants.MSG_SERVER_ERROR)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .error(error)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Extracts a header value or returns empty string if absent.
     */
    public static String getHeader(HttpHeaders headers, String headerName) {
        var values = headers.get(headerName);
        if (values == null || values.isEmpty()) return "";
        return values.getFirst();
    }

    /**
     * Checks if a business type is a FIJA type (IF, TF, IPTV).
     */
    public static boolean isFijaBusinessType(String businessType) {
        if (businessType == null) return false;
        return switch (businessType.toUpperCase()) {
            case Constants.BUSINESS_TYPE_IF, Constants.BUSINESS_TYPE_TF, Constants.BUSINESS_TYPE_IPTV -> true;
            default -> false;
        };
    }
}
