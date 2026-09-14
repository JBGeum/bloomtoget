# bloomtoget 로드맵

2026-09 시작, 주 5~10시간 기준 약 18주에 버퍼를 더한다. 목표는 세 가지다.

- 핵심 기능(일일 달성 체크)이 동작하고 테스트로 증명된다.
- 수치로 설명할 수 있는 기술 결정이 2건 있다.
- 클론한 누구나 문서대로 빌드하고 실행할 수 있다.

## 원칙

1. main의 테스트는 항상 녹색이다. CI가 실패하면 다른 작업보다 먼저 고친다.
2. README에는 코드로 검증되는 것만 적는다. 계획은 이 문서에 둔다.
3. 공부는 재현 → 측정 → 결정 → 기록 순서로 한다.
4. "왜 이렇게 했나"의 답이 판단인 작업은 직접 한다. 답이 코드인 작업은 Claude에 위임할 수 있다.
5. 리뷰 지적은 이슈로 등록하고 PR로 닫는다.

## 작업 방식

- GitHub Flow: 이슈 → 이슈 브랜치 → PR → main. 단계는 마일스톤, 항목은 이슈로 만든다.
- 커밋은 Conventional Commits(`feat:`, `fix:`, `refactor:`, `test:`, `docs:`, `chore:`)를 쓰고 리팩터링과 기능 커밋을 나눈다.
- 기록 위치: 진행 상황은 이슈, 배운 것은 `docs/learning-log.md`, 공개할 결정은 `docs/adr/`, 요약은 README.

## 단계

### 0. 신뢰 복구 (2주)

| 항목 | 담당 |
|---|---|
| `.gitignore` 정리(wrapper jar, 테스트 설정 추적), JWT 시크릿 환경변수화 | 위임 |
| Java 21 toolchain 전환, CI(build + test) | 위임 |
| README를 코드 기준 사실로 교정 | 위임 후 검토 |
| 컨트롤러 테스트 37건의 401 실패 해소(테스트에서 인증을 다루는 방식 결정) | 직접 |
| 멘토 리뷰 미해결 2건 이슈 등록: [f-lab-edu#4](https://github.com/f-lab-edu/bloomtoget/pull/4) 응답에 password가 없음을 검증, [f-lab-edu#6](https://github.com/f-lab-edu/bloomtoget/pull/6) 그룹 검색 쿼리 수 | 직접 |

완료 기준: 클린 클론에서 `./gradlew build`가 통과하고 CI가 녹색이다.

### 1. 핵심 기능 완성 + 결함 수정 (4주)

| 항목 | 담당 |
|---|---|
| 일일 달성 체크 유스케이스 3개 구현(하루 1회 규칙은 unique 제약으로) | 직접 |
| 도메인 예외 계층과 HTTP 상태 매핑(현재 `IllegalStateException`이 401로 나감) | 직접 |
| refresh 토큰이 access 토큰으로 통과하는 결함: 재현 테스트 → 수정 | 직접 |
| 로그아웃 후 access 토큰 처리 방침 결정 | 직접 |
| Flyway 도입, `V1__init.sql` | 위임 후 검토 |

완료 기준: 완료율이 0이 아닌 값을 반환하는 E2E 테스트 1개, `docker compose up` 후 문서대로 앱이 기동한다.

### 2. 도메인 모델 (5주)

| 항목 | 담당 |
|---|---|
| Task: `TaskStatus` enum, 상태 전이 메서드, 불변식(기간, 정원) | 직접 |
| Group: `GroupRole` enum, 정원 규칙, 가입 가능 판단 | 직접 |
| 포트별 중복 record를 도메인 모델로 통합, 어댑터 매핑 | 위임 |
| ArchUnit 규칙: domain은 JPA·웹·보안·infrastructure에 의존하지 않는다 | 위임 |

완료 기준: 전이·정원 규칙이 Spring·DB 없는 단위 테스트로 검증되고 `"ADMIN".equals` 비교가 사라진다.

### 3. 실험 A — 그룹 정원 경합 (4주)

| 항목 | 담당 |
|---|---|
| Testcontainers(PostgreSQL 16) 테스트 환경 | 위임 |
| 실패 재현: 잔여 1자리에 20~100개 스레드 동시 가입 | 직접 |
| 세 방식 구현: 낙관적 락(`@Version` + 재시도), 비관적 락(`SELECT ... FOR UPDATE`), 원자적 조건부 쿼리 | 직접 |
| 측정: 정원 초과 여부, 평균·최대 지연, 처리량, 재시도·실패율, 데드락 | 측정 코드 위임, 해석 직접 |
| `docs/adr/ADR-001-group-capacity-concurrency.md` | 직접 |

완료 기준: 실패 재현 → 세 방식 결과 → 선택 근거가 ADR 하나로 읽힌다. 채택한 방식만 main에 남긴다.

### 4. 실험 B — 태스크 조회 성능 (3~4주)

| 항목 | 담당 |
|---|---|
| k6 부하 스크립트 골격 | 위임 |
| `GetTaskService` 비교: 현재 병렬 조회(CompletableFuture) / 트랜잭션 안 순차 조회 / 집계 쿼리. 동시 요청 1·5·10·20에서 p50·p99, 타임아웃율, 커넥션 대기 | 직접 |
| 태스크 목록 N+1 제거, 쿼리 수 전후 비교(`generate_statistics`) | 직접 |
| `docs/adr/ADR-002` | 직접 |

완료 기준: 수치 표 2개와 선택 근거가 ADR로 남는다.

## 선행 학습

| 단계 | 주제 |
|---|---|
| 0 | Spring Security 테스트 지원, 필터 체인과 MockMvc |
| 1 | JWT 토큰 수명주기와 폐기 전략, Flyway 버전 관리 |
| 2 | 빈약한 모델과 행위 중심 모델, 애그리거트 경계 — 『만들면서 배우는 클린 아키텍처』, 『도메인 주도 설계 핵심』 애그리거트 장 |
| 3 | 트랜잭션 격리 수준, PostgreSQL MVCC, 락 |
| 4 | HikariCP 커넥션 풀, `@Transactional`과 스레드 경계, N+1과 fetch 전략 |

## 설계 결정

- domain은 JPA·웹·보안 기술에 의존하지 않는다. Spring은 `@Service`와 `@Transactional`만 허용한다. 유스케이스가 트랜잭션 경계이기 때문이다.
- 행위 중심 모델은 규칙이 있는 도메인(Task, Group)에만 적용한다. User·Auth는 지금 구조를 유지한다.

## 제외

경험치·레벨, 프로필 이미지 업로드, 캘린더 달성률과 QueryDSL, 커스텀 `@UseCase`로 `@Service` 대체, 알림, 게시판, Redis 캐싱, 배포(CD).
4단계를 마치고 시간이 남으면 이 목록에서 하나를 골라 같은 형식(담당, 완료 기준)으로 추가한다.
