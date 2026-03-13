package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class Message extends BaseEntity {

    private String content;
    private UUID channelId;
    private UUID userId;

    // 추가
    private List<UUID> attachmentIds; // 깊은 복사

    private Message(String content, UUID channelId, UUID userId, List<UUID> attachmentIds) {
        super();
        this.content = content;
        this.channelId = channelId;
        this.userId = userId;
        this.attachmentIds = new ArrayList<>(attachmentIds);
    }

    protected Message(Message other) {
        super(other);
        this.content = other.content;
        this.userId = other.userId;
        this.channelId = other.channelId;
        this.attachmentIds = new ArrayList<>(other.attachmentIds);
    }

    @Override
    public Message copy() {
        return new Message(this);
    }

    public static Message create(String content, UUID channelId, UUID userId, List<UUID> attachmentIds) { // 입력 검증 로직 필요
        return new Message(content, channelId, userId, attachmentIds);
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
