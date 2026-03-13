package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateChannelRequestDTO(
        @NotNull(message = "요청 사용자 ID는 필수입니다.")
        UUID requestUserId,

        @NotNull(message = "채널 타입(PUBLIC/PRIVATE)을 지정해주세요.")
        ChannelType type,

        @NotBlank(message = "채널 이름은 필수입니다.")
        @Size(max = 50, message = "채널 이름은 50자를 초과할 수 없습니다.")
        String name,

        @Size(max = 255, message = "채널 설명은 255자를 초과할 수 없습니다.")
        String description
) {
    public Channel toChannel() {
        return Channel.create(
                this.type,
                this.name,
                this.description,
                this.requestUserId
        );
    }
}