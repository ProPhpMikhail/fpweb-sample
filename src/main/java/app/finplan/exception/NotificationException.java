package app.finplan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum NotificationException implements BusinessExCode {
    NOT_FOUND(
            "notification_not_found",
            "Notification not found",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus status;
}
