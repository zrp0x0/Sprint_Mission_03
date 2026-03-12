package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserChannel;
import com.sprint.mission.discodeit.repository.UserChannelRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileUserChannelRepository extends FileRepository<UserChannel> implements UserChannelRepository {

    protected FileUserChannelRepository(@Value("${app.data.userchannel-path}") String filePath) {
        super(filePath);
    }

    @Override
    public Optional<UserChannel> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        List<UserChannel> list = super.findAll();
        for (UserChannel uc : list) {
            if (uc.getUserId().equals(userId) && uc.getChannelId().equals(channelId)) {
                return findById(uc.getId());
            }
        }
        return Optional.empty();
    }
}
