package app.finplan.handler;

import app.finplan.exception.BusinessException;
import app.finplan.exception.NotFoundException;
import app.finplan.validation.ValidPassword;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handleBusinessException(BusinessException ex) {
        var pd = ProblemDetail.forStatus(ex.getStatus());
        pd.setTitle("Business exception");
        pd.setDetail(ex.getMessage());
        pd.setProperty("code", ex.getCode());
        return ResponseEntity.status(ex.getStatus()).body(pd);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> notFound(NotFoundException ex){
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Resource not found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("code", "not_found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation error");
        pd.setProperty("code", "validation_error");

        var errors = ex.getBindingResult().getAllErrors().stream()
                .map(fe -> Map.of(
                        "field", ((FieldError) fe).getField(),
                        "message", fe.getDefaultMessage(),
                        "code", resolveErrorCode((FieldError) fe)
                )
                )
                .toList();

        pd.setProperty("errors", errors);

        return ResponseEntity.badRequest().body(pd);
    }

    private String resolveErrorCode(FieldError fe) {
        try {
            ConstraintViolation<?> violation = fe.unwrap(ConstraintViolation.class);
            var annotation = violation.getConstraintDescriptor().getAnnotation();

            if (annotation instanceof ValidPassword validPassword) {
                return validPassword.code();
            }
        } catch (Exception e) {}

        return "validation_error";
    }
}
