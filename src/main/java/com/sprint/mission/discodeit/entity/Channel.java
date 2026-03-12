package com.sprint.mission.discodeit.entity;

import lombok.Builder;
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
}
