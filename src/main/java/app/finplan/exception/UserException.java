package app.finplan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserException implements BusinessExCode {
    EMAIL_EXISTS(
            "email_exists",
            "Email exists",
            HttpStatus.CONFLICT
    ),
    EMAIL_ALREADY_CONFIRMED(
            "email_already_confirmed",
            "Email already confirmed",
            HttpStatus.CONFLICT
    ),
    INVALID_CONFIRMATION_CODE(
            "invalid_confirmation_code",
            "Invalid confirmation code",
            HttpStatus.CONFLICT
    ),
    NOT_FOUND(
            "user_not_found",
            "User not found",
            HttpStatus.NOT_FOUND
    ),
    EMAIL_NOT_CONFIRMED(
            "email_not_confirmed",
            "Email not confirmed",
            HttpStatus.CONFLICT
    ),
    INVALID_CREDENTIALS(
            "invalid_credentials",
            "Invalid credentials",
            HttpStatus.UNAUTHORIZED
    );

    private final String code;
    private final String message;
    private final HttpStatus status;
}
