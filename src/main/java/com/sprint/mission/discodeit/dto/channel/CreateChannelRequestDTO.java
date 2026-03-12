package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record CreateChannelRequestDTO(
        UUID requestUserId,
        ChannelType type,
        String name,
        String description
) {
    public Channel toChannel() {
        return new Channel(
                this.type,
                this.name,
                this.description,
                this.requestUserId
        );
    }
}
