package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserChannel;
import com.sprint.mission.discodeit.repository.UserChannelRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserChannelRepository extends FileRepository<UserChannel> implements UserChannelRepository {

    protected FileUserChannelRepository(@Value("${app.data.userchannel-path}") String filePath) {
        super(filePath);
        postLoad();
    }

    private Map<UUID, List<UUID>> userToChannelsIndex = new HashMap<>();
    private Map<UUID, List<UUID>> channelToUsersIndex = new HashMap<>();

    @Override
    protected void postLoad() {
        userToChannelsIndex.clear();
        channelToUsersIndex.clear();
        for (UserChannel uc : dataMap.values()) {
            addToIndex(uc);
        }
    }

    @Override
    protected void postSave(UserChannel entity) {
        addToIndex(entity);
    }

    @Override
    protected void postDelete(UserChannel entity) {
        removeFromIndex();
    }

    private void addToIndex(UserChannel uc) {
        userToChannelsIndex.computeIfAbsent(uc.getUserId(), k -> new ArrayList<>()).add(uc.getId());
        channelToUsersIndex.computeIfAbsent(uc.getChannelId(), k -> new ArrayList<>()).add(uc.getId());
    }

    private void removeFromIndex() {
        
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
