package app.finplan.dto.personal;

import app.finplan.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        String password,
        @ValidPassword
        String newPassword
) {
}
