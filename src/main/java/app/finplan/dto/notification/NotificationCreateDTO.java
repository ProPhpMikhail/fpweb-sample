package app.finplan.dto.notification;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NotificationCreateDTO(
        LocalDateTime createdAt,
        String title,
        String text,
        String status
) {
}
