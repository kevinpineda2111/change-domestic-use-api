package com.claro.amx.cufjava.change_domestic_use_api.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.claro.amx.cufjava.change_domestic_use_api.util.Utils.OBJECT_MAPPER;


@Aspect
@Component
public class LoggerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);
    private static final String END = "END";
    private static final String N_A = "N/A";
    private static final String START = "START";
    private static final String DB_START = "DB_START";
    private static final String DB_CALL = "DB_CALL";
    private static final String DB_ERROR = "DB_ERROR";
    private static final String DB_END = "DB_END";
    private static final String STATUS_ERROR = "EROR";
    private static final String NO_PARAMS = "no parameters";
    private static final String STATUS_OK = "OK";
    private static final String INFO = "INFO";
    private static final String VALUE = "value";
    private static final String TEXT  = "text";
    private static final String DEBUG = "DEBUG";
    private static final String FEIGN_ERROR = "FEIGN_ERROR";
    private static final String FEIGN_REQUEST = "FEIGN_REQUEST";
    private static final String FEIGN_RESPONSE = "FEIGN_RESPONSE";
    private static final String BODY_STR = "Body";
    private static final String REQUEST_STR = "Request";
    private static final String REQUEST_STR_LOWER = "request";
    private static final String HTTP_HEADERS_TXT = "httpHeaders";
    private static final String CORRELATION_ID = "correlationId";
    private static final List<String> RESERVED_PARAMETERS = new ArrayList<>(List.of("authorization"));
    private static final String LOGGER_CONTROLLER = "execution(* com.claro..controller..*(..))";
    private static final String LOGGER_HANDLER = "execution(* com.claro..handler..*(..)))";
    private static final String LOGGER_REPOSITORY = "execution(* com.claro..repository..*(..))";
    private static final String LOGGER_CLIENT = "execution(* com.claro..client..*(..))  && !within(@org.springframework.context.annotation.Configuration *)";


    @Value("${country-code}")
    private String country;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");


    @Around(LOGGER_CONTROLLER)
    public Object loggerToControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID)).orElse("N/A");
        loggerInit(proceedingJoinPoint, correlationId);
        return loggerExecutionToOK(proceedingJoinPoint, correlationId);
    }


    @Around(LOGGER_HANDLER)
    public Object loggerToExceptionHandlerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID)).orElse(N_A);

        final Object result = proceedingJoinPoint.proceed();

        Object responseBody = (result instanceof ResponseEntity<?> response)
                ? response.getBody()
                : result;


        logStructuredError(correlationId, responseBody, proceedingJoinPoint);

        return result;
    }

    @Around(LOGGER_REPOSITORY)
    public Object loggerToRepositoryMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID)).orElse(N_A);

        loggerInit(proceedingJoinPoint, correlationId);
        logRepositoryCall(proceedingJoinPoint);
        return loggerExecutionToOK(proceedingJoinPoint, correlationId);
    }


    private void logRepositoryCall(ProceedingJoinPoint joinPoint) throws Throwable {

        var methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID)).orElse(N_A);

        DbOperation dbOperation = methodSignature.getMethod().getAnnotation(DbOperation.class);
        String catalogName = (dbOperation != null && !dbOperation.catalog().isEmpty())
                ? dbOperation.catalog() : null;

        Map<String, Object> parameters = extractRepositoryParameters(joinPoint);

        long startTime = System.currentTimeMillis();

        logDbStart(correlationId, className, methodName, parameters);
        try {

            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            Map<String, Object> oracleParams = DbParamsHolder.getAndClear();
            if (oracleParams != null) {
                logDbCall(correlationId, className, methodName, catalogName, oracleParams);
            }

            Object response = extractResponseBody(result);
            logDbEnd(correlationId, className, methodName, response, duration);

        } catch (Exception ex) {
            DbParamsHolder.getAndClear(); // limpiar ThreadLocal siempre, incluso en error
            logDbError(correlationId, className, methodName, ex);
            throw ex;
        }
    }

    @Around(LOGGER_CLIENT)
    public Object logClientCall(ProceedingJoinPoint joinPoint) throws Throwable {

        var methodSignature = (MethodSignature) joinPoint.getSignature();
        var methodName = methodSignature.getName();
        var className = methodSignature.getDeclaringType().getSimpleName();
        String correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID)).orElse(N_A);

        logFeignRequest(correlationId, className, methodName, extractFeignParameters(joinPoint));

        long startTime = System.currentTimeMillis();

        try {

            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            Object responseBody = extractResponseBody(result);

            logFeignResponse(correlationId, className, methodName, duration, responseBody);

            return result;

        } catch (Exception ex) {
            logFeignError(ex, className, methodName, joinPoint);
            throw ex;
        }
    }

    private Map<String, Object> extractRepositoryParameters(ProceedingJoinPoint joinPoint) {
        var paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        var args = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<>();

        for (var i = 0; i < args.length; i++) {
            params.put(paramNames[i], args[i]);
        }

        return params.isEmpty() ? Map.of("info", NO_PARAMS) : params;
    }

    private Map<String, Object> extractFeignParameters(ProceedingJoinPoint joinPoint) {
        var paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        var args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();

        for (var i = 0; i < args.length; i++) {
            Object arg = args[i];
            String paramName = paramNames[i];

            if (arg instanceof HttpHeaders || RESERVED_PARAMETERS.contains(paramName)) {
                continue;
            }

            params.put(paramName, arg);
        }

        return params.isEmpty() ? Map.of("info", NO_PARAMS) : params;
    }

    private Object extractResponseBody(Object result) {
        if (result instanceof ResponseEntity<?> responseEntity) {
            return responseEntity.getBody();
        }
        return result;
    }

    private void loggerInit(ProceedingJoinPoint proceedingJoinPoint, String correlationId) {
        final var methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();

        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(INFO)
                .correlationId(correlationId)
                .action(START)
                .className(className)
                .methodName(methodName)
                .country(country)
                .request(extractControllerParameters(proceedingJoinPoint))
                .build();

        logInfo(log);


    }

    private Object loggerExecutionToOK(ProceedingJoinPoint proceedingJoinPoint, String correlationId) throws Throwable {
        final var methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();
        final var stopWatch = new StopWatch();

        stopWatch.start();
        final Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        Object responseBody = extractResponseBody(result);


        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(INFO)
                .correlationId(correlationId)
                .action(END)
                .status(STATUS_OK)
                .className(className)
                .methodName(methodName)
                .country(country)
                .request(extractControllerParameters(proceedingJoinPoint))
                .response(normalize(responseBody))
                .durationMs(stopWatch.getTotalTimeMillis())
                .build();

        logInfo(log);

        return result;
    }


    private Map<String, Object> extractControllerParameters(ProceedingJoinPoint joinPoint) {
        var paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        var args = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<>();

        for (var i = 0; i < args.length; i++) {
            Object arg = args[i];
            String paramName = paramNames[i];

            if (paramName.contains(REQUEST_STR) || paramName.contains(REQUEST_STR_LOWER)) {
                paramName = BODY_STR;
            }

            if (arg instanceof HttpHeaders ||
                    HTTP_HEADERS_TXT.equals(paramName) ||
                    RESERVED_PARAMETERS.contains(paramName)) {
                continue;
            }


            params.put(paramName, arg);
        }

        return params.isEmpty() ? Map.of("info", NO_PARAMS) : params;
    }

    private void logFeignRequest(String correlationId, String className, String methodName, Object request) {
        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(INFO)
                .correlationId(correlationId)
                .action(FEIGN_REQUEST)
                .className(className)
                .methodName(methodName)
                .country(country)
                .request(request)
                .build();

        logInfo(log);
    }

    private void logFeignResponse(String correlationId, String className, String methodName, long duration, Object response) {


        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(INFO)
                .correlationId(correlationId)
                .action(FEIGN_RESPONSE)
                .className(className)
                .methodName(methodName)
                .country(country)
                .response(response)
                .durationMs(duration)
                .build();

        logInfo(log);
    }

    private void logFeignError(Exception ex, String className, String methodName, ProceedingJoinPoint joinPoint) {

        String correlationId = Optional.ofNullable(MDC.get(CORRELATION_ID)).orElse("N/A");

        if (ex instanceof feign.RetryableException || ex.getCause() instanceof ConnectException) {

            var log = LogDTO.builder()
                    .date(LocalDateTime.now().format(formatter))
                    .level(DEBUG)
                    .status(STATUS_ERROR)
                    .correlationId(correlationId)
                    .action(FEIGN_ERROR)
                    .className(className)
                    .methodName(methodName)
                    .message(ex.getMessage())
                    .country(country)
                    .request(extractControllerParameters(joinPoint))
                    .build();
            logDebug(log);
        } else {

            var log = LogDTO.builder()
                    .date(LocalDateTime.now().format(formatter))
                    .level(STATUS_ERROR)
                    .status(STATUS_ERROR)
                    .correlationId(correlationId)
                    .action(FEIGN_ERROR)
                    .className(className)
                    .methodName(methodName)
                    .message(ex.getMessage())
                    .country(country)
                    .request(extractControllerParameters(joinPoint))
                    .build();
            logError(log);
        }
    }

    private void logStructuredError(String correlationId, Object response, ProceedingJoinPoint proceedingJoinPoint) {

        final var methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();
        Object cleanResponse = normalize(response);
        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level("ERROR")
                .status("ERROR")
                .correlationId(correlationId)
                .action("END")
                .className(className)
                .methodName(methodName)
                .country(country)
                .response(cleanResponse)
                .build();
        logError(log);
    }

    private void logDbStart(String correlationId, String className, String methodName, Map<String, Object> parameters) {


        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(INFO)
                .correlationId(correlationId)
                .action(DB_START)
                .className(className)
                .methodName(methodName)
                .country(country)
                .request(parameters)
                .build();

        logInfo(log);
    }

    private void logDbCall(String correlationId, String className, String methodName,
                           String catalogName, Map<String, Object> oracleParams) {
        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(INFO)
                .correlationId(correlationId)
                .action(DB_CALL)
                .className(className)
                .methodName(methodName)
                .operation(catalogName)
                .country(country)
                .request(oracleParams)
                .build();
        logInfo(log);
    }

    private void logDbEnd(String correlationId, String className, String methodName, Object response, long duration) {

        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(INFO)
                .correlationId(correlationId)
                .action(DB_END)
                .className(className)
                .methodName(methodName)
                .country(country)
                .response(normalize(response))
                .durationMs(duration)
                .build();


        logInfo(log);
    }

    private void logDbError(String correlationId, String className, String methodName, Exception ex) {

        var log = LogDTO.builder()
                .date(LocalDateTime.now().format(formatter))
                .level(STATUS_ERROR)
                .status(STATUS_ERROR)
                .correlationId(correlationId)
                .action(DB_ERROR)
                .className(className)
                .methodName(methodName)
                .message(ex.getMessage())
                .country(country)
                .build();

        logError(log);
    }


    private Object normalize(Object obj) {

        if (obj == null) {
            return Map.of();
        }

        return switch (obj) {


            case Optional<?> optional -> normalize(optional.orElse(null));


            case String str -> {
                try {
                    Object parsed = OBJECT_MAPPER.readValue(str, Object.class);
                    // Si el JSON parseado es escalar (no Map ni Collection),
                    // re-normalizamos para que Elasticsearch siempre reciba un objeto.
                    yield (parsed instanceof Map || parsed instanceof Collection<?>)
                            ? parsed
                            : normalize(parsed);
                } catch (Exception e) {
                    yield Map.of(TEXT, str);
                }
            }


            case Number n -> Map.of(VALUE, n);
            case Boolean b -> Map.of(VALUE, b);
            case Character c -> Map.of(VALUE, c.toString());


            case int[] intArray -> intArray;
            case long[] longArray -> longArray;
            case double[] doubleArray -> doubleArray;
            case boolean[] booleanArray -> booleanArray;


            case Object[] objArray -> objArray;


            case Collection<?> collection -> collection;


            case Map<?, ?> map -> map;


            case ResponseEntity<?> responseEntity -> normalize(responseEntity.getBody());


            case Exception ex -> Map.of(
                    "error", ex.getClass().getSimpleName(),
                    "message", ex.getMessage()
            );


            default -> obj;
        };
    }

    private void logInfo(Object payload) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("{}", payload);
        }

    }

    private void logError(Object payload) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("{}", payload);
        }
    }

    private void logDebug(Object payload) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{}", payload);
        }
    }


}