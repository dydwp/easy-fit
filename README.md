# EasyFit 💪

운동 가이드와 캘린더 기록을 한 번에 관리할 수 있는 개인 운동 관리 웹 애플리케이션입니다.
기획부터 설계, 개발, 배포까지 **1인 개발**로 진행했으며, 정적 프론트엔드(HTML/CSS/JavaScript)로 시작한 프로젝트를 계층형 아키텍처를 갖춘 Spring Boot REST API 서버로 리팩토링했습니다.

| 항목 | 내용 |
|---|---|
| 개발 기간 | 기획 → 설계 → 개발 → 배포 (1인 개발) |
| 배포 | Render (백엔드) + Aiven (클라우드 MySQL) |
| 인증 | Google / Kakao OAuth2 소셜 로그인 |
| 담당 역할 | 요구사항 정의, DB 설계, REST API 설계·구현, OAuth2 인증 연동, 배포 및 배포 환경 트러블슈팅 전 과정 단독 수행 |

## 📸 스크린샷

| 홈 | 운동 가이드 |
|---|---|
| ![홈 화면](./images/home.png) | ![운동 가이드](./images/guide.png) |

| 로그인 | 캘린더 |
|---|---|
| ![로그인](./images/login.png) | ![캘린더](./images/calendar.png) |

## 📌 주요 기능

### 1. 운동 가이드 & 캘린더 기록 (핵심 도메인)
사용자가 실제로 사용하는 두 가지 핵심 화면을 REST API 기반으로 구현했습니다.
- 부위별(가슴/등/어깨/팔/하체 등) 운동 목록과 자세, 타겟 근육, 주의사항을 조회하는 **운동 가이드**
- 날짜별로 운동 완료 여부를 스탬프로 표시하고, 그날 수행한 부위를 이모지로 태깅하며, 메모까지 남길 수 있는 **캘린더 기록**
- 캘린더는 월 단위 조회(`/api/calendar/month`)와 일자 단위 조회(`/api/calendar/{date}`)를 분리해, 프론트에서 월 뷰를 그릴 때와 특정 날짜를 클릭했을 때 필요한 데이터만 각각 가져오도록 설계했습니다.

### 2. 회원·인증
- Google / Kakao 두 provider를 지원하는 OAuth2 로그인을 직접 구현했습니다. `CustomOAuth2UserService`에서 `DefaultOAuth2UserService`를 확장해, provider마다 다른 사용자 정보 응답 구조(Google은 `sub`/`email`/`name`, Kakao는 `kakao_account` 안에 중첩된 `profile`)를 각각 파싱하도록 분기 처리했습니다.
- provider + providerId를 기준으로 기존 회원이면 정보를 갱신하고, 신규 회원이면 저장하는 upsert 로직을 구현해 같은 이메일이라도 로그인 provider가 다르면 별개 계정으로 안전하게 분리했습니다.
- `SecurityConfig`에서 `/calendar`, `/api/calendar/**`처럼 개인 데이터가 걸린 경로만 인증을 요구하고 나머지(홈, 가이드, 로그인)는 비회원도 접근 가능하도록 인가 범위를 최소한으로 좁혔습니다.

### 3. 데이터 설계
- `User`, `Workout`, `CalendarRecord`, `BodyPartCategory` 4개의 JPA 엔티티로 회원, 운동 데이터, 캘린더 기록, 부위 카테고리를 모델링했습니다.
- Controller → Service → Repository → Entity로 계층을 분리하고, Service는 인터페이스와 구현체(`impl`)를 나눠 API 계약과 실제 로직을 분리했습니다. 덕분에 프론트 요구사항이 바뀌어도 컨트롤러/DTO만 수정하면 되도록 만들었습니다.

## 🔧 트러블슈팅: 배포 후 DB 연결 실패

로컬에서는 정상 동작하던 애플리케이션이 Render에 배포한 직후 DB 연결에 실패하는 문제가 발생했습니다.

- **문제**: 배포 환경에서 애플리케이션이 기동하지 못하고 DB 연결 에러가 발생
- **원인 파악**: 기능 자체의 버그로 의심하고 코드를 다시 수정하기보다, 로컬과 배포 환경의 **차이**에 먼저 주목했습니다. 확인 결과 `spring.datasource.url`이 `localhost:3306`으로 하드코딩되어 있어, 로컬 MySQL만 바라보고 있었다는 것을 확인했습니다.
- **해결**: DB 접속 정보(URL, 계정, 비밀번호)를 코드에서 분리해 환경변수(`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`)로 주입받도록 변경하고, 프로덕션 DB는 Aiven에서 제공하는 클라우드 MySQL로 연결했습니다. 로컬 실행 시에는 기본값(`localhost:3306`)이 그대로 적용되도록 해 로컬/배포 환경을 코드 변경 없이 전환할 수 있게 만들었습니다.
- **이후 습관**: 이 경험 이후로 DB 접속 정보뿐 아니라 OAuth2 클라이언트 ID/Secret 등 환경마다 달라질 수 있는 값은 처음부터 환경변수로 분리해서 설계하는 습관이 생겼습니다.

## 🛠 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 |
| Data Access | Spring Data JPA, Hibernate |
| Template Engine | Thymeleaf |
| Database | MySQL (로컬), Aiven MySQL (배포) |
| Security | Spring Security, OAuth2 Client (Google, Kakao) |
| Build Tool | Gradle |
| Deploy | Render |
| ETC | Lombok |

## 🏗 아키텍처

계층형(Layered) 아키텍처를 기반으로 관심사를 분리했습니다.

```
Controller  →  Service  →  Repository  →  Entity
(요청/응답)     (비즈니스 로직)   (DB 접근)      (도메인 모델)
```

```
src/main/java/com/yongje/easyfit
├── config/       # 설정 (Security 등)
├── controller/   # 페이지/REST API 컨트롤러
├── dto/          # 계층 간 데이터 전달 객체
├── entity/       # JPA 엔티티 (User, Workout, CalendarRecord, BodyPartCategory)
├── repository/   # Spring Data JPA 리포지토리
├── security/     # 인증/인가 관련 (PrincipalDetails 등)
└── service/      # 비즈니스 로직
    └── impl/     # 서비스 구현체
```

## 🔗 API 엔드포인트

| Method | URI | 설명 |
|---|---|---|
| GET | `/api/workouts` | 전체 운동 목록 조회 |
| GET | `/api/workouts/{bodyPart}` | 부위별 운동 목록 조회 |
| GET | `/api/categories` | 부위 카테고리 목록 조회 |
| GET | `/api/calendar/month` | 월별 캘린더 기록 조회 |
| GET | `/api/calendar/{date}` | 특정 날짜 기록 조회 |
| POST | `/api/calendar/{date}/stamp` | 운동 완료 도장 찍기 |
| POST | `/api/calendar/{date}/memo` | 메모 등록/수정 |

## 🚀 실행 방법

### 1. 사전 준비
- Java 21
- MySQL 서버

### 2. 환경변수 설정
민감한 정보(DB 접속 정보, OAuth 키)는 코드에 포함하지 않고 환경변수로 분리했습니다. 실행 전 아래 환경변수를 설정해주세요.

```
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
KAKAO_CLIENT_ID=your_kakao_client_id
KAKAO_CLIENT_SECRET=your_kakao_client_secret
```

### 3. 실행

**배포 링크(포트폴리오용)**

🔗 https://easy-fit.onrender.com

**로컬에서 직접 실행하고 싶다면**

IDE(Eclipse/STS 등)에서 `EasyFitApplication`을 실행한 뒤, 브라우저에서 아래 URL로 접속합니다.

```
http://localhost:8080
```

## 📄 산출물

- ERD / 테이블 설계서
- UML Use Case 다이어그램
- 화면설계서

## 📝 향후 개선 계획

- 운동 기록 통계/그래프 페이지 추가
- 사용자 운동 기록·목표 데이터를 기반으로 한 AI 맞춤 운동 루틴 추천 기능
