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
* OAUTH2
  * **구글, 카카오 소셜 로그인**
  * 예외처리
  * UserDetailService, UserDetails/OAuth2User 커스터마이징


# 2. 사용 방법 - branch OAUTH2
### 🌿 체크리스트
* 소셜 로그인 API를 세팅하세요
  * Redirect URI ```http://localhost:[spring port]/oauth2/code/kakao``` 기준으로 작성된 코드예요
* yml에 소셜 로그인 관련 정보를 기입하세요
  * 구글에 비해 카카오는 기입할 정보가 많습니다.
  * ⭐️ 카카오의 client-authentication-method는 ```POST```가 아니라, ***client_secret_post***로 작성해야합니다.
    * 오류나요!!
* User 엔티티를 수정했습니다. 실제 DB의 스키마 변경을 확인해주세요!


### 🌿 프로젝트 구조

```
.
├── build.gradle // ⭐️ dependency, clean 재정의
├── setting-react // ⭐️ React 프로젝트
│   ├── public
│   ├── src
│   │   ├── App.js // ⭐️ 라우팅 추가(react-router-dom)
│   │   ├── GetToken.jsx // ⭐️ url 파라미터로 전달된 access token 저장
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
    │   │       │   ├── JwtExceptionFilter.java // ⭐️ JWT Filter 예외처리
    │   │       │   ├── JwtFilter.java // ⭐️ Access Token 유효성 검사
    │   │       │   └── SecretKey.java // ⭐️ JWT Signature Key 생성 객체
    │   │       ├── config
    │   │       │   ├── QueryDslConfig.java // ⭐️ QueryDsl 설정
    │   │       │   └── SecurityConfig.java // ⭐️ Spring Security 설정
    │   │       ├── controller
    │   │       │   ├── GlobalExceptionHandler.java // ⭐️ 예외처리
    │   │       │   └── LoginController.java // ⭐️ 테스트용 컨트롤러
    │   │       ├── entity
    │   │       │   └── User.java // ⭐️ User 엔티티
    │   │       ├── oauth2
    │   │       │   ├── OAuth2Attribute.java // ⭐️ OAuth2 로그인 성공 정보를 담을 클래스
    │   │       │   ├── OAuth2MemberService.java // ⭐️ OAuth2 사용자 정보를 가져옵니다
    │   │       │   ├── OAuth2SuccessHandler.java // ⭐️ 성공 후 로직(access token과 함께 리다이렉트)
    │   │       │   ├── UserDetailsServiceImpl.java // ⭐️ 사용자 정보 로드
    │   │       │   └── UserPrincipal.java // ⭐️ OAuth2User와 UserDetails 커스터마이징
    │   │       └── repository
    │   │           ├── UserQueryRepository.java // ⭐️ QueryDsl 리파지토리(인터페이스)
    │   │           ├── UserQueryRepositoryImpl.java // ⭐️ QueryDsl 리파지토리(구현체)
    │   │           └── UserRepository.java // ⭐️ JPA 리파지토리(쿼리 메소드)
    │   └── resources
    │       └── application.yml // ⭐️ DB, JPA, Spring Boot 설정 정보
    └── test
        └── java/com/memil
            └── setting
                └── SettingApplicationTests.java // ⭐️ 기본 로그인용 유저 insert 메소드가 있어요
    
```

### 🌿 파일 설명
(개발 순서와 동일합니다)

* Spring Boot
  * build.gradle
    * OAuth2 dependency
  * application.yml
    * 소셜 로그인 관련 정보를 기입하세요
  * User.java
    * 엔티티를 수정했어요
    * 우리는 더이상 username을 PK로 쓸 수 없습니다. OAuth2 유저의 정보 중 unique한 값이 없으니까요
    * 저는 userId(auto_increment)로 해결했습니다. 다르게 구현하실 분들은 수정하세요!
  * OAuth2Attribute.java
    * OAuth2 로그인 성공 정보를 바탕으로 User 엔티티를 생성, 저장합니다.
  * UserPrincipal.java
    * OAuth2User, UserDetails를 implements한 클래스예요.
    * 이후 @AuthenticationPrincipal에서 기본/소셜 로그인 유저 공통으로 사용하기 위해 만들었습니다.
  * OAuth2MemberService.java
    * OAuth2 로그인 성공 정보를 바탕으로 User 엔티티를 생성, 저장합니다.
  * OAuth2SuccessHandler.java
    * UserPrincipal 객체의 정보를 바탕으로 Access Token을 생성한 뒤, redirect 시켜줍니다.
  * SecurityConfig.java
    * OAuth2 관련 설정을 추가해줍니다.
    * Spring Boot 2.X 버전과 문법이 다르니 참고하세요!
  * GlobalExceptionHandler.java
    * 기본 로그인 시 예외처리를 간단히 구현했어요
  * LoginController.java
    * 로그인 후 보일 메인 페이지에서 로그인한 사용자 정보를 리턴하도록 수정했습니다.
     

* React
  * Login.jsx
    * 소셜 로그인 버튼을 추가했어요.
    * 못생겼습니다
    * ```localhost:[spring boot 포트]/oauth2/authorization/[서비스이름]```으로 이동하도록 구현하세요
  * GetToken.jsx
    * useParams()를 사용해 우리가 만든 access token을 추출, 저장합니다.
  * App.js
    * GetToken 컴포넌트 라우팅 코드를 추가했어요
  * Main.jsx
    * 로그인 후 (인증이 필요한 API 요청으로) 간단히 로그인한 사용자 정보를 받아옵니다.