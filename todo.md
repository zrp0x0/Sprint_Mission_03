# Validation
    - 이거는 DTO에서 하는게 맞지 않나?
    - 내부 로직에서 해야하는가?

# ArrayList? ConcurrentHashMap?
    - Lock을 걸면 그냥 HashMap을 사용해도 되는거 아닌가? 
    - 내부 구조는 ArrayList이면 안되는건가?
    - 복사가 어디까지 전파되는지 (인덱스는 unmodified가 아니기때문에 위험한거 아닌가?)
    - 또한 동시성 문제가 현재 일어날 수 있는 부분인가?

# User 기능 추가
    - 가입한 채널 조회 
    - 작성한 댓글 조회
    - 유저 정보 보기
    - 유저 정보 수정

# 피드백 반영
    - 예외처리
    - 테스트

# 추가 요구사항
    

//
이후 테스트코드를 통해 로직의 성공 케이스뿐만 아니라 예외 발생 케이스(예: 존재하지 않는 유저 삭제 시도)를 자동화된 코드로 검증해야 합니다.

JUnit 5와 Mockito를 도입해 보세요.
java @Test void 존재하지_않는_유저조회_시_null_또는_예외반환_검증() { // Given: 저장된 유저가 없는 상태 // When: 임의의 UUID로 조회 시 User result = userService.read(UUID.randomUUID()); // Then: 기대한 결과(null 또는 메시지 출력)가 나오는지 확인 assertNull(result); }
BaseEntity를 abstract class로 선언하고 모든 도메인 모델이 이를 상속받도록 설계하신 점이 아주 훌륭합니다 👍

BaseEntity에 seraiVersionUID을 둔 점도 좋습니다.

신규 생성용, 파일 복원용함수를 뺀 점도 좋아요!

추가로 createdAt과 같은 날짜를 다룰때는 항상 프로젝트 규약을 확인하는 습관을 가지는게 좋습니다!
타임스탬프 단위: System.currentTimeMillis()는 밀리초(ms) 단위
보통 유닉스 타임스탬프라고 Instant.now().getEpochSecond()와 같이 하면 초(sec) 단위를 의미하는 경우가 많습니다.

updateTime()으로 수정 시간 갱신 로직을 공통화한 점이 좋습니다.
추후에는 직접 업데이트 시각을 업데이트하지 않고
JPA의 @LastModifiedDate 같은 annotation으로 자동 업데이트하는 방식이나,
DB 레벨에서 updated_at을 관리하는 방법도 고려해볼 수 있습니다!

ConcurrentHashMap을 쓰고 있지만, 그 내부의 Value는 일반 ArrayList입니다.
여러 스레드에서 동시에 접근할 때 ArrayList 내에서 ConcurrentModificationException이나 데이터 누락을 발생시킵니다!

synchronized로 동시성을 제어한것은 좋습니다!
하지만 “락은 잡는 시간이 짧을수록, 대상이 구체적일수록 고수의 코드입니다." 라는 점을 기억하시면 좋습니다.
락 관련해 리팩토링을 진행해보세요!

현재의 가장 큰 문제는 "읽기 요청(메시지 조회)"이 "쓰기 요청(메시지 저장)" 때문에 대기해야 한다는 점입니다.
사실 메시지를 읽는 사람들은 서로 기다릴 필요가 없습니다.
ReentrantReadWriteLock을 사용하면 "읽기는 여러 명이 동시에, 쓰기는 한 명만" 가능하게 할 수 있습니다.

private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
private final Lock readLock = rwLock.readLock();
private final Lock writeLock = rwLock.writeLock();

public List<Message> findAllByChannelId(UUID channelId) {
readLock.lock(); // 여러 스레드가 동시에 이 코드 구간을 통과할 수 있음
try {
// 조회 로직...
} finally {
readLock.unlock();
}
}

public Message save(Message message) {
writeLock.lock(); // 쓰기 시에는 다른 읽기/쓰기 스레드를 모두 차단
try {
// 저장 로직...
} finally {
writeLock.unlock();
}
}
또는 채널별 독립 잠금 (Striped Locking)을 할 수 있습니다.
채팅 서버에서 'A번 방'의 메시지 저장이 'B번 방'의 메시지 조회를 막을 이유가 있을까요? 전혀 없습니다. 전체 리포지토리에 락을 거는 대신, 채널 ID(UUID)별로 락을 쪼개는 것이 핵심입니다.
// 채널별로 별도의 락 객체를 관리 (예시)
private final Map<UUID, Object> locks = new ConcurrentHashMap<>();

public Message save(Message message) {
// 해당 채널에 대한 락만 획득함 (다른 채널 작업은 방해받지 않음)
Object lock = locks.computeIfAbsent(message.getChannelId(), k -> new Object());

    synchronized(lock) {
        // 저장 로직...
    }
}

또는 ArrayList 대신 쓰기 작업이 적고 읽기가 압도적으로 많은 환경이라면 CopyOnWriteArrayList를 고려해 보세요.
장점: 읽을 때 락이 아예 없습니다. 매우 빠릅니다.
단점: 쓸 때마다 리스트를 전체 복사하므로 리스트가 너무 길면 메모리 부하가 있습니다.
// 수정 전: ArrayList 사용 (동시성에 취약)
// channelIndex.computeIfAbsent(id, k -> new ArrayList<>())

// 수정 후: 락 없이 읽기가 가능한 컬렉션
channelIndex.computeIfAbsent(id, k -> new CopyOnWriteArrayList<>())

input에 대한 Validation이 부재합니다!

Channel 등 모든 엔티티에 값의 유효성 검증(예: 빈 이름, 글자 수 제한 등)이 없습니다.
잘못된 데이터가 파일에 한 번 저장되면 복구가 매우 힘든 구조라서 유의해주세요.

전체적으로 서비스 계층에서 모든 비즈니스 예외(가입 안 됨, 본인 아님 등)를 일반 RuntimeException으로 던지고 있습니다.

이는 API 응답을 처리하는 컨트롤러 계층에서 어떤 에러인지 구분하기 위해 에러 메시지 문자열을 파싱해야 하는 최악의 상황을 만들 수 있어요!

PermissionDeniedException, ResourceNotFoundException 같은 커스텀 예외 도입이 시급합니다!


FileMessageService가 Message 객체를 직접 생성하고 있습니다.
지금처럼 간단한 프로젝트에서는 서비스에서 만드셔도 큰 차이가 없습니다. 하지만 다음과 같은 상황이 온다면요?

복잡한 도메인 규칙이 생길 경우, 서비스 로직과 엔티티 생성 로직이 뒤섞여 유지보수가 어려워집니다.

메시지를 생성하는 서비스가 여러 개가 될 때 (예: 일반 메시지 전송 서비스, 시스템 자동 알림 서비스 등) -> 생성 로직이 여기저기 복사됩니다.
메시지 생성 규칙이 복잡해질 때 (예: 특정 채널 타입에 따라 기본값이 달라지는 경우 등) -> 서비스 코드가 읽기 힘들 정도로 길어집니다.
이런경우 Factory 패턴이나 엔티티 내부의 정적 팩토리 메서드 활용도 고려해보세요!

