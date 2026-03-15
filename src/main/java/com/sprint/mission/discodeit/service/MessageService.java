package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.DeleteMessageRequestDTO;
import com.sprint.mission.discodeit.dto.message.SendMessageRequestDTO;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // create
    Message sendMessage(SendMessageRequestDTO dto);

    Message updateMessage(UpdateMessageRequestDTO dto);

    List<Message> getMessagesByChannel(UUID requestUserId, UUID channelId);

    void deleteMessage(
            DeleteMessageRequestDTO dto
    );
}
