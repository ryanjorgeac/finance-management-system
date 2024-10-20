package br.ufpb.dcx.dsc.finance_management.validation;

import br.ufpb.dcx.dsc.finance_management.types.TransactionTypes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TransactionTypeValidator implements ConstraintValidator<TransactionType, String> {
    @Override
    public boolean isValid(String type, ConstraintValidatorContext constraintValidatorContext) {
        try {
            TransactionTypes gottenType = TransactionTypes.valueOf(type);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
