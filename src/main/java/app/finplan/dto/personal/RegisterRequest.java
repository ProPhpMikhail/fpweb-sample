package app.finplan.dto.personal;

import app.finplan.validation.ValidPassword;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        String email,
        @Size(min = 8, max = 64)
        @ValidPassword
        String password
) {

}
