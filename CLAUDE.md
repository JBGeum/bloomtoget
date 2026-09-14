# Bloomtoget Project Context

> **Purpose**: Quick reference for Claude Code to understand project structure, patterns, and preferences

**Last Updated**: 2026-09-14
**Architecture**: Hexagonal (Ports & Adapters) + Multi-Module
**Language**: Java 21 + Spring Boot 3.5.6
**Build**: Gradle with Convention Plugins
**Branching**: GitHub Flow (main + 이슈 브랜치). 전역 Git Flow 규칙(develop, release 병합)은 이 저장소에 적용하지 않는다.
**Response Language**: 한글 (Korean)

---

## 🎯 Critical Rules (Never Break)

1. **Dependency Direction**: `Infrastructure → Domain` (NEVER reverse)
2. **Module Isolation**: `domain` MUST NOT depend on `infrastructure`
3. **Abstraction Boundary**: All external systems accessed via Ports
4. **Compile-Time Enforcement**: Gradle dependencies enforce architecture

---

## 🏗️ Module Structure

```
bloomtoget/
├── domain/          - Business logic, Port interfaces (JPA·web·security 독립)
├── infrastructure/  - Adapters, Controllers, Repositories (depends on domain)
└── app/            - Assembly, Configuration, Main class
```

### Module Dependencies

```gradle
domain:          spring-boot-starter-validation + spring-tx
infrastructure:  domain + web + jpa + security + jwt + mapstruct
app:             domain + infrastructure
```

**Key Files**:
- `domain/port/in/*UseCase.java` - Inbound Ports (Use Cases)
- `domain/port/out/*Port.java` - Outbound Ports (External systems)
- `domain/service/*.java` - Business logic (implements Use Cases)
- `infrastructure/web/*Controller.java` - REST endpoints
- `infrastructure/persistence/*Adapter.java` - Port implementations
- `infrastructure/security/jwt/JwtTokenAdapter.java` - JWT wrapper

---

## 📁 Package Organization

### Domain Layer (`domain/src/main/java/com/btg/core/`)
```
application/
  port/
    in/              - *UseCase.java (interfaces)
    out/             - *Port.java (interfaces)
  service/           - *Service.java (implements Use Cases)
```

### Infrastructure Layer (`infrastructure/src/main/java/com/btg/infrastructure/`)
```
web/                 - *Controller.java, dto/request, dto/response
persistence/         - entity/, repository/, adapter/*PersistenceAdapter.java
security/            - jwt/*Adapter.java, config/
config/              - Bean configurations
```

### Test Organization (`app/src/test/`)
```
integration/         - MockMvc + @MockitoBean (fast)
e2e/                - @SpringBootTest(RANDOM_PORT) + real DB (slow, complete)
```

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

### Standard Patterns

**Use Case Interface** (in `domain/port/in/`):
```java
public interface SignupUseCase {
    UserResult signup(SignupCommand command);
    record SignupCommand(String email, String password, String name) {
        public void validate() { /* self-validation */ }
    }
    record UserResult(Long id, String email, String name, String createdAt) {}
}
```

**Service Implementation** (in `domain/service/`):
```java
@Service
@RequiredArgsConstructor
@Transactional
public class SignupService implements SignupUseCase {
    private final SaveUserPort saveUserPort;  // Outbound Ports only
    private final LoadUserPort loadUserPort;

    @Override
    public UserResult signup(SignupCommand command) {
        command.validate();
        // business logic
        return new UserResult(...);
    }
}
```

**Controller Pattern** (in `infrastructure/web/`):
```java
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final SignupUseCase signupUseCase;  // Use Case only

    @PostMapping("/auth/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        var command = new SignupUseCase.SignupCommand(...);  // DTO → Command
        var result = signupUseCase.signup(command);          // Execute
        return ResponseEntity.ok(new UserResponse(...));     // Result → DTO
    }
}
```

**Adapter Pattern** (in `infrastructure/persistence/` or `security/`):
```java
@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements SaveUserPort, LoadUserPort {
    private final UserJpaRepository repository;

    @Override
    public SaveUserPort.User save(String email, String password, String name) {
        var entity = new UserJpaEntity(email, password, name);
        var saved = repository.save(entity);
        return new SaveUserPort.User(...);  // Entity → Port record
    }
}
```

---

## 🧪 Testing Strategy

| Type | Scope | Tools | Mocks | Speed | When to Use |
|------|-------|-------|-------|-------|-------------|
| E2E | Full stack | TestRestTemplate, H2, @Transactional | None (real impl) | Slow | Critical flows |
| Integration | HTTP layer | MockMvc, @MockitoBean | Use Cases | Fast | Controller validation |
| Unit | Business logic | JUnit, Mockito | Ports | Very fast | Service logic |

**E2E Test Template** (`app/src/test/java/*/e2e/`):
```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
class AuthControllerE2ETest {
    @Autowired TestRestTemplate restTemplate;
    @Autowired UserJpaRepository userRepo;

    @Test
    void signup_Success() {
        var response = restTemplate.postForEntity("/auth/signup",
            new HttpEntity<>(json, headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(userRepo.findByEmail("test@example.com")).isPresent();
    }
}
```

**Integration Test Template** (`app/src/test/java/*/integration/`):
```java
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class AuthControllerIntegrationTest extends IntegrationTestBase {
    @Test
    void signup_Success() throws Exception {
        when(signupUseCase.signup(any())).thenReturn(mockResult);
        mockMvc.perform(post("/auth/signup").content(json))
            .andExpect(status().isCreated());
    }
}
```

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

**Fix**: All Ports belong in `domain/application/port/`

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
1. Define Use Case: `domain/port/in/auth/ResetPasswordUseCase.java`
2. Check/Create Outbound Ports: `domain/port/out/auth/SendEmailPort.java`
3. Implement Service: `domain/service/auth/ResetPasswordService.java`
4. Implement Adapters: `infrastructure/email/SmtpEmailAdapter.java`
5. Add Controller: `infrastructure/web/auth/AuthController.java`
6. Create DTOs: `infrastructure/web/auth/dto/request/ResetPasswordRequest.java`
7. Write E2E Test: `app/test/e2e/AuthControllerE2ETest.java`

---

## 🔍 Architecture Validation Checklist

When reviewing code, verify:
- [ ] No `import com.btg.infrastructure.*` in `domain/` module
- [ ] All Ports defined in `domain/application/port/`
- [ ] All Adapters in `infrastructure/`
- [ ] Services only depend on Port interfaces, never concrete classes
- [ ] Controllers only depend on Use Case interfaces
- [ ] Tests exist for new features (E2E + Unit preferred)

---

## 🎯 Key Patterns Summary

```
Flow: Request → Controller → Use Case → Service → Port → Adapter → External System

Dependency: Infrastructure → Domain (one-way)

Naming:
  - Use Cases: {Action}UseCase
  - Ports: {Verb}{Noun}Port
  - Adapters: {Tech}Adapter
  - Services: {Noun}Service

Testing:
  - E2E: Real server + real DB (@SpringBootTest RANDOM_PORT)
  - Integration: MockMvc + mocked Use Cases
  - Unit: Pure logic + mocked Ports
```

---

**Related Docs**: `C:\CCChat\hexagonal-architecture\e2e-integration-test-circular-dependency-resolution.md`
