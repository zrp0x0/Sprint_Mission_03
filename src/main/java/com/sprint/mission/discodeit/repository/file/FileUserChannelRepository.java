package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserChannel;
import com.sprint.mission.discodeit.repository.UserChannelRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserChannelRepository extends FileRepository<UserChannel> implements UserChannelRepository {

    protected FileUserChannelRepository(@Value("${app.data.userchannel-path}") String filePath) {
        super(filePath);
    }
}
