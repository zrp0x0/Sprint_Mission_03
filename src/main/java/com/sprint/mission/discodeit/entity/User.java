package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true) // 부모 필드도 toString에 포함
public class User extends BaseEntity {

    private String username;
    private String email;
    private String password;

    // 추가
    private UUID profileId; // 프로필 이미지

    private User(String username, String email, String password, UUID profileId) {
        super();
        this.username = username;
        this.email = email;
        this.password = password; // BCrypt 해시 추후 적용
        this.profileId = profileId;
    }

    protected User(User other) {
        super(other);
        this.username = other.username;
        this.email = other.email;
        this.password = other.password;
        this.profileId = other.profileId;
    }

    @Override
    public User copy() {
        return new User(this);
    }

    public static User create(String username, String email, String password, UUID profileId) {
        // 검증 로직 여기서
        return new User(username, email, password, profileId);
    }

    //
    public void authenticate(String rawPassword) {
        if (!password.equals(rawPassword)) {
            throw new RuntimeException("인증 정보가 일치하지 않습니다.");
        }
    }
}
