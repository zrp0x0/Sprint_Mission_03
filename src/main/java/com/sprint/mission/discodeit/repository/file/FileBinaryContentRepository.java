package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.base.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FileBinaryContentRepository extends FileRepository<BinaryContent> implements BinaryContentRepository {

    protected FileBinaryContentRepository(@Value("${app.data.binarycontent-path}") String filePath) {
        super(filePath);
        postLoad();
    }

    @Override
    protected void postLoad() {

    }
}
