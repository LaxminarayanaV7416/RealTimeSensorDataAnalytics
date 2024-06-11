# RealTimeSensorDataAnalytics


https://medium.com/@amberkakkar01/getting-started-with-apache-kafka-on-docker-a-step-by-step-guide-48e71e241cf2


# before running the docker run these commands
$ cd RealTimeSensorDataAnalyticsBackend
$ ./gradlew build

# to run only backend
$ cd RealTimeSensorDataAnalyticsBackend
$ ./gradlew bootRun

## Command to run the docker
$ docker compose --env-file dockerConfig.yml up

## Swagger UI
http://localhost:8080/swagger-ui/index.html


# websocket refernce link
https://kishoretarun.medium.com/websockets-integration-in-a-spring-boot-application-7e365c015f69