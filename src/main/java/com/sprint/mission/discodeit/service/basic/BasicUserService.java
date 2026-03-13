package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.FindUserByIdResponseDTO;
import com.sprint.mission.discodeit.dto.user.SignUpRequestDTO;
import com.sprint.mission.discodeit.dto.user.UpdateUserProfileImageRequestDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;
    private final MessageRepository messageRepository;
    private final UserChannelRepository userChannelRepository;
    private final ReadStatusRepository readStatusRepository;
    // 여기가 문제인데, BinaryContentRepository를 부를 것이냐?

    // create
    @Override
    public User signUp(
            SignUpRequestDTO dto
    ) {
        // username & email 중복확인
        validateDuplicateEmailAndUsername(dto.email(), dto.username());

        UUID profileId = null;
        if (dto.profileImage() != null && dto.profileImage().fileName() != null && dto.profileImage().data() != null) {
            BinaryContent binaryContent = BinaryContent.create(
                    dto.profileImage().fileName(),
                    dto.profileImage().contentType(),
                    dto.profileImage().data()
            );
            binaryContentRepository.save(binaryContent);
            profileId = binaryContent.getId();
        }
        User newUser = dto.toUser(profileId);
        User savedUser = userRepository.save(newUser);

        UserStatus userStatus = UserStatus.create(newUser.getId());
        userStatusRepository.save(userStatus);

        return savedUser;
    }

    public FindUserByIdResponseDTO findUser(
        UUID userId
    ) {
        // 검증
        // - 해당 유저가 있는지 동시에 찾기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저는 없습니다."));

        // - 있다면 해당 유저 아이디로 UserStatus 검색
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new RuntimeException("해당 유저 상태 정보가 없습니다."));

        return FindUserByIdResponseDTO.from(user, userStatus);
    }

    public void updateUserProfile(
        UpdateUserProfileImageRequestDTO dto
    ) {
        // 검증
        // - 해당 유저가 있는지
        User user = userRepository.findById(dto.requestUserId()) // 고민 좀 해보자 이게 요청하는 사람의 requestUserId라는 의미로 써야되나? 본인이긴한데 (보안은 일단 배제하자)
                .orElseThrow(() -> new RuntimeException("해당 유저는 없습니다.")); // 이거 나중에 하나로 뺄 수 있지 않을까?

        // - 근데 본인인지 확인을 해야하는데? 이건 로그인에서 하는게 맞을 것 같다
        // 실행
        user.updateProfileId(dto.profileId());
        // - 여기서 고민점:
        // -- 프로필 사진 등록 로직을 애초에 따로 빼는게 맞지 않을까?
        // -- 위에서 등록을 위해서 프로필 사진 입력도 그렇고
        // -- 나중에 따로 프로필 사진을 만들어서 profileId로 넣어주는게 더 좋을 것 같다고 일단 판단
        // -- 나중에 updateUserProfile을 호출하는 Controller에서 프로필 사진 생성 후 id를 반환 받는 방식으로 구현하도록 주의하자
        // -- 그러면 회원가입도 그렇게 해야하는거 아닌가 싶지만! 일단은 패스
        userRepository.save(user);
    }

    public void deleteUser(
            UUID userId
    ) {
        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 유저는 없습니다."));

        // 프로필 사진 삭제
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }

        // 유저 Status도 삭제
        userStatusRepository.findByUserId(user.getId())
                .ifPresent(status -> userStatusRepository.deleteById(status.getId()));

        // 유저가 들어가있는 채널도 삭제
        List<UserChannel> userChannelList = userChannelRepository.findAllByUserId(userId);
        for (UserChannel uc : userChannelList) {
            userChannelRepository.deleteById(uc.getId());
        }
        // - 근데 여기는 정말 정말 힘든게 방장을 위임해야되긴하는데 이게 참 어렵네 일단 배제하고 구현해보자


        // 유저가 작성한 메세지 삭제 - 멘토님이 말씀하신 논리적 삭제 / 물리적 삭제 시간이 있으면 추가 고려 혹은 다음 미션 때 개선안으로 생각해보자
        // - 메세지 안에 들어가있는 사진도 삭제
        List<Message> userMessageList = messageRepository.findAllByUserId(userId);
        for (Message m : userMessageList) {
            if (m.getAttachmentIds() != null) {
                for (UUID attachmentId : m.getAttachmentIds()) {
                    binaryContentRepository.deleteById(attachmentId);
                }
            }
            messageRepository.deleteById(m.getId());
        }

        // ReadStatus 삭제
        List<ReadStatus> readStatusesList = readStatusRepository.findByUserId(userId);
        for (ReadStatus rs : readStatusesList) {
            readStatusRepository.deleteById(rs.getId());
        }

        userRepository.deleteById(userId);
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
