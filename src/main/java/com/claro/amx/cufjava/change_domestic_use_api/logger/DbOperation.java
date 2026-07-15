package com.claro.amx.cufjava.change_domestic_use_api.logger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación para identificar el paquete y función/procedimiento de Oracle
 * que ejecuta un método de repositorio.
 * <p>
 * Ejemplo de uso:
 * <pre>
 * {@code @DbOperation(catalog = "PKG_CHARGE_CANCELATION", function = "GET_ACTIVATION_CHARGE")}
 * public ActivationChargeResult getActivationCharge(String nim) { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbOperation {
    /** Nombre del paquete Oracle (catalogName). Ej: "PKG_CHARGE_CANCELATION" */
    String catalog() default "";

    /** Nombre de la función o procedimiento Oracle. Ej: "GET_ACTIVATION_CHARGE" */
    String function() default "";
}

