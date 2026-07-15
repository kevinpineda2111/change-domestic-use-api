package com.claro.amx.cufjava.change_domestic_use_api.logger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LogDTO {
    private String date;
    private String correlationId;
    private String level;
    private String action;
    private String status;
    private String className;
    private String methodName;
    private String source;
    private String module;
    private String message;
    private String country;
    private Long durationMs;
    private String query;
    private String procedure;
    private String parameters;
    private Object request;
    private Object response;
    private String operation;


    @Override
    public String toString() {
        try {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "LogDTO{}";
        }
    }
}
