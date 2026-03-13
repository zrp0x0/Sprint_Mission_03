package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record DeleteMessageRequestDTO(
        UUID messageId,
        UUID requestUserId
) {
}
