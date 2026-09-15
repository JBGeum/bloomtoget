# 학습 로그

직접 겪고 판단한 것만 쓴다. 항목 하나는 30줄을 넘기지 않는다. 공개할 결정은 다듬어서 `docs/adr/`로 옮긴다.

```text
## [#이슈] 제목 (YYYY-MM-DD)
- 문제: 증상 (재현 명령 또는 테스트 이름)
- 가설:
- 원인: 가설을 가른 증거와 확인된 원인
- 결정과 근거:
- 버린 대안과 이유:
- 한 일: (커밋)
- 결과: (수치 또는 전후 비교)
- 막힌 곳 / 다음에 볼 것: 
```

## [#11] 컨트롤러 테스트 37건의 401 실패 해소 (2026-09-15)

- 문제: 컨트롤러 테스트 54건 중 37건 실패 (`./gradlew :app:test`)
  - 실패 메시지는 전부 `expected 200/201/204/400 but was 401`이며 400(입력 검증 실패)을 기대한 테스트도 401로 확인.
  - main CI는 PR #2를 병합한 2026-09-14부터 계속 실패하고 있었음.
- 가설:
  - A. 보안 필터 체인에서 거절: 토큰이 없으면 `JwtAuthenticationEntryPoint`가 "인증이 필요합니다"로 401을 응답.
  - B. 컨트롤러에서 거절: `SecurityContextUtil.getCurrentUserId()`가 `IllegalStateException`을 던지면 `GlobalExceptionHandler`가 "인증되지 않은 사용자입니다"로 401을 응답.
- 원인:
  - 실패한 통합 테스트 27건의 응답이 모두 "인증이 필요합니다"였고, "인증되지 않은 사용자입니다"는 0건이고, 통과한 17건은 모두 인증이 필요 없는 경로. 가설 A로 판정.
  - 테스트는 모든 요청을 허용하던 시점(`1d8d619`)에 작성됐고, JWT 인증 도입(`fc6b2b4`) 때 갱신되지 않았다. 또, "보안 비활성화" 주석과 `spring.security.enabled: false`는 효과 없이 남아 문제를 가렸다.
- 결정과 근거: 통합 테스트와 E2E 모두 실제 JWT를 발급해 요청에 포함. 토큰 생성 방식이 바뀌어도 2곳만 고치면 되고, 운영과 제일 비슷한 경로라고 판단했다.
  - 판단에 쓴 제약 4개: ① principal이 `Long`이어야 함 ② 테스트가 사용자 1을 가정함 ③ E2E에도 쓸 수 있어야 함 ④ 의존성 추가 여부
  - 로드맵과의 연결: 1단계의 refresh 토큰 결함을 재현하려면 실제 토큰이 필요.
  - 받아들인 대가: JWT 필터가 깨지면 테스트 37건이 한꺼번에 실패. 단, 원인이 한 곳이라 찾기는 쉽다.
- 버린 대안과 이유:
  - 로그인해서 받기: 로그인이 깨지면 원인을 가리기 어렵다. 통합 테스트에서는 `LoginUseCase`가 mock이라 사용 불가능.
  - spring-security-test: E2E에는 쓸 수 없어서 방식이 2개가 됨. 의존성을 추가해야 하고, JWT 필터 결함을 잡지 못함. `@WithMockUser`는 principal이 `User` 객체라서 그대로 쓰면 401 발생. (통합 테스트를 `@WebMvcTest`로 바꾸면 유리해질 수 있음)
  - 필터 끄기 + 직접 설정: 인가 규칙이 테스트에서 빠지고, E2E에는 쓸 수 없음.
  - 테스트 전용 보안 설정: 운영 `SecurityConfig`에 조건을 넣어야 하고, 운영 보안 동작은 검증하지 못함. CLAUDE.md가 E2E를 "real impl"로 정의한 것과도 어긋남.
- 한 일:
  - `IntegrationTestBase`에 `bearerToken(Long userId)` 헬퍼를 추가하고, 통합 테스트 27건에 헤더를 삽입.
  - E2E 10건에는 `setBearerAuth`를 넣었고, 그중 GET 2건은 `getForEntity`를 `exchange`로 바꿈.
  - 효과가 없던 보안 비활성화 설정과 주석을 삭제.
- 결과:
  - 로컬 테스트: 54건 중 실패 37건에서 0건.
  - main CI: PR #2 병합 이후 처음으로 녹색(run 34923823194).
- 막힌 곳 / 다음에 볼 것:
  - 각 방식의 장단점을 비교하고 한 가지로 정하는 데서 실무 감각이 부족함을 느꼈고, 결정에 어려움이 있었다.
  - 테스트 환경 구성에 익숙하지 않아서 다양한 방식으로 테스트를 구현하는 배경 지식을 갖춰야 할 필요가 있을 것 같다.

## [#19] refresh 토큰이 access 토큰으로 통과하는 결함 수정 (2026-09-15)

- 문제: refresh 토큰으로 `GET /users/me`를 요청하면 200이 응답됨.
  - 재현 테스트 `getMyProfile_Unauthorized_RefreshToken`: `Status expected:<401> but was:<200>` (`./gradlew :app:test --tests "com.btg.integration.UserControllerIntegrationTest"`)
- 가설: 인증 필터의 `validateToken`이 서명만 확인하고 토큰 종류(`type` claim)를 보지 않는다.
- 원인:
  - `validateToken` 하나를 요청 인증(access용)과 `RefreshTokenService`·`LogoutService`(refresh용)가 함께 써서, 어느 쪽도 토큰 종류를 확인하지 않았다.
  - 재현 테스트는 기존 성공 테스트와 토큰만 다르게 두어, 200이 곧 "refresh 토큰으로 인증이 통과했다"는 증거가 되게 했다.
- 결정과 근거: 토큰 종류별로 서로 배타적인 검증 메서드를 둔다. 요청 인증은 access만, refresh·로그아웃은 refresh만 받는다.
  - RFC 8725 §3.12: 같은 발급자의 여러 종류 JWT는 검증 규칙이 서로 배타적이어야 한다(MUST). 그중 "claim 값을 다르게 둔다" 전략을 썼다.
  - 범용 `validateToken`을 지워 잘못 쓸 메서드가 남지 않게 했다.
  - 재현 테스트는 HTTP 동작을 검사하므로 수정 방식과 무관하게 유지된다.
- 버린 대안과 이유:
  - `validateToken`에 access 확인만 추가: refresh·로그아웃이 refresh 토큰을 거절하게 되어 깨진다.
  - 서명 키 분리: RFC가 인정하는 전략이지만 검증 분리는 여전히 필요하고, 두 시크릿을 같은 값으로 잘못 설정하면 보호가 조용히 사라진다. 유출 피해 범위·독립 교체 같은 이점은 배포가 없는 지금은 드러나지 않아, 키 관리가 필요해질 때 다시 본다.
- 한 일:
  - `IntegrationTestBase`에 `refreshBearerToken` 헬퍼와 재현 테스트 추가.
  - `JwtTokenProvider`에 `validateAccessToken`·`validateRefreshToken`(type 확인)을 두고 `validateToken` 삭제.
  - 필터는 access 검증으로, `GenerateTokenPort`·`JwtTokenAdapter`·`RefreshTokenService`·`LogoutService`는 refresh 검증으로 교체.
- 결과:
  - refresh 토큰 요청: 200 → 401. 전체 테스트 55건 통과.
  - 반대 방향(access 토큰을 refresh에 사용)은 수정 전에도 저장된 refresh 토큰 조회에서 막혀 실제 피해는 없었다.
- 막힌 곳 / 다음에 볼 것:
  - 새 테스트 대신 기존 PUT 테스트의 기대값을 바꿔, 그 실패를 재현으로 착각했다. 실패를 보면 어느 테스트가 실패했는지부터 확인한다.
  - "엄격함"을 키 분리로만 생각했는데, 핵심은 검증 규칙이 서로 배타적인가였다.
  - 토큰 종류를 `type` claim 대신 `typ` 헤더로 명시하는 방식(RFC 8725 §3.11 권장).  