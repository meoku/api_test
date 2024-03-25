# Team_UPGRADE

## 요청 방식
### 1. GET : 날씨 API에서 DB로 데이터 넣기

- 스케줄러로 동작하며 데이터를 넣는것을 기본으로 하고 있음
- 해당 링크는 수동으로 집어넣어야 하는 경우에 사용
- 링크 : http://localhost:8080/api/weather

﻿

### 2. POST : DB에서 날씨 정보 가져오기
- 링크 : http://localhost:8080/api/weather-data
- 요청 파라미터
- Body : raw (json)
    ```
    { "currentTime": "2024-03-24 16:00:00" }
    ```
