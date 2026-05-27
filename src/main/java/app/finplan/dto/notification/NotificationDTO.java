package app.finplan.dto.notification;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NotificationDTO(
        Long id,
        LocalDateTime createdAt,
        @NotNull
        Long userId,
        String title,
        String text,
        String status
) {
}
