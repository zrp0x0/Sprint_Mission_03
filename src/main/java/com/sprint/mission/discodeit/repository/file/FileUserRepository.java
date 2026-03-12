package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileUserRepository extends FileRepository<User> implements UserRepository {

    private final Map<String, UUID> emailIndex = new ConcurrentHashMap<>();

    protected FileUserRepository(@Value("${app.data.user-path}") String filePath) {
        super(filePath);
        postLoad(); // 자식 클래스의 인덱스 생성
    }

    @Override
    protected void postLoad() {
        for (User user : dataMap.values()) {
            emailIndex.put(user.getEmail(), user.getId());
        }
    }

    @Override
    protected void postSave(User entity) {
        emailIndex.put(entity.getEmail(), entity.getId());
    }

    @Override
    protected void postDelete(User entity) {
        emailIndex.remove(entity.getEmail());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        readLock.lock();
        try {
            UUID targetId = emailIndex.get(email);
            if (targetId == null) {
                return Optional.empty();
            }
            return super.findById(targetId);
        } finally {
            readLock.unlock();
        }
    }
}
