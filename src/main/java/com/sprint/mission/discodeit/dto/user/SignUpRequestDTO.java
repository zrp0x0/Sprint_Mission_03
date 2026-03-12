package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

@Builder
public record SignUpRequestDTO(
        String username,
        String email,
        String password
) {
    public User toUser() {
        return new User(
                this.username,
                this.email,
                this.password
        );
    }
}
