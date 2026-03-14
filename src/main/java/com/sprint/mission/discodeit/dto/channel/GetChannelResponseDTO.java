package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetChannelResponseDTO(
    UUID channelId,
    ChannelType type,
    String name,
    String description,
    Instant recentMessageTime,
    List<UUID> participantIds
) {
    public static GetChannelResponseDTO create(
            Channel channel,
            Instant recentMessageTime,
            List<UUID> participantIds
    ) {
        return new GetChannelResponseDTO(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                recentMessageTime,
                participantIds
        );
    }
}
