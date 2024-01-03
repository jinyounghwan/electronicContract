# 전자 계약 관리 시스템

- [개발 환경](#개발-환경)
- [환경 변수](#환경-변수)
- [서버 접속 URL](#서버-접속)


## 개발 환경
| 스펙          | 버전    |
|-------------|-------|
| JDK         | 17    |
| Spring Boot | 3.1.0 |
| MyBatis     | 3.0.2 |
| thymeleaf   | 3.1.0 |

[//]: # (| Spring Security |6.1.1|)

## 환경 변수
```
-Dspring.profiles.active=<profile>
JASYPT_PASSWORD='dejay1234!@#$'
```

## 서버 접속
[전자 계약 관리 시스템](http://10.34.220.195:3030/)

## 애플리케이션 실행
```bash
java -jar -Dspring.profiles.active=local <jar>
```
