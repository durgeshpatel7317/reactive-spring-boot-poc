FROM gradle:8.0.2-jdk17 as builder

WORKDIR /app

COPY ./gradlew /app/
COPY ./build.gradle /app/
COPY ./gradle /app/gradle

RUN ./gradlew dependencies

COPY ./src /app/src

RUN ./gradlew clean build

FROM gradle:8.0.2-jdk17

WORKDIR /app

# Name of the jar file changes as per the root directory
# Hence use the root directory name while copying the jarfile
COPY --from=builder /app/build/libs/app-0.0.1-SNAPSHOT.jar /app/
RUN mkdir json

ENTRYPOINT ["java", "-jar", "app-0.0.1-SNAPSHOT.jar"]

# docker build -t "test" .
# docker run -p 8080:8080 -v $(pwd)/json:/json <imageid>