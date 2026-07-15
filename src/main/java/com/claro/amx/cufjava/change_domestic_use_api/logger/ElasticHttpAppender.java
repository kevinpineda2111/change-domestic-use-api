package com.claro.amx.cufjava.change_domestic_use_api.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Setter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Setter
public class ElasticHttpAppender extends AppenderBase<ILoggingEvent> {

    private String host;
    private String port;
    private String credentials;
    private String applprefix;
    private String appl;
    private String indexPattern;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    @Override
    protected void append(ILoggingEvent event) {

        try {
            String index = buildIndexName();
            String url = "http://" + host + ":" + port + "/" + index + "/_doc";

            Map<String, Object> log = new HashMap<>();
            log.put("@timestamp", Instant.ofEpochMilli(event.getTimeStamp()).toString());
            log.put("level", event.getLevel().toString());
            log.put("logger", getSimpleClassName(event.getLoggerName()));
            log.put("thread", event.getThreadName());

            Object[] args = event.getArgumentArray();

            if (args != null && args.length > 0 && args[0] != null) {
                // Caso ideal: viene tu LogDTO
                Object payload = args[0];

                if (payload instanceof String) {
                    // fallback por si mandaste string
                    log.put("message", payload);
                } else {
                    // lo convertís a mapa → JSON estructurado
                    Map<String, Object> payloadMap = mapper.convertValue(payload,
                            new TypeReference<>() {});
                    log.putAll(payloadMap);
                }

            } else {
                // fallback
                log.put("message", event.getFormattedMessage());
            }
            log.put("application", appl);
            addInfo("INDEX PATTERN: " + indexPattern);
            var json = mapper.writeValueAsString(log);

            var requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json));

            // auth básica
            if (credentials != null && credentials.contains(":")) {
                var encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
                requestBuilder.header("Authorization", "Basic " + encoded);
            }

            HttpRequest request = requestBuilder.build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //  DEBUG (clave para validar)
            if (response.statusCode() >= 300) {
                addError("Elastic ERROR: " + response.statusCode() + " → " + response.body());
            } else {
                addInfo("Elastic OK: " + response.statusCode());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            addError("Hilo interrumpido enviando log a Elastic", e);
        } catch (Exception e) {
            addError("Error enviando log a Elastic", e);
        }
    }

    private String buildIndexName() {
        ZonedDateTime now = Instant.now().atZone(ZoneId.systemDefault());
        String datePart;

        if (indexPattern != null && indexPattern.contains("ww")) {
            // Patrón semanal ISO: yyyy.ww → ej. 2026.16
            int isoYear = now.get(IsoFields.WEEK_BASED_YEAR);
            int isoWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            datePart = String.format("%04d.%02d", isoYear, isoWeek);
        } else {
            // Cualquier otro patrón delegado a DateTimeFormatter
            DateTimeFormatter formatter;
            try {
                formatter = DateTimeFormatter.ofPattern(
                        indexPattern != null ? indexPattern : "yyyy-MM-dd"
                ).withZone(ZoneId.systemDefault());
            } catch (IllegalArgumentException e) {
                addError("Pattern inválido: " + indexPattern + ", usando default yyyy-MM-dd");
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());
            }
            datePart = formatter.format(now);
        }

        return applprefix + "_idx_" + appl + "-" + datePart;
    }

    private String getSimpleClassName(String fullName) {
        if (fullName == null) return null;
        int lastDot = fullName.lastIndexOf('.');
        return lastDot != -1 ? fullName.substring(lastDot + 1) : fullName;
    }
}