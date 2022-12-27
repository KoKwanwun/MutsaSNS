# MutsaSNS 프로젝트
멋쟁이사자처럼 최종 프로젝트로, 회원가입, 로그인, 게시판 기능이 있으며, API 형태로 리턴하도록 구현했습니다.
View는 현재 구현중입니다.

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