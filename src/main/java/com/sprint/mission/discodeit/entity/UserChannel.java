package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class UserChannel extends BaseEntity { // 유저의 등록일 또는 나중에 id로 접근해야될 일 있을 경우를 대비

    private UUID userId;
    private UUID channelId;
    private UserChannelRole userChannelRole;

    public UserChannel(UUID userId, UUID channelId, UserChannelRole userChannelRole) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.userChannelRole = userChannelRole;
    }
}
