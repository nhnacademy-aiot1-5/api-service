# ⚙️ API 서비스
<img width="1110" alt="image" src="https://github.com/nhnacademy-aiot1-5/api-service/assets/98167706/17abe6ab-9cc4-421a-9968-f0c2eb10e988">
<br>
<br>
시스템에 필요한 API를 제공하는 서비스입니다.
<br>
<br>
<div>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
<br>
<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<br>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/influxdb-22ADF6?style=for-the-badge&logo=influxdb&logoColor=white">
<br>
<img src="https://img.shields.io/badge/maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/nhncloud-2B5CDE?style=for-the-badge&logo=cloudera&logoColor=white">
<img src="https://img.shields.io/badge/sonarqube-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white">
</div>
<br>
<br>

## 🛠️ 개발 내용
### 👨‍💻 유승진
- User API 구현
- API 명세 작성을 위해 Springdoc Swagger 적용
- @AdminOnly AOP 작성
   - Role.ADMIN만 접근할 수 있게 하는 AOP로 그 외 권한은 401 에러 발생
- Admin API 구현
   - 권한관리, 유저관리, 예산 설정, 예산 변경내역, 조직관리
- 센서 API 구현
   - 장소, Modbus센서, Channel

### 👩‍💻 이은지
- 대시보드 전월/당월/전일/당일 전력 사용량 조회
    - 전월, 전일은 MySQL에서, 당월, 당일은 influx에서 조회
- 대시보드 실시간 전력량 조회
    - 실시간으로 가장 많은 전력을 쓴 채널 10개 조회
 
### 👩‍💻 임세연
- 요구사항 분석 및 ERD 설계
- CI/CD 구축
- 회원가입, 로그인, USER 회원 CRUD 기능 구현 
- 금월 일별 총전력량, 누적 요금, 예측 전력량, 예측 요금 조회 기능 구현
- 최근 1시간 총전력량 조회 기능 구현
- MQTT 센서 CRUD 기능 구현
- 토픽 CRUD 기능 구현
- RuleEngine으로 요청을 보내는 MqttSensorAdaptor 구현
- 조직 코드 검증 AOP구현
- SpringDoc API 명세 작성

### 👨‍💻 하준영
- 일별, 월별 전력량 및 요금 CRUD API 구현
- 장소 및 채널 CRUD API 구현
- 시계열 데이터베이스 InfluxDB 연결 설정
