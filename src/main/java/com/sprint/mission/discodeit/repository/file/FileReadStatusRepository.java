package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class FileReadStatusRepository extends FileRepository<ReadStatus> implements ReadStatusRepository {

    protected FileReadStatusRepository(@Value("${app.data.readstatus-path}")String filePath) {
        super(filePath);
        postLoad();
    }

    @Override
    protected void postLoad() {

    }

}
