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

    public UserStatus copy() {
        return new UserStatus(this);
    }

    public static UserStatus create(UUID userId, Instant lastOnlineTime) {
        return new UserStatus(userId, lastOnlineTime);
    }

    // 로직
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

// 사용자별 마지막으로 확인된 접속 시간
// 사용자의 온라인 상태를 확인하기 위한 용도
// 마지막 시간이 현재 시간으로부터 5분이내면 현재 접속 중인 유저로 간주
    // - 이건 로직으로 해결? 혹은 저 온라인 상태를 보여줘야하니간 계산 로직으로 보여줘야되나?