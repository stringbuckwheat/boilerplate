목차

# 1. Spring Boot / React 보일러 플레이트🔥
블로그에서 가장 핫한 포스트가 Spring Boot 3 설정 및 CORS 설정 관련 포스트이기도 하고,     
토이 프로젝트를 만들 때마다 비슷한 코드를 반복해야 하는 탓에 보일러 플레이트 코드를 작성했어요.     

쓰임새에 따라 브랜치로 구분해놓았습니다.     
스터디원과 공유할 겸 깃허브에 등록합니다 🤟

cf)
README에 적지 못한 설명은 코드 내 주석에 적어놓았어요.   
전체 검색으로 MEMIL을 검색하면 쉽게 찾아볼 수 있어요!

## 사용 기술
* Spring Boot 3.1
  * JPA/Hibernate
  * QueryDsl
* React 18
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
  * **access token을 사용한 로그인이 가능합니다**


# 2. 사용 방법 - branch DATA
### 🌿 체크리스트
* **yml** 설정하기 - DB, JPA 설정을 완성해주세요
* JPA ddl 옵션이 update라 **서버를 켜면 테이블**이 만들어져요.
  * 데이터베이스를 확인해 JPA 설정 성공 여부를 확인하세요
  * 필요시 다른 모드로 바꾸세요
* ```src/generated/...``` 경로에서 Qclass가 생성 여부를 확인하세요 
  * Qclass가 무사히 생성되었다면, QueryDsl 연동 완료입니다
* gitignore에 QClass 경로를 추가해주세요
* mock data가 필요하다면, ```/src/main/test```의 UserRepositoryTest의 insertUsers 메소드를 활용하세요

### 🌿 프로젝트 구조

```
.
├── build.gradle // ⭐️ dependency, clean 재정의
├── setting-react // ⭐️ React 프로젝트
│   ├── public
│   ├── src
│   │   ├── App.js // ⭐️ 로그인 form
│   │   ├── index.css
│   │   ├── index.js
│   ├── package.json // ⭐️ 프록시 서버 설정
│   └── .env // ⭐️ 서버 주소를 저장해뒀습니다
└── src
    ├── main
    │   ├── java
    │   │   └── com/memil/setting
    │   │       └── SettingApplication.java
    │   │           ├── config
    │   │           │   ├── QueryDslConfig.java // ⭐️ QueryDsl 설정
    │   │           │   └── SecurityConfig.java // ⭐️ Spring Security 설정
    │   │           ├── controller
    │   │           │   └── LoginController.java // ⭐️ 로그인 컨트롤러
    │   │           ├── entity
    │   │           │   └── User.java // ⭐️ User 엔티티
    │   │           └── repository
    │   │              ├── UserQueryRepository.java // ⭐️ QueryDsl 리파지토리(인터페이스)
    │   │              ├── UserQueryRepositoryImpl.java // ⭐️ QueryDsl 리파지토리(구현체)
    │   │              └── UserRepository.java // ⭐️ JPA 리파지토리(쿼리 메소드)
    │   └── resources
    │       └── application.yml // ⭐️ DB, JPA, Spring Boot 설정 정보
    └── test
        └── java
            └── com
                └── memil
                    └── setting
                        └── UserRepositoryTest.java // ⭐️ User Mock data 메소드, 로그인 테스트 메소드
    
```
* **React**
  * setting-react
    * 이 프로젝트는 spring boot 프로젝트 안에 react 프로젝트가 들어있는 구조입니다
    * .env
      * 통신할 서버의 주소를 기록합니다.
      * Spring Boot가 기본 포트를 사용하고 있지 않을 시, 이곳을 수정하세요.
    * package.json
      * 가장 하단 프록시 설정이 있습니다.
      * Spring Boot가 기본 포트를 사용하고 있지 않을 시, 이곳을 수정하세요.
    * App.js
      * 간단한 로그인 폼이 있어요
      * 테스트 성공 데이터(username, password)가 기본값으로 설정되어 있습니다.

    
     

* **Spring Boot**
  * build.gradle
    * JPA, QueryDsl 의존성을 확인하세요
    * 엔티티 변경 시 새 Qclass를 반영할 수 있도록 clean을 재정의합니다
  * QueryDslConfig.java
    * JpaQueryFactory를 빈으로 등록합니다.
  * SecurityConfig.java
    * CORS와 Security FilterChiain 설정 파일이에요
    * React가 다른 포트를 사용하고 있거나, 기타 설정이 더 필요하다면 이곳에서 설정하세요
  * LoginController.java
    * JPA 쿼리메소드 / QueryDsl 메소드를 사용하여 로그인합니다
  * User.java
    * 엔티티
    * username(PK), password 필드 구성이에요
  * application.yml
    * DB와 JPA 설정 정보를 기입하세요
  * UserTrpositoryTest.java
    * DB에 입력할 mock data 메소드, 로그인 테스트 메소드가 간단히 구현되어 있습니다.

### 🌿 Repository 구성
![repository](https://github.com/stringbuckwheat/boilerplate/assets/104717358/bb0847fe-1f7d-4896-ae68-5d897c69a881)
