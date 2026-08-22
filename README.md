# EasyFit 💪

> **운동 가이드와 캘린더 기록을 한 번에 관리하는 개인 운동 관리 웹 애플리케이션**

EasyFit은 운동을 처음 시작하는 사용자가 **운동 방법을 쉽게 확인하고, 운동 기록을 캘린더에 남길 수 있도록** 만든 개인 운동 관리 서비스입니다.

정적 프론트엔드(HTML/CSS/JavaScript)로 시작한 프로젝트를 **Spring Boot REST API 기반의 계층형 아키텍처**로 리팩토링하여 백엔드 구조와 데이터 관리까지 구현했습니다.

---

## 📸 서비스 화면

### 🏠 Home

서비스의 핵심 기능인 운동 가이드와 나만의 캘린더로 바로 이동할 수 있도록 구성했습니다.

<p align="center">
  <img src="./images/home.png" alt="EasyFit Home" width="900">
</p>

### 🏋️ 운동 가이드

가슴, 등, 하체, 어깨, 팔, 코어 등 부위별로 운동을 확인할 수 있으며, 운동 난이도와 장비 정보를 함께 제공합니다.

<p align="center">
  <img src="./images/guide.png" alt="EasyFit 운동 가이드" width="900">
</p>

### 📅 나만의 캘린더

운동한 날짜를 스탬프로 기록하고, 운동 부위를 이모지로 표시할 수 있습니다. 날짜별 메모를 통해 운동 내용을 함께 관리할 수 있습니다.

<p align="center">
  <img src="./images/calendar.png" alt="EasyFit 캘린더" width="900">
</p>

### 🔐 로그인

Google과 Kakao OAuth2 로그인을 지원하여 소셜 계정으로 간편하게 서비스를 이용할 수 있도록 구성했습니다.

<p align="center">
  <img src="./images/login.png" alt="EasyFit 로그인" width="900">
</p>

---

## 📌 주요 기능

| 기능 | 설명 |
|---|---|
| 🏋️ 운동 가이드 | 부위별 운동 목록과 운동 방법, 타겟 근육, 주의사항 안내 |
| 📅 캘린더 기록 | 날짜별 운동 여부를 스탬프로 표시하고 메모 저장 |
| 🏷️ 운동 부위 태깅 | 운동한 부위를 이모지로 표시 |
| 🔐 소셜 로그인 | Google / Kakao OAuth2 로그인 |
| 💾 운동 기록 관리 | 사용자별 운동 기록을 데이터베이스에 저장 |

---

## 🛠 기술 스택

| 분류 | 기술 |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 |
| Data Access | Spring Data JPA, Hibernate |
| Template Engine | Thymeleaf |
| Database | MySQL |
| Security | Spring Security, OAuth2 Client |
| OAuth2 | Google, Kakao |
| Build Tool | Gradle |
| ETC | Lombok |

---

## 🏗️ 아키텍처

EasyFit은 **Layered Architecture**를 기반으로 각 계층의 역할을 분리했습니다.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
```

- **Controller**: HTTP 요청 및 응답 처리
- **Service**: 핵심 비즈니스 로직 처리
- **Repository**: 데이터베이스 접근
- **Entity**: 도메인 데이터 모델 관리
- **Security**: 로그인 및 인증/인가 처리
- **DTO**: 계층 간 데이터 전달

### 📂 패키지 구조

```text
src/main/java/com/yongje/easyfit
├── config/       # 애플리케이션 설정
├── controller/   # 페이지 / REST API 컨트롤러
├── dto/          # 계층 간 데이터 전달 객체
├── entity/       # JPA Entity
├── repository/   # Spring Data JPA Repository
├── security/     # 인증 / 인가
└── service/      # 비즈니스 로직
```

---

## 🔗 API Endpoint

| Method | URI | 설명 |
|---|---|---|
| GET | `/api/workouts` | 전체 운동 목록 조회 |
| GET | `/api/workouts/{bodyPart}` | 부위별 운동 목록 조회 |
| GET | `/api/categories` | 운동 부위 카테고리 조회 |
| GET | `/api/calendar/month` | 월별 캘린더 기록 조회 |
| GET | `/api/calendar/{date}` | 특정 날짜 기록 조회 |
| POST | `/api/calendar/{date}/stamp` | 운동 완료 스탬프 저장 |
| POST | `/api/calendar/{date}/memo` | 메모 등록 / 수정 |

---

## 🔐 인증 및 데이터 관리

민감한 설정값은 소스 코드에 직접 포함하지 않고 환경변수로 분리했습니다.

```text
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password

GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret

KAKAO_CLIENT_ID=your_kakao_client_id
KAKAO_CLIENT_SECRET=your_kakao_client_secret
```

이를 통해 DB 접속 정보와 OAuth 인증 키가 GitHub 저장소에 노출되지 않도록 구성했습니다.

---

## 🚀 실행 방법

### 1. 사전 준비

- Java 21
- MySQL
- Gradle

### 2. 환경변수 설정

위의 DB 및 OAuth 관련 환경변수를 설정합니다.

### 3. 애플리케이션 실행

IDE(Eclipse / STS 등)에서 `EasyFitApplication`을 실행합니다.

```text
http://localhost:8080
```

---

## 👤 사용자 흐름

```text
EasyFit 접속
      ↓
운동 가이드 확인
      ↓
운동 부위 / 운동 방법 선택
      ↓
운동 수행
      ↓
캘린더에서 날짜 선택
      ↓
운동 부위 스탬프 + 메모 기록
      ↓
나의 운동 기록 관리
```

---

## 🎯 프로젝트를 통해 경험한 것

- Spring Boot 기반 REST API 설계
- Controller / Service / Repository 계층 분리
- Spring Data JPA를 활용한 DB 연동
- MySQL 기반 데이터 모델링
- Spring Security를 활용한 인증 처리
- Google / Kakao OAuth2 로그인 연동
- 사용자별 운동 기록 관리
- 환경변수를 활용한 민감 정보 분리
- 기존 정적 프론트엔드 프로젝트의 백엔드 리팩토링

---

## 📄 프로젝트 산출물

- ERD / 테이블 설계서
- UML Use Case 다이어그램
- 화면 설계서

---

## 🔮 향후 개선 계획

- 운동 기록 통계 및 그래프 페이지 추가
- 사용자별 맞춤 운동 루틴 추천
- 운동별 상세 정보 및 검색 기능 확장
- 운동 기록 기반의 성취도 분석

---

## 📷 Screenshot

본 README의 모든 서비스 화면은 실제 EasyFit 화면을 캡처한 이미지이며, GitHub 저장소의 `images` 폴더에서 관리합니다.

```text
EasyFit-README/
├── README.md
└── images/
    ├── home.png
    ├── guide.png
    ├── calendar.png
    └── login.png
```

---

## 💡 Portfolio

**EasyFit**은 운동 초보자가 복잡하게 느낄 수 있는 운동 정보와 기록 관리를 하나의 서비스에서 간단하게 사용할 수 있도록 구현한 프로젝트입니다.

특히 정적 프론트엔드에서 시작하여 **Spring Boot + JPA + MySQL + OAuth2 기반의 웹 애플리케이션으로 발전시킨 과정**을 통해 웹 애플리케이션의 전체적인 구조와 백엔드 개발 경험을 쌓는 것을 목표로 했습니다.
