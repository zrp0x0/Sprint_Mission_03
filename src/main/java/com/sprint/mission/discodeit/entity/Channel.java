package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class Channel extends BaseEntity {

    private ChannelType type;
    private String name;
    private String description;
    private UUID masterUserId; // 방장

    public Channel(ChannelType type, String name, String description, UUID masterUserId) {
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

    public void updateInfo(String name, String description) {
        this.name = name;
        this.description = description;
        touch();
    }
}
