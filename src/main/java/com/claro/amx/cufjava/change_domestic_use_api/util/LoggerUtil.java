package com.claro.amx.cufjava.change_domestic_use_api.util;

import org.slf4j.Logger;

import java.util.Arrays;

public final class LoggerUtil {

    private LoggerUtil() {}

    public static void logEntry(Logger log, String className, String methodName, Object[] args) {
        if (log.isDebugEnabled()) {
            log.debug("[{}#{}] - ENTRY - args: {}", className, methodName,
                    args != null ? Arrays.toString(args) : "[]");
        }
    }

    public static void logExit(Logger log, String className, String methodName) {
        if (log.isDebugEnabled()) {
            log.debug("[{}#{}] - EXIT", className, methodName);
        }
    }

    public static void logError(Logger log, String className, String methodName, Throwable e) {
        log.error("[{}#{}] - ERROR - {}", className, methodName, e.getMessage(), e);
    }

    public static void logInfo(Logger log, String message, Object... params) {
        log.info(message, params);
    }

    public static void logDebug(Logger log, String message, Object... params) {
        if (log.isDebugEnabled()) {
            log.debug(message, params);
        }
    }
}
