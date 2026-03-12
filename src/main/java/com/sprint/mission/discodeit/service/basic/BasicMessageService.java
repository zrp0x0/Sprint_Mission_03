package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService {

    private final MessageRepository messageRepository;
}
