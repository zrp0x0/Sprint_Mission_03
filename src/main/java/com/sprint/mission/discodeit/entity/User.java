package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true) // 부모 필드도 toString에 포함
public class User extends BaseEntity {

    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password; // BCrypt 해시 추후 적용
    }

    protected User(User other) {
        super(other);
        this.username = other.username;
        this.email = other.email;
        this.password = other.password;
    }

    @Override
    public User copy() {
        return new User(this);
    }
}
