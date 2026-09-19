# Bloomtoget Project Context

> **Purpose**: Quick reference for Codex to understand project structure, patterns, and preferences

**Last Updated**: 2026-09-16
**Architecture**: Hexagonal (Ports & Adapters) + Multi-Module
**Language**: Java 21 + Spring Boot 3.5.6
**Build**: Gradle with Convention Plugins
**Database**: PostgreSQL 16 (docker compose) / H2 (test 프로필)
**Branching**: GitHub Flow (main + 이슈 브랜치). 전역 Git Flow 규칙(develop, release 병합)은 이 저장소에 적용하지 않는다.
**Response Language**: 한글 (Korean)

---

## 🎯 Critical Rules (Never Break)

1. **Dependency Direction**: `Infrastructure → Domain` (NEVER reverse)
2. **Module Isolation**: `domain` MUST NOT depend on `infrastructure`
3. **Abstraction Boundary**: All external systems accessed via Ports
4. **Compile-Time Enforcement**: Gradle dependencies enforce architecture

---

## ⚙️ 빌드 · 실행 · 테스트

```bash
# 로컬 실행 (PostgreSQL 필요)
docker compose up -d
cp app/src/main/resources/application-local.yml.example app/src/main/resources/application-local.yml
./gradlew :app:bootRun

# 테스트 (H2 인메모리, Docker 불필요)
./gradlew test
./gradlew :domain:test
./gradlew :app:test --tests '*AuthControllerE2ETest'
./gradlew :app:test --tests '*AuthControllerE2ETest.signup_Success_WithDatabasePersistence'

# CI와 동일한 검증
./gradlew build
```

`application-local.yml`과 `application-prod.yml`은 `.gitignore`의 `**/application-*.yml`에 걸려 추적되지 않는다. 클론 직후에는 `.example`에서 복사해야 실행된다.

Gradle 실행에는 JDK 17 이상이면 되고, 컴파일용 JDK 21은 toolchain이 내려받는다.

API 문서: http://localhost:8080/swagger-ui.html

---

## 🧭 작업 안내 방식

- 작업할 때 무엇을 왜 하는지 짧게 설명한다. 다른 방식과 무엇이 다른지를 이 저장소의 실제 상황을 예로 든다.
- 다음 할 일은 한 번에 한 단계만 제안하고, 확인을 받은 뒤 진행한다. 여러 단계를 한꺼번에 실행하거나 나열하지 않는다.
- 이슈 하나 분량의 작업이 끝나면 PR을 올릴 때라고 먼저 알리고, 학습 로그 항목을 쓸지 묻는다. 쓰면 같은 PR에 넣는다. 이슈 1개 = 브랜치 1개 = PR 1개.

---

## 📚 저장소의 기록물

| 경로 | 무엇 | 누가 쓰나 |
|---|---|---|
| `docs/ROADMAP.md` | 단계별 진행 계획과 작업 우선순위 | 다음 작업을 고를 때 여기부터 본다 |
| `docs/learning-log.md` | 이슈별 학습 기록 | **사용자만 쓴다. Codex는 Read만 한다** |
| `docs/openapi/` | API 명세 | |

`.codex/hooks/protect-learning-records.js`가 PreToolUse에서 `docs/learning-log.md`와 `docs/adr/`에 대한 Write·Edit·Bash를 exit 2로 차단한다. 차단되면 훅이 의도대로 동작한 것이므로 우회하지 않는다.

`.codex/agents/interviewer.toml`은 변경분을 읽고 기술 면접 질문만 돌려주는 서브에이전트이고, `.agents/skills/session-review/`는 세션 복습 노트를 저장소 밖 학습 자료 저장소에 쓰는 skill이다.

CI는 `.github/workflows/build.yml` 하나이며 main push와 모든 PR에서 `./gradlew build`를 돌린다.

---

## 🎨 Coding Patterns & Preferences

### When to Create Ports

**CREATE Port when**:
- Accessing external systems (DB, API, file system)
- Using technology frameworks (JWT, encryption)
- Need to mock in tests

**DON'T CREATE Port for**:
- Java stdlib (Collections, Stream, LocalDateTime)
- Simple utilities (string manipulation)
- Pure business calculations

### Naming Conventions

```
Inbound Ports:    {Action}UseCase          (SignupUseCase, LoginUseCase)
Outbound Ports:   {Verb}{Noun}Port         (SaveUserPort, GenerateTokenPort)
Adapters:         {Technology}Adapter      (JwtTokenAdapter, UserPersistenceAdapter)
Services:         {Noun}Service            (SignupService, LoginService)
Controllers:      {Noun}Controller         (AuthController, UserController)
Entities:         {Noun}JpaEntity          (UserJpaEntity, RefreshTokenJpaEntity)
DTOs:             {Noun}Request/Response   (SignupRequest, UserResponse)
```

### Code Organization Philosophy

**From Developer Preferences:**
1. **Inline over Helper Methods**: Prefer inline for single-use logic (<40 lines)
2. **Avoid Premature Abstraction**: Extract methods only when reused 2+ times
3. **Simplification over Patterns**: Question necessity of each abstraction layer
4. **Code Clarity**: Optimize for reading/debugging, not theoretical reusability

---

## 🧪 Testing Strategy

| Type | Scope | Tools | Mocks | Speed | When to Use |
|------|-------|-------|-------|-------|-------------|
| E2E | Full stack | TestRestTemplate, H2 | 대상 외 Use Case만 | Slow | Critical flows |
| Integration | HTTP layer | MockMvc, @MockitoBean | 모든 Use Case | Fast | Controller validation |
| Unit | Business logic | JUnit, Mockito | Ports | Very fast | Service logic |

`@DisplayName`은 한글로 쓴다. 메서드명은 자바 식별자이므로 영어를 유지한다.

E2E는 검증 대상 Controller의 Use Case만 실제 구현으로 두고, 나머지 Use Case는 컨텍스트 로딩을 위해 Mock으로 채운다. 기존 E2E는 `@MockBean`, Integration은 `@MockitoBean`을 쓴다.

---

## ⚠️ Common Mistakes (Never Do This)

### ❌ Domain → Infrastructure Dependency
```java
// domain/service/LoginService.java
import com.btg.infrastructure.security.jwt.JwtTokenProvider;  // WRONG!
private final JwtTokenProvider provider;
```

**Fix**: Create Port in `domain/port/out/`, implement in `infrastructure/`

### ❌ Port in Wrong Module
```
infrastructure/port/out/TokenPort.java  // WRONG!
```

**Fix**: All Ports belong in the `domain` module, under `com.btg.core.application.port`

### ❌ Direct Technology Usage in Domain
```java
// domain/service/
private final RestTemplate restTemplate;  // WRONG!
private final RedisTemplate redis;        // WRONG!
```

**Fix**: Wrap technology behind Port interface

---

## 📋 Quick Reference: File Creation Workflow

**Adding New Feature** (e.g., "Password Reset"):
1. Define Use Case: `domain/src/main/java/com/btg/core/application/port/in/auth/ResetPasswordUseCase.java`
2. Check/Create Outbound Ports: `domain/src/main/java/com/btg/core/application/port/out/auth/SendEmailPort.java`
3. Implement Service: `domain/src/main/java/com/btg/core/application/service/auth/ResetPasswordService.java`
4. Implement Adapters: `infrastructure/src/main/java/com/btg/infrastructure/email/SmtpEmailAdapter.java`
5. Add Controller: `infrastructure/src/main/java/com/btg/infrastructure/web/auth/AuthController.java`
6. Create DTOs: `infrastructure/src/main/java/com/btg/infrastructure/web/auth/dto/request/ResetPasswordRequest.java`
7. Write E2E Test: `app/src/test/java/com/btg/e2e/AuthControllerE2ETest.java`

---

## 🔍 Architecture Validation Checklist

When reviewing code, verify:
- [ ] No `import com.btg.infrastructure.*` in `domain/` module
- [ ] All Ports defined in the `domain` module, under `com.btg.core.application.port`
- [ ] All Adapters in `infrastructure/`
- [ ] Services only depend on Port interfaces, never concrete classes
- [ ] Controllers only depend on Use Case interfaces
- [ ] Tests exist for new features (E2E + Unit preferred)

