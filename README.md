# MutsaSNS 프로젝트
멋쟁이사자처럼 최종 프로젝트로, 회원가입, 로그인, 게시판 기능이 있으며, API 형태로 리턴하도록 구현했습니다.
View는 현재 구현중입니다.

## 접속 링크
- GitLab : https://gitlab.com/kkw1232/finalproject_kokwanwun_team9
- Swagger : http://ec2-54-180-98-1.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/
- 화면 UI : http://ec2-54-180-98-1.ap-northeast-2.compute.amazonaws.com:8080/posts

## 체크리스트
- Swagger 적용
- CI/CD
- 회원가입 / 로그인 구현
- 게시판 리스트, 상세, 등록, 수정, 삭제 기능 구현
- UserRestController, PostRestController 테스트 코드 작성
- 도전과제(User 등급업 기능)
- 도전과제(화면 UI 구현)

## 접근 방법
- Swagger 적용
  - Swagger 구현은 편의성 개선에서 어려움이 있었습니다.
  - Authorize UI 기능은 관련 자료가 모두 동일해서 제가 원하는 형태의 UI를 만들기 힘들었습니다.
  - Swagger의 편의성을 위해 특정 Endpoint에만 Token을 인증했을 경우 접근할 수 있도록 했습니다.(예로, 회원가입, 로그인은 토큰 인증이 있으나 없으나 접근 가능하도록)
![image](https://user-images.githubusercontent.com/84280815/209634214-e942c8c0-52d3-4545-b5cb-af52b3a7d584.png)

- CI/CD
  - 제가 작성한 블로그를 참고했습니다. ([CI/CD 참고자료](https://velog.io/@id1232/GitLab-%EB%B9%8C%EB%93%9C-%EB%B0%8F-%EB%B0%B0%ED%8F%AC))
  - 이후, main 브랜치 변경할 때만 CI/CD 되도록 수정했습니다

- 회원가입 / 로그인 구현
  - 회원가입, 로그인 구현은 Authentication에서 어려움이 있었습니다.
  - Token 예외 처리 과정에서 어려움이 있었는데 AuthenticationEntryPoint, addFilterBefore를 추가적으로 넣어서 많은 예외처리를 할 수 있었습니다.
참고 자료([JWT 토큰 만료에 대한 예외처리](https://velog.io/@hellonayeon/spring-boot-jwt-expire-exception), [스프링시큐리티 JWT 예외처리](https://velog.io/@dltkdgns3435/%EC%8A%A4%ED%94%84%EB%A7%81%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-JWT-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC))

- 게시판 리스트, 상세, 등록, 수정, 삭제 기능 구현
  - 게시판 기능은 Auditing 기능 구현에서 어려움이 있었습니다.
  - Auditing : createdAt, lastModifiedAt을 `2022/12/27 13:40:51`의 형태로 하기 위해 많은 시간을 투자했습니다. String 형태로 바꾸고 해결했지만, deletedAt은 해결하지 못하여 isDeleted로 삭제 여부만 확인할 수 있도록 수정했습니다. 또한, Docker
서버에 띄운 후 게시판을 등록하면, Docker의 서버 시간으로 등록이 되어, ZonedDateTime을 `Asia/Seoul`으로 하여 어디서나 서울 시간으로 되도록 설정했습니다.
    ([JPA Auditing 형식 변경](https://kimseungjae.tistory.com/13))

- UserRestController, PostRestController 테스트 코드 작성
  - Test 코드를 짜는 과정에서는 많은 어려움이 있었습니다. 이 과정에서는 강사님의 Git을 많이 활용했습니다.

- 도전과제(User 등급업 기능)
  - 초기 ADMIN 역할의 유저는 DB에서 수동 설정하도록 했습니다.
  - 이 기능에는 많은 예외가 있었는데, 이미 USER인 경우, 이미 ADMIN인 경우, 유저가 ADMIN이 아닌경우, 입력된 role이 정해진 값이 아닌경우를 모두 예외 처리 했습니다.
  - 또한, enum 형태로 받기 위해 UserRole 클래스에 메소드를 추가했습니다.

- 도전과제(화면 UI 구현)
  - Startbootstrap 사이트를 활용했습니다. 하지만 예상과는 다르게 템플릿을 제 프로젝트에 적용하는 것에 어려움이 있었고, 결국 포스트 리스트, 상세 기능에만 적용할 수 있었습니다. 이 부분에서는 리펙토링 시간이나 2차 프로젝트에 여유가 있다면 보완할 것입니다.

## 특이 사항
- 아쉬웠던 점 : 예외 처리, Swagger 편의성 개선을 위해 시간을 많이 써서 도전과제인 화면 UI 부분을 완성하지 못한 부분이 아쉬웠습니다.

## Endpoint
### View (구현중)
- 게시판 목록 `GET /posts`
![image](https://user-images.githubusercontent.com/84280815/209626018-113485ad-5d33-4ad6-85e0-7c8772f1ab30.png)
- 게시판 상세 `GET /posts/{id}`
![image](https://user-images.githubusercontent.com/84280815/209626279-a493a468-aa59-481b-82c2-def43d1c9a72.png)

### API
- 회원가입 `POST /api/v1/users/join`
  - 입력 (JSON 형식)
    ```json
    {
        "userName" : "user1",
        "password" : "123"
    }
    ```
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "userId": 5,
            "userName": "user1"
        }
    }
    ```
    
- 로그인  `POST /api/v1/users/login`
  - 입력 (JSON 형식)
    ```json
    {
        "userName" : "user1",
        "password" : "123"
    }
    ```
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "jwt": "eyJhbGciOiJIUzI1NiJ9"
        }
    }
    ```
    
- 유저 등급 변경 `POST api/v1/users/{id}/role/change`
    - 입력 (JSON 형식)
      ```json
      {
          "role": "ADMIN" | "USER"
      }
      ```
    - 리턴 (JSON 형식)
      ```json
      {
          "resultCode": "SUCCESS",
          "result": {
              "userId": 10,
              "userName": "user9",
              "preRole": "USER",
              "postRole": "ADMIN"
          }
      }
      ```

- 포스트 리스트 `GET /api/v1/posts`
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "content": [
                {
                    "id": 62,
                    "title": "hello-title",
                    "body": "hello-body",
                    "userName": "user1",
                    "createdAt": "2022/12/27 13:40:51",
                    "lastModifiedAt": "2022/12/27 13:40:51"
                },
                {
                    "id": 61,
                    "title": "test-title",
                    "body": "test-body",
                    "userName": "user1",
                    "createdAt": "2022/12/27 13:32:41",
                    "lastModifiedAt": "2022/12/27 13:32:41"
                },
                {
                    "id": 60,
                    "title": "hello-title",
                    "body": "hello-body",
                    "userName": "user1",
                    "createdAt": "2022/12/27 13:30:46",
                    "lastModifiedAt": "2022/12/27 13:30:46"
                }
            ],
            "pageable": {
                "sort": {
                    "empty": false,
                    "sorted": true,
                    "unsorted": false
                },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 20,
            "paged": true,
            "unpaged": false
            },
            "totalPages": 3,
            "totalElements": 41,
            "last": false,
            "size": 20,
            "number": 0,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "numberOfElements": 20,
            "first": true,
            "empty": false
        }
    }
    ```

- 포스트 상세 `GET /api/v1/posts/{postId}`
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode":"SUCCESS",
        "result":{
            "id" : 1,
            "title" : "title1",
            "body" : "body1",
            "userName" : "user1",
            "createdAt" : yyyy-mm-dd hh:mm:ss,
            "lastModifiedAt" : yyyy-mm-dd hh:mm:ss
        }
    }
    ```
- 포스트 등록 `POST /api/v1/posts`
    - 입력 (JSON 형식)
      ```json
      {
          "title" : "title1",
          "body" : "body1"
      }
      ```
    - 리턴 (JSON 형식)
      ```json
      {
          "resultCode": "SUCCESS",
          "result": {
              "message": "포스트 등록 완료",
              "postId": 0
          }
      }
      ```

- 포스트 수정 `PUT /api/v1/posts/{id}`
    - 입력 (JSON 형식)
      ```json
      {
          "title" : "update title1",
          "body" : "update body1"
      }
      ```
    - 리턴 (JSON 형식)
      ```json
      {
          "resultCode": "SUCCESS",
          "result": {
              "message": "포스트 수정 완료",
              "postId": 0
          }
      }
      ```

- 포스트 삭제 `DELETE /api/v1/posts/{id}`
    - 리턴 (JSON 형식)
      ```json
      {
          "resultCode": "SUCCESS",
          "result": {
              "message": "포스트 삭제 완료",
              "postId": 0
          }
      }
      ```