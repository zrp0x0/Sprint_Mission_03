package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class UserStatus extends BaseEntity {

    private UUID userId;
    private Instant lastOnlineTime;

    private UserStatus(UUID userId, Instant lastOnlineTime) {
        super();
        this.userId = userId;
        this.lastOnlineTime = lastOnlineTime;
    }

    private UserStatus(UserStatus other) {
        super(other);
        this.userId = other.userId;
        this.lastOnlineTime = other.lastOnlineTime;
    }

    @Override
    public UserStatus copy() {
        return new UserStatus(this);
    }

    public static UserStatus create(UUID userId) {
        return new UserStatus(userId, Instant.now());
    }

    // 이하 로직
    public boolean isOnline() {
        if (lastOnlineTime == null) return false;
        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
        return lastOnlineTime.isAfter(fiveMinutesAgo);
    }

    public void updateLastOnlineTime() {
        this.lastOnlineTime = Instant.now();
        touch();
    }

}