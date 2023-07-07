package com.tacs.backend.security;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * @author tianshuwang
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ConfirmedFieldValidator.class)
@Documented
public @interface ConfirmedField {

    String message() default "Doesn't match the original";

    String originalField();

    String confirmationField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.TYPE})
    @Retention(RUNTIME)
    @interface List {
        ConfirmedField[] value();
    }
}