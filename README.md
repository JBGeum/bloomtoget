# bloomtoget 🌸

> 그룹과 함께 목표를 만들고, 매일 달성 여부를 체크하는 그룹형 Todo 서비스

---

## 목차

- [서비스 소개](#서비스-소개)
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [아키텍처](#아키텍처)
- [모듈 구조](#모듈-구조)
- [로컬 실행 방법](#로컬-실행-방법)
- [설계 결정](#설계-결정)
- [로드맵](#로드맵)

---

## 서비스 소개

bloomtoget은 혼자서는 지속하기 어려운 계획 및 목표 달성 과정을 **그룹의 힘으로 함께 달성**하는 서비스입니다.

그룹에 참여한 멤버들이 같은 목표를 공유하고, 매일 달성 여부를 체크하면서 서로의 진행 상황을 확인할 수 있습니다. 몇 명이 오늘 목표를 달성했는지 실시간으로 볼 수 있어 자연스러운 동기부여가 됩니다.

> 이 프로젝트는 [구 버전(Java 8 + Spring MVC + MyBatis)](https://github.com/JBGeum/bloomtoget)을 Java 21 + Spring Boot 3 + 헥사고날 아키텍처로 전면 재설계한 버전입니다.

---

## 주요 기능

| 기능 | 설명 | 상태 |
|------|------|------|
| 회원 가입·로그인 | JWT access/refresh 토큰 발급, 토큰 재발급, 로그아웃 | 구현 |
| 프로필 관리 | 프로필 조회 및 정보 수정 | 구현 |
| 그룹 | 그룹 생성·수정·삭제, 검색, 가입(정원 확인)·탈퇴, 멤버 목록 | 구현 |
| 목표 | 그룹 안에서 목표 생성·수정·삭제, 상태 변경, 참여·중단 | 구현 |
| 일일 달성 체크 | 참여 중인 목표의 오늘 달성 여부 체크 | 미구현 |
| 달성 현황 확인 | 목표별 달성률, 오늘 달성한 인원 수 | 미구현 |

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Architecture | Hexagonal Architecture (Ports & Adapters) |
| Build | Gradle (Multi-module + Convention Plugins) |
| Database | PostgreSQL 16 (로컬), H2 (테스트) |
| ORM | Spring Data JPA |
| Auth | Spring Security + JWT |
| Mapping | MapStruct |
| Test | JUnit 5, Mockito, MockMvc |
| Container | Docker Compose (로컬 DB) |
| CI | GitHub Actions (build + test) |

---

## 아키텍처

3모듈 헥사고날 아키텍처를 적용했습니다. 비즈니스 로직이 외부 기술(DB, 웹 프레임워크)에 의존하지 않도록 경계를 분리했습니다.

```
┌─────────────────────────────────────────────┐
│                   app                        │
│           (실행 진입점, 설정)                  │
└───────────────────┬─────────────────────────┘
                    │ depends on
┌───────────────────▼─────────────────────────┐
│              infrastructure                  │
│  ┌──────────────┐  ┌──────────────────────┐ │
│  │  web adapter │  │ persistence adapter  │ │
│  │ (Controller) │  │  (JPA Repository)    │ │
│  └──────┬───────┘  └──────────┬───────────┘ │
└─────────┼────────────────────┼──────────────┘
          │ calls               │ implements
┌─────────▼────────────────────▼──────────────┐
│                  domain                      │
│   ┌─────────────┐   ┌────────────────────┐  │
│   │  Use Cases  │   │  Services          │  │
│   │  (Ports)    │   │  (비즈니스 로직)   │  │
│   └─────────────┘   └────────────────────┘  │
└─────────────────────────────────────────────┘
```

**의존성 방향:** `infrastructure` → `domain` ← `app`
domain 모듈은 JPA·웹·보안 기술에 의존하지 않습니다. Spring 의존은 `@Service`와 `@Transactional`로 한정합니다.

---

## 모듈 구조

```
bloomtoget/
├── app/                    # 실행 진입점, Spring Boot Application, 전체 설정
├── domain/                 # Use Case, Port 인터페이스, 비즈니스 로직(Service)
├── infrastructure/         # 어댑터 구현체 (Web, Persistence, Security)
├── buildSrc/               # Convention Plugins (모듈 공통 Gradle 설정)
├── docs/                   # 로드맵, API 명세
├── docker-compose.yml      # 로컬 개발 환경 (PostgreSQL)
└── settings.gradle
```

### 각 모듈의 책임

**domain** — Use Case 인터페이스(인바운드 포트)와 외부 시스템 접근용 아웃바운드 포트를 정의하고, 서비스가 그룹·목표 등의 비즈니스 규칙을 수행합니다. JPA·웹·보안 기술에 의존하지 않습니다.

**infrastructure** — 웹 요청을 처리하는 Controller, JPA를 이용한 Persistence 어댑터, Spring Security·JWT 설정이 포함됩니다. Controller는 Use Case를 호출하고, Persistence 어댑터는 domain의 아웃바운드 포트를 구현합니다.

**app** — Spring Boot 실행 진입점과 전체 설정을 담당합니다. 모든 모듈을 조립하는 역할입니다.

---

## 로컬 실행 방법

필요한 것: JDK 17 이상(Gradle 실행용), Docker. 컴파일에 쓰는 JDK 21은 Gradle toolchain이 자동으로 내려받습니다.

```bash
# 1. PostgreSQL 실행
docker compose up -d

# 2. 로컬 설정 파일 준비
cp app/src/main/resources/application-local.yml.example app/src/main/resources/application-local.yml

# 3. 애플리케이션 실행 (local 프로필)
./gradlew :app:bootRun
```

- API 문서: http://localhost:8080/swagger-ui.html
- 테스트: `./gradlew test` (H2 인메모리 DB를 사용하므로 Docker가 필요 없습니다)

---

## 설계 결정

### 헥사고날 아키텍처를 선택한 이유

구 버전(Spring MVC + MyBatis)은 Controller → Service → Mapper가 강하게 결합되어 있었습니다. DB 기술을 교체하거나 테스트를 작성할 때 전체 레이어를 함께 수정해야 했습니다.

헥사고날 아키텍처를 적용하면 domain이 외부 기술(JPA, PostgreSQL, HTTP)을 모릅니다. Use Case 테스트를 DB 없이 작성할 수 있고, JPA를 다른 기술로 교체해도 domain 코드는 바뀌지 않습니다.

### Records를 도입한 이유

domain 모듈의 Command/Result 객체를 Record로 작성합니다. 불변성이 컴파일 타임에 보장되고, 보일러플레이트(getter, equals, hashCode, toString)가 제거됩니다. 입력 검증은 Command 객체가 스스로 책임집니다.

---

## ERD

> (ERD 이미지 추가 예정)

구 버전 ERD 참고: https://imgur.com/lcpGmjA

---

## 로드맵

진행 계획과 범위는 [docs/ROADMAP.md](docs/ROADMAP.md)에 있습니다.

---

## 업데이트 내역

| 날짜         | 내용                                             |
|------------|------------------------------------------------|
| 2022.04.13 | 구 버전(Java 8 + Spring MVC) 최초 배포                |
| 2025.10.23 | Java 21 + Spring Boot 3 + 헥사고날 아키텍처로 전면 재설계 시작 |
| 2026.04.26 | README 업데이트                                 |
