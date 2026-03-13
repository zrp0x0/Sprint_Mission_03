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
    private Set<UUID> userList; // 일단 PRIVATE 전용 // 생각해보니깐 Private라는게 과연 무엇일까 전체 채널 목록에 포함되면 안되는 것 같은데?

    private Channel(ChannelType type, String name, String description, UUID masterUserId, Set<UUID> userList) {
        super();
        this.type = type;
        this.name = name;
        this.description = description;
        this.masterUserId = masterUserId;
        this.userList = userList == null ? null : new HashSet<>(userList);
    }

    protected Channel(Channel other) {
        super(other);
        this.type = other.type;
        this.name = other.name;
        this.description = other.description;
        this.masterUserId = other.masterUserId;
        this.userList =  userList == null ? null : new HashSet<>(other.userList);
    }

    @Override
    public Channel copy() {
        return new Channel(this);
    }

    public static Channel create(ChannelType type, String name, String description, UUID masterUserId, Set<UUID> userList) {
        return new Channel(type, name, description, masterUserId, userList);
    }

    public void updateInfo(String name, String description, UUID requestUserId) {
        // 채널 수정 자격 검증
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
}
