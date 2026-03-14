package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    // 채널 생성 - Public
    Channel createPublicChannel(
            CreatePublicChannelRequestDTO dto
    );

    // 채널 생성 - Private
    Channel createPrivateChannel(
            CreatePrivateChannelRequestDTO dto
    );

    // 채널 정보 수정
    Channel updateChannel(
            UpdateChannelRequestDTO dto
    );

    // 채널 가입
    void joinChannel(
            JoinChannelRequestDTO dto
    );

    // 채널 탈퇴
    void leaveChannel(
            LeaveChannelRequestDTO dto
    );

    // 채널 삭제
    void deleteChannel(
            DeleteChannelRequestDTO dto
    );

    // 특정 채널 조회 (find)
    GetChannelResponseDTO getChannels(
            UUID channelId
    );

    // 채널 전체 조회 (findAll)
    List<GetChannelResponseDTO> findAllByUserId(UUID userId);

    // 내 채널 목록 조회
    List<Channel> getUserChannels(UUID userId);
}
