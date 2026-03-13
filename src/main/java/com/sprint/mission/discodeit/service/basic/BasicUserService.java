package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.LoginRequestDTO;
import com.sprint.mission.discodeit.dto.user.SignUpRequestDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User signUp(
            SignUpRequestDTO dto
    ) {
        // 이메일 중복 체크
        validateDuplicateEmail(dto.email());

        // 비밀번호 해싱 - 일단 과제에 없으므로 패스

        // 신규 User 객체 생성 및 리포지토리에 저장
        User newUser = User.create(dto.username(), dto.email(), dto.password());
        return userRepository.save(newUser);
    }

    @Override
    public User login(
            LoginRequestDTO dto
    ) {
        // 이메일 체크
        String targetEmail = dto.email();
        User targetUser = userRepository.findByEmail(targetEmail)
                .orElseThrow(() -> new RuntimeException("아이디 혹은 비밀번호가 틀렸습니다."));

        // 비밀번호 체크
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
