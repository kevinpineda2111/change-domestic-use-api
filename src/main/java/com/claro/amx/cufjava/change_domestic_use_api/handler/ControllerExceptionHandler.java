package com.claro.amx.cufjava.change_domestic_use_api.handler;

import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ChangeDomesticUseResponseDTO;
import com.claro.amx.cufjava.change_domestic_use_api.dto.common.ErrorDTO;
import com.claro.amx.cufjava.change_domestic_use_api.exception.BusinessException;
import com.claro.amx.cufjava.change_domestic_use_api.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ChangeDomesticUseResponseDTO<Void>> handleBusinessException(BusinessException ex) {
        var error = ErrorDTO.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .source(ex.getSource())
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();

        var response = ChangeDomesticUseResponseDTO.<Void>builder()
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ChangeDomesticUseResponseDTO<Void>> handleValidationException(
            MethodArgumentNotValidException ex) {
        var errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        var error = ErrorDTO.builder()
                .code(Constants.CODE_BAD_REQUEST)
                .message("Validation failed: " + errorMessages)
                .source("RequestValidation")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();

        var response = ChangeDomesticUseResponseDTO.<Void>builder()
                .code(Constants.CODE_BAD_REQUEST)
                .message("Validation failed: " + errorMessages)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .error(error)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ChangeDomesticUseResponseDTO<Void>> handleGenericException(Exception ex) {
        var error = ErrorDTO.builder()
                .code(Constants.CODE_SERVER_ERROR)
                .message(Constants.MSG_SERVER_ERROR + ": " + ex.getMessage())
                .source("UnexpectedException")
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .build();

        var response = ChangeDomesticUseResponseDTO.<Void>builder()
                .code(Constants.CODE_SERVER_ERROR)
                .message(Constants.MSG_SERVER_ERROR)
                .timestamp(LocalDateTime.now().format(FORMATTER))
                .error(error)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
