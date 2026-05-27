package app.finplan.dto.personal;

import app.finplan.validation.ValidPassword;

public record ResetPasswordRequest(
        @ValidPassword
        String password
) {}
