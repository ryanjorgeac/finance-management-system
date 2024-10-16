package br.ufpb.dcx.dsc.finance_management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TransactionTypeValidator.class)
@Documented
public @interface TransactionType {
    String message() default "Only 'INCOMING' or 'OUTCOMING' types are accepted.";

    Class<?>[] groups() default {};

    Class<? extends Payload[]>[] payload() default { };
}
