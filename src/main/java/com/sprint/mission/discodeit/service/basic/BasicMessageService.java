package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.DeleteMessageRequestDTO;
import com.sprint.mission.discodeit.dto.message.SendMessageRequestDTO;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.UserChannel;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserChannelRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserChannelRepository userChannelRepository;

    public Message sendMessage(
            SendMessageRequestDTO dto
    ) {
        // 검증 로직
        // - 이 유저가 이 채널에 가입되어있는가?
        userJoinThisChannel(dto.userId(), dto.channelId());

        // 실행 로직
        Message newMessage = dto.toMessage();
        return messageRepository.save(newMessage);
    }

    public Message updateMessage(
            UpdateMessageRequestDTO dto
    ) {
        // 검증 로직
        // - 메세지 존재 여부
        Message message = getMessage(dto.messageId());

        // 실행 로직
        message.updateContent(dto.content(), dto.requestUserId());
        return messageRepository.save(message);
    }

    public void deleteMessage(
            DeleteMessageRequestDTO dto
    ) {
        // 검증 로직
        // - 메세지 존재 여부
        Message message = getMessage(dto.messageId());
        message.verifySender(dto.requestUserId());

        // 실행 로직
        messageRepository.deleteById(message.getId());
    }

    public List<Message> getMessagesByChannel(UUID requestUserId, UUID channelId) {
        // 검증 로직
        userJoinThisChannel(requestUserId, channelId);

        // 실행 로직
        return messageRepository.findAllByChannelId(channelId);
    }

    private UserChannel userJoinThisChannel(UUID userId, UUID channelId) {
        UserChannel uc = userChannelRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new RuntimeException("가입된 채널에만 메세지를 보낼 수 있습니다."));
        return uc;
    }

    private Message getMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("해당 메세지는 존재하지 않습니다."));
        return message;
    }
}
