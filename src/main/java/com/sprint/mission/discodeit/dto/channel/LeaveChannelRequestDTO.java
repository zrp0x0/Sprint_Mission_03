package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record LeaveChannelRequestDTO(
        UUID requestUserId,
        UUID channelId
) {
}
