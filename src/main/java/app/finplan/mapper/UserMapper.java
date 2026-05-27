package app.finplan.mapper;

import app.finplan.dto.personal.AuthResponse;
import app.finplan.dto.personal.UserInfo;
import app.finplan.dto.personal.UserInfoCreate;
import app.finplan.model.User;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract UserInfo map(User model);

    public abstract AuthResponse auth(User model);
    public abstract void create(UserInfoCreate dto, @MappingTarget User model);
}
