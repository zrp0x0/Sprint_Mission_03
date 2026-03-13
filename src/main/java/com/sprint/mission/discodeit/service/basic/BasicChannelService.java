package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.UserChannel;
import com.sprint.mission.discodeit.entity.UserChannelRole;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserChannelRepository userChannelRepository;

    // 채널 생성
    @Override
    public Channel createChannel(
            CreateChannelRequestDTO dto
    ) {
        // 채널 생성 - 로그인 여부는 판단하지 않음(혹은 더 앞 단에서 확인)
        Channel newChannel = Channel.create(dto.type(), dto.name(), dto.description(), dto.requestUserId());

        // 채널 저장
        Channel savedChannel = channelRepository.save(newChannel);

        // 방장 - 채널 관계 매핑
        UserChannel masterMapping = UserChannel.create(dto.requestUserId(), savedChannel.getId(), UserChannelRole.MASTER);
        userChannelRepository.save(masterMapping);

        return savedChannel;
    }

    // 채널 정보 수정
    @Override
    public Channel updateChannel(
            UpdateChannelRequestDTO dto
    ) {
        // 채널 존재 유무 확인
        Channel channel = getChannel(dto.channelId());

        // 채널 수정 권한 확인은 안에서
        channel.updateInfo(dto.name(), dto.description(), dto.requestUserId());

        // 수정된 채널 업데이트
        return channelRepository.save(channel);
    }

    // 채널 가입
    @Override
    public void joinChannel(
            JoinChannelRequestDTO dto
    ) {
        // 채널 유무 확인
        getChannel(dto.channelId());

        // 이미 가입된 상태인지 체크 - 좀만 더 고민
        Optional<UserChannel> result = userChannelRepository.findByUserIdAndChannelId(dto.requestUserId(), dto.channelId());
        if (result.isPresent()) {
            throw new RuntimeException("이미 가입된 채널입니다.");
        }

        // 유저 - 채널 관계 매핑
        UserChannel newMapping = UserChannel.create(dto.requestUserId(), dto.channelId(), UserChannelRole.NORMAL);
        userChannelRepository.save(newMapping);
    }

    // 채널 탈퇴
    @Override
    public void leaveChannel(
            LeaveChannelRequestDTO dto
    ) {
        // 관계가 있는지 확인 - 이거 바로 위에 거랑 비슷하지만 반대되되는 개념
        UserChannel mapping = userChannelRepository.findByUserIdAndChannelId(dto.requestUserId(), dto.channelId())
                .orElseThrow(() -> new RuntimeException("참여하지 않은 채널입니다."));

        // 채널 존재 유무 확인 - 근데 관계가 있다면 채널이 존재하는거라고 생각해도 되지 않을까? - 근데 방장 때문에 필요하구나
        Channel channel = getChannel(dto.channelId());

        // 방장은 채널을 함부로 나갈 수 없음 (그냥 이건 내가 정한 정책) - 단, 채널을 삭제하거나 방장을 위임하면 됨(위임은 구현 X)
        if (channel.isMaster(dto.requestUserId())) {
            throw new RuntimeException("방장은 채널을 나갈 수 없습니다. 채널을 삭제하거나 방장을 위임하세요.");
        }

        userChannelRepository.deleteById(mapping.getId());
    }

    // 채널 삭제
    @Override
    public void deleteChannel(
            DeleteChannelRequestDTO dto
    ) {
        // 채널 존재 유무 확인
        Channel channel = getChannel(dto.channelId());

        // 채널에 대한 권환 확인
        channel.verifyChannelUpdate(dto.requestUserId());

        // 유저 - 채널 관계 삭제
        List<UserChannel> list = userChannelRepository.findAllByChannelId(channel.getId()); // O(1)
        for (UserChannel uc : list) { // O(K)
            userChannelRepository.deleteById(uc.getId());
        }

        // 채널 삭제
        channelRepository.deleteById(channel.getId());
    }

    // 채널 전체 조회
    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    // 내 채널 목록 조회
    @Override
    public List<Channel> getUserChannels(UUID userId) {
        // 해당 유저가 포함된 유저채널 관계 가져오기
        List<UserChannel> allByUserId = userChannelRepository.findAllByUserId(userId); // O(1)

        // 가져온 유저채널 관계로 채널 가져오기
        List<Channel> myChannelList = new ArrayList<>();
        for (UserChannel uc : allByUserId) { // O(K)
            myChannelList.add(getChannel(uc.getChannelId()));
        }

        return myChannelList;
    }


    private Channel getChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("해당 채널을 찾을 수 없습니다."));
        return channel;
    }

}
