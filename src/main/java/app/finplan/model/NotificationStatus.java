package app.finplan.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum NotificationStatus {
    NEW,
    READ,
    REMOVED;


    @JsonCreator
    public static NotificationStatus from(String value) {
        return NotificationStatus.valueOf(value.toUpperCase());
    }
}
