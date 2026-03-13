package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User login(
            LoginRequestDTO dto
    ) {
        String targetEmail = dto.email();
        User targetUser = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("아이디 혹은 비밀번호가 틀렸습니다."));

        targetUser.authenticate(dto.password());
        return targetUser;
    }

    private void validateDuplicateEmail(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isPresent()) {
            throw new RuntimeException("이메일 중복입니다.");
        }
    }
}
