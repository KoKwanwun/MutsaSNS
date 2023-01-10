# MutsaSNS 프로젝트
회원가입 · 로그인 · 게시판 · 댓글 · 좋아요 · 알람 기능이 있는 SNS 프로젝트.<br>
화면 UI는 현재 구현중

## 접속 링크
- GitLab : https://gitlab.com/kkw1232/finalproject_kokwanwun_team9
- Swagger : http://ec2-54-180-98-1.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/
- 화면 UI : http://ec2-54-180-98-1.ap-northeast-2.compute.amazonaws.com:8080/posts

## 개발환경
- Java 11
- Build : Gradle 7.5.1
- Framework : Springboot 2.7.6
- Database : MySQL 8.0
- CI & CD : GitLab
- Server : AWS EC2
- Deploy : Docker
- IDE : IntelliJ

## 체크리스트
- AWS EC2에 Docker 배포
- Gitlab CI & Crontab CD
- Swagger
- 회원가입
- 로그인
- 포스트 작성, 수정, 삭제, 리스트
- 화면 UI 개발 : 게시판 조회, 게시판 상세(구현중)
- ADMIN 회원으로 등급업하는 기능
- ADMIN 회원이 로그인 시 자신이 쓴 글이 아닌 글과 댓글에 수정, 삭제를 할 수 있는 기능
- 댓글
- 좋아요
- 마이피드
- 알림
- Swagger에 ApiOperation을 써서 Controller 설명 보이도록 설정

## 특이사항
- Swagger 적용
  - Swagger의 편의성을 위해 특정 Endpoint에만 Token을 인증했을 경우 접근할 수 있도록 함. (예로, 회원가입, 로그인은 토큰 인증이 있으나 없으나 접근 가능하도록)
![image](https://user-images.githubusercontent.com/84280815/209634214-e942c8c0-52d3-4545-b5cb-af52b3a7d584.png)

- CI/CD
  - main 브랜치 변경할 때만 CI/CD 되도록 수정함.

- Token 예외처리
  - Token 예외 처리를 AuthenticationEntryPoint, addFilterBefore를 추가적으로 넣어서 해결했음.
(참고 자료 : [JWT 토큰 만료에 대한 예외처리](https://velog.io/@hellonayeon/spring-boot-jwt-expire-exception), [스프링시큐리티 JWT 예외처리](https://velog.io/@dltkdgns3435/%EC%8A%A4%ED%94%84%EB%A7%81%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-JWT-%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC))

- Auditing
  - createdAt, lastModifiedAt을 `2022/12/27 13:40:51`의 형태로 저장하기 위해 String 형태로 바꾸어 저장 (참고 자료 : [JPA Auditing 형식 변경](https://kimseungjae.tistory.com/13))
  - isDeleted로 삭제 여부만 확인할 수 있도록 설정
  - Docker 서버에 띄운 후 게시판을 등록하면, Docker의 서버 시간으로 등록이 되어, ZonedDateTime을 `Asia/Seoul`으로 하여 어디서나 서울 시간으로 되도록 설정

- Soft Delete
  - delete 메소드가 호출되었을 때, 물리적으로 삭제하는 것이 아닌 논리적으로 삭제하도록 update문이 실행되도록 함.

- Like 테이블 설정
  - Like 테이블을 생성할 때, SQL문의 LIKE 연산자로 인식하므로 문자열로 설정해줘야 함. : `@Table(name = "\"like\"")`

- 알람 설정
  - 자신의 게시글에 좋아요나 댓글이 달면 알림 DB에 저장되지 않도록 설정

- 도전과제(User 등급업 기능)
  - 초기 ADMIN 역할의 유저는 DB에서 수동 설정
  - 이미 USER인 경우, 이미 ADMIN인 경우, 유저가 ADMIN이 아닌경우, 입력된 role이 정해진 값이 아닌 경우를 모두 예외 처리함.
  - 또한, enum 형태로 받기 위해 UserRole 클래스에 하위 메소드를 추가했습니다. (대소문자 관계없이 입력받을 수 있음)
    ```java
    @JsonCreator
    public static UserRole create(String requestValue) {
    return Stream.of(values())
    .filter(v -> v.toString().equalsIgnoreCase(requestValue))
    .findFirst()
    .orElse(null);
    }
    ```

- 도전과제(화면 UI 구현)
  - Startbootstrap 사이트를 활용.
  - 현재 게시판 목록은 구현 완료, 게시판 상세 구현중

## Endpoint
### View (구현중)
- 게시판 목록 `GET /posts`
![image](https://user-images.githubusercontent.com/84280815/211489523-c8aed09d-533b-432b-b4fb-c97af1b08344.png)
- 게시판 상세 `GET /posts/{id}`
![image](https://user-images.githubusercontent.com/84280815/211489701-4d97e4b0-5456-4fd7-9717-e4d5c30798ca.png)

### Endpoint Info
| METHOD | URL                                  | Description             | JWT Required |
|--------|--------------------------------------|-------------------------|:------------:|
| POST   | /api/v1/users/join                   | 회원가입                    |      X       |
| POST   | /api/v1/users/login                  | 로그인                     |      X       |
| POST   | /api/v1/users/{id}/role/change       | 회원 등급 변경 (ADMIN 등급만 가능) |      O       |
| GET    | /api/v1/posts                        | 포스트 리스트                 |      X       |
| POST   | /api/v1/posts                        | 포스트 등록                  |      O       |
| PUT    | /api/v1/posts/{id}                   | 포스트 수정                  |      O       |
| DELETE | /api/v1/posts/{id}                   | 포스트 삭제                  |      O       |
| GET    | /api/v1/posts/{postsId}              | 포스트 상세                  |      X       |
| GET    | /api/v1/posts/my                     | 마이피드                    |      O       |
| GET    | /api/v1/posts/{postId}/comments      | 댓글 조회                   |      X       |
| POST   | /api/v1/posts/{postId}/comments      | 댓글 작성                   |      O       |
| PUT    | /api/v1/posts/{postId}/comments/{id} | 댓글 수정                   |      O       |
| DELETE | /api/v1/posts/{postId}/comments/{id} | 댓글 삭제                   |      O       |
| POST   | /api/v1/posts/{postId}/likes         | 좋아요 누르기                 |      O       |
| GET    | /api/v1/posts/{postId}/likes         | 좋아요 개수                  |      X       |
| GET    | /api/v1/alarms                       | 알람 리스트 조회               |      O       |

### Endpoint Input, Return Example
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
      
- 마이피드 `GET /api/v1/posts/my`
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "content": [
                {
                  "id": 81,
                  "title": "string",
                  "body": "string",
                  "userName": "123",
                  "createdAt": "2023/01/10 17:32:11",
                  "lastModifiedAt": "2023/01/10 17:32:11"
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

- 댓글 조회
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "content": [
                {
                  "id": 1,
                  "comment": "hello-comment",
                  "userName": "kyeongrok45",
                  "postId": 2,
                  "createdAt": "2023/01/04 09:35:08",
                  "lastModifiedAt": "2023/01/04 09:35:08"
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
    
- 댓글 작성
  - 입력 (JSON 형식)
    ```json
    {
        "comment" : "test comment",
    }
    ```
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "id": 49,
            "comment": "string",
            "userName": "123",
            "postId": 2,
            "createdAt": "2023/01/10 17:28:06",
            "lastModifiedAt": "2023/01/10 17:28:06"
        }
    }
    ```

- 댓글 수정
  - 입력 (JSON 형식)
    ```json
    {
        "comment" : "update comment",
    }
    ```
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "id": 49,
            "comment": "update comment",
            "userName": "123",
            "postId": 2,
            "createdAt": "2023/01/10 17:28:06",
            "lastModifiedAt": "2023/01/10 17:28:06"
        }
    }
    ```

- 댓글 삭제
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "message": "댓글 삭제 완료",
            "postId": 49
        }
    }
    ```

- 좋아요 누르기
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": "좋아요를 눌렀습니다."
    }
    ```

- 좋아요 개수
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": 4
    }
    ```

- 알람 리스트 조회
  - 리턴 (JSON 형식)
    ```json
    {
        "resultCode": "SUCCESS",
        "result": {
            "content": [
                {
                  "id": 16,
                  "alarmType": "NEW_COMMENT_ON_POST",
                  "fromUserId": 29,
                  "targetId": 5,
                  "text": "new comment!",
                  "createdAt": "2023/01/10 14:33:18",
                  "lastModifiedAt": "2023/01/10 14:33:18"
                },
                {
                  "id": 4,
                  "alarmType": "NEW_LIKE_ON_POST",
                  "fromUserId": 7,
                  "targetId": 6,
                  "text": "new like!",
                  "createdAt": "2023/01/04 17:30:30",
                  "lastModifiedAt": "2023/01/04 17:30:30"
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
      
## Error Info
|  Status Code  | Error Message                   | Description           |
|:-------------:|--------------------------------------|-----------------------|
|      409      | DUPLICATED_USER_NAME                   | UserName 중복           |
|      404      | USERNAME_NOT_FOUND                  | UserName이 DB에 존재하지 않음 |
|      401      | INVALID_PASSWORD                  | 패스워드가 일치하지 않음         |
|      401      | INVALID_TOKEN                  | 유효하지 않은 Token임        |
|      401      | INVALID_PERMISSION                  | 사용자가 권한이 없음           |
|      404      | POST_NOT_FOUND                  | Post가 DB에 존재하지 않음     |
|      500      | DATABASE_ERROR                  | DB 에러                 |
|      404      | ROLE_NOT_FOUND                  | 입력받은 ROLE이 없음         |
|      409      | ALREADY_ROLE_USER                  | 이미 Role이 USER임        |
|      409      | ALREADY_ROLE_ADMIN                  | 이미 Role이 ADMIN임       |
|      404      | COMMENT_NOT_FOUND                  | Comment가 DB에 존재하지 않음  |
|      409      | ALREADY_CLICK_LIKE                  | 이미 좋아요를 눌렀음           |

## 테스트 코드
### Controller
|  기능 구분  | 내용                                                                 |
|:-------:|--------------------------------------------------------------------|
|  회원가입   | 성공 / 실패 (1) userName이 중복인 경우                                       |
|   로그인   | 성공 / 실패 (1) userName 없음 (2) password 일치하지 않음                       |
| 포스트 상세  | 성공                                                                 |
| 포스트 등록  | 성공 / 실패 (1) JWT 유효하지 않음 (2) JWT 안의 userName이 DB에 없음 (3) 사용자가 권한이 없음 |
| 포스트 수정  | 성공 / 실패 (1) JWT 유효하지 않음 (2) 작성자 불일치 (3) DB 에러                      |
| 포스트 삭제  | 성공 / 실패 (1) JWT 유효하지 않음 (2) 작성자 불일치 (3) DB 에러                      |
| 포스트 리스트 | 성공                                                                 |
|  마이피드   | 성공 / 실패 (1) 로그인 하지 않음                                              |
|  댓글 등록  | 성공 / 실패 (1) 로그인 하지 않음 (2) 포스트 존재하지 않음                              |
|  댓글 수정  | 성공 / 실패 (1) JWT 유효하지 않음 (2) 포스트 존재하지 않음 (3) 작성자 불일치 (4) DB 에러        |
|  댓글 삭제  | 성공 / 실패 (1) JWT 유효하지 않음 (2) 포스트 존재하지 않음 (3) 작성자 불일치 (4) DB 에러        |
| 댓글 리스트  | 성공         |
| 좋아요 누르기 | 성공 / 실패 (1) 로그인 하지 않음 (2) 포스트 존재하지 않음                              |
| 좋아요 개수  | 성공 / 실패 (1) 포스트 존재하지 않음                               |
|   알람    | 성공 / 실패 (1) 로그인 하지 않음                               |

### Service
|  기능 구분  | 내용                                                                 |
|:-------:|--------------------------------------------------------------------|
| 포스트 등록  | 성공 / 실패 (1) 유저가 존재하지 않음                                            |
| 포스트 수정  | 성공 / 실패 (1) 포스트 존재하지 않음 (2) 작성자 불일치 (3) 유저 존재하지 않음                 |
| 포스트 삭제  | 성공 / 실패 (1) 포스트 존재하지 않음 (2) 작성자 불일치 (3) 유저 존재하지 않음                 |