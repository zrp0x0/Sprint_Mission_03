package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class FileUserRepository extends FileRepository<User> implements UserRepository {

    protected FileUserRepository(@Value("${app.data.user-path}") String filePath) {
        super(filePath);
    }


}
