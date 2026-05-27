package app.finplan.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^$|^7\\d{10}$")
@ReportAsSingleViolation
public @interface PhoneRu {

    String message() default "Format 79999999999";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
