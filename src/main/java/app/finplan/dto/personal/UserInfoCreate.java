package app.finplan.dto.personal;

import app.finplan.validation.PhoneRu;

public record UserInfoCreate(
        String firstName,
        String lastName,

        @PhoneRu
        String phone
) {
}
