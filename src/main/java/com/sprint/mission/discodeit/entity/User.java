package com.sprint.mission.discodeit.entity;

import lombok.Builder;
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
}
