package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record UpdateChannelRequestDTO(
        UUID requestUserId,
        UUID channelId,
        String name,
        String description
) {

}
