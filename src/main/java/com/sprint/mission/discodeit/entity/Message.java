package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class Message extends BaseEntity {

    private String content;
    private UUID channelId;
    private UUID userId;

    public Message(String content, UUID channelId, UUID userId) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.userId = userId;
    }
}
