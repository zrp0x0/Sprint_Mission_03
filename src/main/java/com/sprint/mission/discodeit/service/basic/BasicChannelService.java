package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService {

    private final ChannelRepository channelRepository;
}
