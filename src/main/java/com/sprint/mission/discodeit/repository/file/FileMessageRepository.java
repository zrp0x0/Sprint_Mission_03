package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository extends FileRepository<Message> implements MessageRepository {

    protected FileMessageRepository(@Value("${app.data.message-path}") String filePath) {
        super(filePath);
    }
}
