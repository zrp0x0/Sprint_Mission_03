package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record UpdateChannelRequestDTO(
        UUID requestUserId,
        UUID id,
        String name,
        String description
) {

}
