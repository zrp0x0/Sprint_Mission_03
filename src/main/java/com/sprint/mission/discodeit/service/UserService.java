package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.SignUpRequestDTO;
import com.sprint.mission.discodeit.entity.User;

public interface UserService {

    User signUp(
            SignUpRequestDTO dto
    );
}
