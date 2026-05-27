package app.finplan.dto.notification;

public record NotificationUpdateDTO(
        String text,
        String title,
        String status
) {
}
