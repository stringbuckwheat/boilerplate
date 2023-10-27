목차

# 1. Spring Boot / React 보일러 플레이트🔥
블로그에서 가장 핫한 포스트가 Spring Boot 3 설정 및 CORS 설정 관련 포스트이기도 하고,     
토이 프로젝트를 만들 때마다 비슷한 코드를 반복해야 하는 탓에 보일러 플레이트 코드를 작성했어요.

쓰임새에 따라 브랜치로 구분해놓았습니다.     
스터디원과 공유할 겸 깃허브에 등록합니다 🤟

cf)
README에 적지 못한 설명은 코드 내 주석에 적어놓았어요.   
전체 검색으로 MEMIL을 검색하면 쉽게 찾아볼 수 있어요!

## 버전, 디펜던시
* Spring Boot(3.1)
  * JPA/Hibernate
  * QueryDsl
* React(18)
  * Axios

## 브랜치 요약
* CORS
  * **Spring Boot, React 간 통신 이슈만 해결해요**
  * Spring Boot, React 연동 및 CORS 설정
  * Mock 데이터를 통한 간단한 로그인 테스트
* DATA
  * **DataBase, JPA, QueryDsl을 설정합니다**
  * yml DB 연동, JPA 설정
  * QueryDsl QClass 설정(build.gradle)
* LOGIN
  * **access token을 사용한 로그인**
  * 로그인 이후 처리용 Filter 및 예외처리
  * Request Header에 Access Token을 포함시키는 interceptor
  * access token 만료/유효하지 않을 시 로그아웃 처리


# 2. 사용 방법 - branch LOGIN
### 🌿 체크리스트
* yml에 JWT Key를 만들 비밀키를 추가해주세요
* (필요하다면) AccessToken의 EXPIRED_AFTER 필드를 수정하여 토큰 유효 시간을 조정하세요.
* JwtExceptionFilter의 에러코드, 메시지를 커스터마이징하세요

### 🌿 프로젝트 구조

```
.
├── build.gradle // ⭐️ dependency, clean 재정의
├── setting-react // ⭐️ React 프로젝트
│   ├── public
│   ├── src
│   │   ├── App.js // ⭐️ 라우팅 추가(react-router-dom)
│   │   ├── Login.jsx // ⭐️ 로그인 페이지
│   │   ├── Main.jsx // ⭐️ 로그인 후 넘어갈 메인 페이지
│   │   ├── index.css
│   │   ├── index.js
│   │   └── interceptors.js // ⭐️ axios 객체 설정 및 interceptor
│   ├── package.json // ⭐️ 프록시 서버 설정
│   └── .env // ⭐️ 서버 주소를 저장해뒀습니다
└── src
    ├── main
    │   ├── java
    │   │   └── com/memil/setting
    │   │       ├── SettingApplication.java
    │   │       ├── AccessToken.java // ⭐️ AccessToken 클래스
    │   │       ├── auth
    │   │       │   ├── JwtExceptionFilter.java // ⭐️ JWTFilter 예외처리
    │   │       │   ├── JwtFilter.java // ⭐️ Access Token 유효성 검사
    │   │       │   └── SecretKey.java // ⭐️ JWT Signature Key 생성 객체
    │   │       ├── config
    │   │       │   ├── QueryDslConfig.java // ⭐️ QueryDsl 설정
    │   │       │   └── SecurityConfig.java // ⭐️ Spring Security 설정
    │   │       ├── controller
    │   │       │   └── LoginController.java // ⭐️ 테스트용 컨트롤러
    │   │       ├── entity
    │   │       │   └── User.java // ⭐️ User 엔티티
    │   │       └── repository
    │   │           ├── UserQueryRepository.java // ⭐️ QueryDsl 리파지토리(인터페이스)
    │   │           ├── UserQueryRepositoryImpl.java // ⭐️ QueryDsl 리파지토리(구현체)
    │   │           └── UserRepository.java // ⭐️ JPA 리파지토리(쿼리 메소드)
    │   └── resources
    │       └── application.yml // ⭐️ DB, JPA, Spring Boot 설정 정보
    └── test
        └── java/com/memil
            └── setting
                └── UserRepositoryTest.java // ⭐️ User Mock data 메소드, 로그인 테스트 메소드
    
```

### 🌿 파일 설명
(개발 순서와 동일합니다)

* Spring Boot
  * build.gradle
    * JWT 관련 dependency 추가
  * SecretKey.java
    * AccessToken 생성 시 사용할 Secret Key를 만드는 객체입니다
    * AccessToken 클래스, JwtFilter에서 공통적으로 사용해요
  * AccessToken.java
    * 실제 엑세스 토큰 객체
    * encoding, decoding 메소드를 포함합니다.
  * LoginController.java
    * (CORS, DATA 브랜치와 달리) 사용자 정보를 Spring Security에 저장하고,
    * AccessToken을 발급합니다.
    * 테스트 용도로 인증이 필요한 메소드를 구현해놓았어요. ("/auth/test")
  * JwtFilter.java
    * header로 넘어온 access token의 유효성을 검사합니다
  * JwtExceptionFilter.java
    * JwtFilter의 예외처리를 담당합니다
  * SecurityConfig.java
    * JwtFilter, JwtExceptionFilter를 적절한 순서에 추가해요


* React
  * package.json
    * react-router-dom 의존성 추가
  * interceptor.js
    * Request Interceptor
      * Header에 access token 포함시키는 용도
    * Response Interceptor
      * Custom Error Code를 사용해 로그아웃
  * App.js
    * 라우팅 추가
    * 기본 주소에서 로그인 시 메인 페이지로 넘어갑니다.
  * Login.jsx
    * 로그인 페이지
    * access token을 받아 저장하고, 메인 페이지로 이동시켜요
  * Main.jsx
    * 컴포넌트 마운트 시점에 인증이 필요한 GET 요청이 하나 갑니다.
    * token이 유효하지 않을 시 로그아웃 처리됩니다.