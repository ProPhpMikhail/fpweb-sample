package app.finplan.dto.personal;

import app.finplan.model.UserRole;

public record UserInfo(
        String email,
        String firstName,
        String lastName,
        String phone,
        UserRole role
) {
}
