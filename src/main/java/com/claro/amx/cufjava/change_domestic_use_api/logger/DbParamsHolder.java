package com.claro.amx.cufjava.change_domestic_use_api.logger;

import java.util.Map;

/**
 * Holder ThreadLocal que permite a los repositorios publicar los parámetros
 * Oracle resueltos (incluyendo constantes hardcodeadas) para que el AOP
 * ({@link LoggerAspect}) los incluya en el log {@code DB_END} automáticamente.
 *
 * <p>Uso en el repositorio:
 * <pre>{@code
 *   var params = buildParams(dto);
 *   DbParamsHolder.set(params.getValues());
 * }</pre>
 *
 * <p>El AOP llama a {@link #getAndClear()} tras {@code joinPoint.proceed()},
 * garantizando que el ThreadLocal se limpie siempre.
 */
public final class DbParamsHolder {

    private static final ThreadLocal<Map<String, Object>> PARAMS = new ThreadLocal<>();

    private DbParamsHolder() {}

    /** Publica los parámetros Oracle resueltos para el hilo actual. */
    public static void set(Map<String, Object> params) {
        PARAMS.set(params);
    }

    /**
     * Obtiene los parámetros publicados y limpia el ThreadLocal.
     *
     * @return mapa de parámetros o {@code null} si no se publicaron.
     */
    public static Map<String, Object> getAndClear() {
        Map<String, Object> params = PARAMS.get();
        PARAMS.remove();
        return params;
    }
}

