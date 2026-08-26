# EasyFit 💪

운동 가이드와 캘린더 기록을 한 번에 관리할 수 있는 개인 운동 관리 웹 애플리케이션입니다.
정적 프론트엔드(HTML/CSS/JavaScript)로 시작해, 계층형 아키텍처를 갖춘 Spring Boot REST API 프로젝트로 리팩토링했습니다.

## 📸 스크린샷

| 홈 | 운동 가이드 |
|---|---|
| ![홈 화면](./images/home.png) | ![운동 가이드](./images/guide.png) |

| 로그인 | 캘린더 |
|---|---|
| ![로그인](./images/login.png) | ![캘린더](./images/calendar.png) |

## 📌 주요 기능

- **운동 가이드**: 부위별(가슴/등/어깨/팔/하체 등) 운동 목록과 자세, 타겟 근육, 주의사항 안내
- **캘린더 기록**: 날짜별 운동 여부를 도장(스탬프)으로 표시하고, 메모를 남길 수 있는 캘린더
- **운동 부위 태깅**: 캘린더에 그날 수행한 운동 부위를 이모지로 표시
- **소셜 로그인**: Google / Kakao OAuth2 로그인 지원

## 🛠 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 |
| Data Access | Spring Data JPA, Hibernate |
| Template Engine | Thymeleaf |
| Database | MySQL |
| Security | Spring Security, OAuth2 Client (Google, Kakao) |
| Build Tool | Gradle |
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
- 사용자별 맞춤 운동 루틴 추천 기능
