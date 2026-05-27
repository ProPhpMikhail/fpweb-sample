package app.finplan.dto.personal;

import app.finplan.model.UserRole;

public record AuthResponse(
        String token,
        String email,
        String firstName,
        String lastName,
        String phone,
        UserRole role
) {

}
