package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record GetAllChannelResponseDTO(
    GetChannelResponseDTO channelResponseDTO
) {
    public static GetAllChannelResponseDTO create(
            GetChannelResponseDTO channelRespoonseDTO
    ) {
        return new GetAllChannelResponseDTO(
            channelRespoonseDTO
        );
    }
}
