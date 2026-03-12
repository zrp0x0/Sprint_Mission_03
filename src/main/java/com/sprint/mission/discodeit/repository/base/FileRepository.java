package com.sprint.mission.discodeit.repository.base;

import com.sprint.mission.discodeit.entity.base.BaseEntity;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/*
예외 처리 로직 아직 안함
 */
public abstract class FileRepository<T extends BaseEntity> {

    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();

    protected final String filePath;

    protected Map<UUID, T> dataMap = new ConcurrentHashMap<>();
    // Q: 락을 걸어주면 HashMap을 사용해도 Thread-Safe 한 것 아닌가?

    protected FileRepository(String filePath) {
        this.filePath = filePath;
        load(); // 생성 시 파일에서 읽어오기
    }

    private void load() {
        File file = new File(filePath);

        // 현재 파일이 없는 경우
        if (!file.exists()) {
            return;
        }

        try(
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis);
        ) {
            Object object = ois.readObject();
            if (object instanceof Map) {
                this.dataMap = new ConcurrentHashMap<>((Map<UUID, T>)object);
            }
        } catch (Exception e) {

        }
    }

    private void saveToFile() {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(dataMap);
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("unchecked")
    public T save(T entity) {
        writeLock.lock();
        try {
            T copyEntity = (T) entity.copy(); // 원본 객체 훼손 위험성이 존재함!!
            dataMap.put(copyEntity.getId(), copyEntity);
            saveToFile();
            return copyEntity;
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public Optional<T> findById(UUID id) {
        readLock.lock();
        try {
            T entity = dataMap.get(id);
            return entity != null ? Optional.of((T) entity.copy()) : Optional.empty();
        } finally {
            readLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        readLock.lock();
        try {
            List<T> list = new ArrayList<>();
            for (T entity : dataMap.values()) {
                T copied = (T) entity.copy();
                list.add(copied);
            }
            return Collections.unmodifiableList(list);
        } finally {
            readLock.unlock();
        }
    }

    public void deleteById(UUID id) {
        writeLock.lock();
        try {
            dataMap.remove(id);
            saveToFile();
        } finally {
            writeLock.unlock();
        }
    }
}
