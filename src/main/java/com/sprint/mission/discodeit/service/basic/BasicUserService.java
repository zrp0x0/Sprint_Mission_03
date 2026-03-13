package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.SignUpRequestDTO;
import com.sprint.mission.discodeit.dto.user.CreateUserProfileImageRequestDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    // 여기가 문제인데, BinaryContentRepository를 부를 것이냐?

    // create
    @Override
    public User signUp(
            SignUpRequestDTO dto
    ) {
        // username & email 중복확인
        validateDuplicateEmailAndUsername(dto.email(), dto.username());

        UUID profileId = null;
        if (dto.fileName() != null && dto.contentType() != null && dto.data() != null) {
            BinaryContent binaryContent = BinaryContent.create(dto.fileName(), dto.contentType(), dto.data());
            binaryContentRepository.save(binaryContent);
            profileId = binaryContent.getId();
        }
        User newUser = dto.toUser(profileId);

        UserStatus userStatus = UserStatus.create(newUser.getId());
        userStatusRepository.save(userStatus);

        return userRepository.save(newUser);
    }

    public void updateUserProfile(
        CreateUserProfileImageRequestDTO dto
    ) {
        // 검증
        // - 해당 유저가 있는지
        User user = userRepository.findById(dto.requestUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저는 없습니다."));

        // - 근데 본인인지 확인을 해야하는데? 이건 로그인에서 하는게 맞을 것 같다
        // 실행
        user.updateProfileId(dto.profileId());
    }

    private void validateDuplicateEmailAndUsername(String email, String username) {
        Optional<User> emailResult = userRepository.findByEmail(email);
        if (emailResult.isPresent()) {
            throw new RuntimeException("이메일 중복입니다.");
        }

        Optional<User> usernameResult = userRepository.findByUsername(username);
        if (usernameResult.isPresent()) {
            throw new RuntimeException("유저이름 중복입니다.");
        }
    }

}
