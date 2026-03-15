package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@Getter
@ToString(callSuper = true)
public class Channel extends BaseEntity {

    private ChannelType type;
    private String name;
    private String description;
    private UUID masterUserId; // 방장

    private Channel(ChannelType type, String name, String description, UUID masterUserId) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
        this.masterUserId = masterUserId;
    }

    protected Channel(Channel other) {
        super(other);
        this.type = other.type;
        this.name = other.name;
        this.description = other.description;
        this.masterUserId = other.masterUserId;
    }

    @Override
    public Channel copy() {
        return new Channel(this);
    }

    public static Channel create(ChannelType type, String name, String description, UUID masterUserId) {
        return new Channel(type, name, description, masterUserId);
    }

    // 이하 로직
    public void updateInfo(String name, String description, UUID requestUserId) {
        verifyChannelUpdate(requestUserId);
        this.name = name;
        this.description = description;
        touch();
    }

    public void verifyChannelUpdate(UUID requestUserId) {
        if (!this.masterUserId.equals(requestUserId)) {
            throw new RuntimeException("이 권한은 방장한테만 있습니다.");
        }
    }

    public boolean isMaster(UUID userId) {
        return this.masterUserId.equals(userId);
    }

    public boolean isPrivate() {
        return this.type == ChannelType.PRIVATE;
    }
}
