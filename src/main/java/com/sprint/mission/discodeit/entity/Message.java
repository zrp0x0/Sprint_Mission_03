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

    private Message(String content, UUID channelId, UUID userId) {
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

    public static Message create(String content, UUID channelId, UUID userId) { // 입력 검증 로직 필요
        return new Message(content, channelId, userId);
    }

    public void updateContent(String newContent, UUID requestUserId) { // 입력 검증 로직 필요
        verifySender(requestUserId);
        this.content = newContent;
        touch();
    }

    public void verifySender(UUID requestUserId) {
        if (!this.userId.equals(requestUserId)) {
            throw new RuntimeException("메세지 작성자만 권한이 있습니다.");
        }
    }
}
