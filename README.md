목차

# 1. Spring Boot / React 보일러 플레이트🔥
토이 프로젝트를 만들 때마다 비슷한 코드를 반복해야 하는 탓에 보일러 플레이트 코드를 작성했어요.     
쓰임새에 따라 브랜치로 구분해놓았습니다.     
스터디원과 공유할 겸 깃허브에 등록합니다 🤟

README에 적지 못한 설명은 코드 내 주석에 적어놓았어요.   
전체 검색으로 MEMIL을 검색하면 쉽게 찾아볼 수 있어요!

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


# 2. 사용 방법 - branch CORS
### 🌿 체크리스트
* CORS 에러가 나지 않는 것을 확인하세요.

### 🌿 프로젝트 구조

```
.
├── build.gradle
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
    │   │   └── com
    │   │       └── memil
    │   │           └── setting
    │   │               ├── LoginController.java // ⭐️ 로그인 메소드
    │   │               ├── SecurityConfig.java // ⭐️ CORS 포함한 security 설정
    │   │               └── SettingApplication.java // 메인 메소드
    │   └── resources
    └── test
```
* **React**
  * setting-react
    * 이 프로젝트는 spring boot 프로젝트 안에 react 프로젝트가 들어있는 구조입니다
    * App.js
      * 간단한 로그인 폼이 있어요
      * 테스트 성공 데이터(username, password)가 기본값으로 설정되어 있습니다.
    * package.json
      * 가장 하단 프록시 설정이 있습니다.
      * Spring Boot가 기본 포트를 사용하고 있지 않을 시, 이곳을 수정하세요.
    * .env
      * 통신할 서버의 주소를 기록합니다.
      * Spring Boot가 기본 포트를 사용하고 있지 않을 시, 이곳을 수정하세요.
     

* **Spring Boot**
  * LoginController.java
    * 가짜 데이터를 통해 로그인 테스트를 합니다.
  * SecurityConfig.java
    * CORS와 Security FilterChiain 설정 파일이에요
    * React가 다른 포트를 사용하고 있거나, 기타 설정이 더 필요하다면 이곳에서 설정하세요


