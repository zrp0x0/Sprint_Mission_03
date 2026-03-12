package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record JoinChannelRequestDTO(
        UUID requestUserId,
        UUID channelId
) {
}
