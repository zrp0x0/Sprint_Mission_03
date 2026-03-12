package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UserChannel;
import com.sprint.mission.discodeit.entity.UserChannelRole;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService {

    private final ChannelRepository channelRepository;
    private final UserChannelRepository userChannelRepository;

    // 채널 생성
    public Channel createChannel(
            CreateChannelRequestDTO dto
    ) {
        // 채널 생성
        Channel newChannel = dto.toChannel();
        Channel savedChannel = channelRepository.save(newChannel);

        // 방장 - 채널 관계 매핑
        UserChannel masterMapping = new UserChannel(
                dto.requestUserId(),
                savedChannel.getId(),
                UserChannelRole.MASTER
        );
        userChannelRepository.save(masterMapping);

        return savedChannel;
    }

    // 채널 정보 수정
    public Channel updateChannel(
            UpdateChannelRequestDTO dto
    ) {
        Channel channel = channelRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다."));

        if (!channel.getMasterUserId().equals(dto.requestUserId())) {
            throw new RuntimeException("채널을 수정할 권한이 없습니다. (방장 전용)");
        }

        channel.updateInfo(dto.name(), dto.description());
        return channelRepository.save(channel);
    }

    // 채널 가입
    public void joinChannel(
        JoinChannelRequestDTO dto
    ) {
        // 채널 유무 확인
        channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 채널입니다."));

        // 이미 가입된 상태인지 체크
        Optional<UserChannel> result = userChannelRepository.findByUserIdAndChannelId(dto.requestUserId(), dto.channelId());
        if (result.isPresent()) {
            throw new RuntimeException("이미 가입된 채널입니다.");
        }

        // 유저 - 채널 관계 매핑
        UserChannel newMapping = new UserChannel(dto.requestUserId(), dto.channelId(), UserChannelRole.NORMAL);
        userChannelRepository.save(newMapping);
    }

    // 채널 탈퇴
    public void leaveChannel(
        LeaveChannelRequestDTO dto
    ) {
        // 관계가 있는지 확인
        UserChannel mapping = userChannelRepository.findByUserIdAndChannelId(dto.requestUserId(), dto.channelId())
                .orElseThrow(() -> new RuntimeException("참여하지 않은 채널입니다."));

        // 방장이라면 어떻게 할까?
        Channel channel = channelRepository.findById(dto.channelId())
                .orElseThrow(() -> new RuntimeException("채널이 없습니다."));

        if (channel.getMasterUserId().equals(dto.requestUserId())) {
            throw new RuntimeException("방장은 채널을 나갈 수 없습니다. 채널을 삭제하거나 방장을 위임하세요.");
        }

        userChannelRepository.deleteById(mapping.getId());
    }

    // 채널 삭제
    public void deleteChannel(
            DeleteChannelRequestDTO dto
    ) {
        Channel channel = channelRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다."));

        if (!channel.getMasterUserId().equals(dto.requestUserId())) {
            throw new RuntimeException("채널을 삭제할 권한이 없습니다. (방장 전용)");
        }

        // 유저 - 채널 관계 삭제
        List<UserChannel> list = userChannelRepository.findAll(); // 성능 최적화 고려해보기
        for (UserChannel uc : list) {
            if (uc.getChannelId().equals(channel.getId())) {
                userChannelRepository.deleteById(uc.getId());
            }
        }

        // 채널 삭제
        channelRepository.deleteById(channel.getId());
    }

    // 채널 전체 조회
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    // 내 채널 목록 조회
    public List<Channel> getUserChannels(UUID userId) {
        List<UserChannel> list = userChannelRepository.findAll();
        List<Channel> retList = new ArrayList<>();
        for (UserChannel uc : list) {
            if (uc.getUserId().equals(userId)) {
                Channel ch = channelRepository.findById(uc.getChannelId())
                                .orElseThrow(() -> new RuntimeException("해당 채널이 없습니다."));
                retList.add(ch);
            }
        }
        return retList;
    }

}
