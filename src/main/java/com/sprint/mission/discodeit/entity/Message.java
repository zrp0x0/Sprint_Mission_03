package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
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

    protected Message(Message other) {
        super(other);
        this.content = other.content;
        this.userId = other.userId;
        this.channelId = other.channelId;
    }

    @Override
    public Message copy() {
        return new Message(this);
    }
}
