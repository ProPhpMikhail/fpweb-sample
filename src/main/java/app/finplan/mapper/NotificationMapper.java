package app.finplan.mapper;

import app.finplan.dto.notification.*;
import app.finplan.model.Notification;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class NotificationMapper {
    public abstract Notification map(NotificationCreateDTO dto);

    @Mapping(target = "userId", source = "model.user.id")
    public abstract NotificationDTO map(Notification model);
    public abstract void update(NotificationUpdateDTO dto, @MappingTarget Notification model);
    public abstract void create(NotificationCreateDTO dto, @MappingTarget Notification model);
}
