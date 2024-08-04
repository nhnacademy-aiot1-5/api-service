# 베이스 이미지 선택
FROM amd64/maven:3.8.4-openjdk-11-slim as build

# 환경 변수를 빌드 단계에서 사용할 수 있도록 설정
ARG SPRING_PROFILES_ACTIVE
ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD
ARG INFLUX_URL
ARG INFLUX_BUCKET
ARG INFLUX_ORG
ARG INFLUX_TOKEN

# 환경 변수를 시스템 프로퍼티로 전달
#ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
ENV INFLUX_URL=${INFLUX_URL}
ENV INFLUX_BUCKET=${INFLUX_BUCKET}
ENV INFLUX_ORG=${INFLUX_ORG}
ENV INFLUX_TOKEN=${INFLUX_TOKEN}

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 복사
COPY pom.xml .
COPY src ./src

# Maven을 통한 프로젝트 빌드
# 환경 변수를 Maven 프로퍼티로 전달
RUN mvn -B -f pom.xml clean package -DskipTests=true \
    -Dspring.datasource.url=$SPRING_DATASOURCE_URL \
    -Dspring.datasource.username=$SPRING_DATASOURCE_USERNAME \
    -Dspring.datasource.password=$SPRING_DATASOURCE_PASSWORD \
    -Dspring.influx.url=$INFLUX_URL \
    -Dspring.influx.bucket=$INFLUX_BUCKET \
    -Dspring.influx.org=$INFLUX_ORG \
    -Dspring.influx.token=$INFLUX_TOKEN


# 런타임 이미지 준비
FROM amd64/openjdk:11-jre-slim

WORKDIR /app

# 빌드 단계에서 생성된 jar 파일 복사
COPY --from=build /app/target/*.jar app.jar

# 포트 8090 열기
EXPOSE 8090

# 환경 변수를 런타임 이미지에 설정
# Set the active Spring profile
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
ENV INFLUX_URL=${INFLUX_URL}
ENV INFLUX_BUCKET=${INFLUX_BUCKET}
ENV INFLUX_ORG=${INFLUX_ORG}
ENV INFLUX_TOKEN=${INFLUX_TOKEN}

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
