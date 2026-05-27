package app.finplan.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private ValidPassword annotation;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        boolean hasMinLength = value.length() >= 8;
        boolean hasLetter    = value.chars().anyMatch(Character::isLetter);
        boolean hasDigit     = value.chars().anyMatch(Character::isDigit);

        boolean valid = hasMinLength && hasLetter && hasDigit;
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(annotation.message())
                    .addConstraintViolation();
        }
        return valid;
    }
}
