package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record SendMessageRequestDTO(
        UUID channelId,
        UUID userId,
        String content
) {
}
