# Factory Pulse: Machinery Health Monitoring System - RealTimeSensorDataAnalyticsDashBoard

[![FrontEnd Docker Hub Link](https://img.shields.io/badge/Docker%20Hub%20Frontend%20Link-blue.svg)](
    https://hub.docker.com/r/laxvadnala/realtimeanalytics-frontend)
[![BackEnd Docker Hub Link](https://img.shields.io/badge/Docker%20Hub%20Backend%20Link-blue.svg)](
    https://hub.docker.com/r/laxvadnala/realtimeanalytics-backend)

## Introduction
This project centers around the utilization of machine sensors installed within newly constructed factories or machinery. These sensors continuously transmit data through a connected server using the MQTT protocol. Our backend is connected to the MQTT Broker publishes this data by the conencted clients through WebSocket, the websocket can be opened by web applications or any compatible application, including mobile apps. our Front end  application visually shows the data through graphs. In the event that an operator detects any malfunction in the machinery for any reason, they can refer to the application to identify the problematic machine and initiate necessary repairs. The application's scope extends beyond its current description; it can be expanded to include automatic alerts triggered by machine malfunction detection using machine learning. However, this aspect of the scope is not included in this project

## Architecture
![Applciation Flow](/assets/PSD.png)

we use MQTT brokers to enable the publish/subscribe (pub/sub) communication model helps make MQTT a highly efficient and scalable protocol. 
- IOT sensors or machine sensors are producers which publish the messages to the apache ActiveMQ (MQTT Broker).
- Our Backend is the universal subscriber to the the all publishers that means how many publishers may be created all are subscribed by our backend.
- Websockets which are usually have the capability the filter out to which particular publishers messages should we recieve as stream in real time.
- so we query the via websocket params to the backend that, we need to listen to machine 1 and our backend enables us to redirect the flow of messages from that particular publisher to the websocket connection in real time.

### Components used in Architecture
- #### Spring Boot
Spring Boot is an open-source Java framework used for programming standalone, production-grade Spring-based applications with minimal effort. Spring Boot is a convention-over-configuration extension for the Spring Java platform intended to help minimize configuration concerns while creating Spring-based applications
- #### Apache ActiveMQ (MQTT Broker)
Apache ActiveMQ is an open source message broker written in Java together with a full Java Message Service client. It provides "Enterprise Features" which in this case means fostering the communication from more than one client or server. It supports four protocols JMS, STOMP, MQTT and AMQP
- #### Postgres DB
PostgreSQL, also known as Postgres, is a free and open-source relational database management system emphasizing extensibility and SQL compliance
- #### React JS
React is a free and open-source front-end JavaScript library for building user interfaces based on components.
- #### Swagger UI
Swagger is a suite of tools for API developers from SmartBear Software and a former specification upon which the OpenAPI Specification is based.

## How we are sending data to MQTT broker as publisher (mocking the data sending process)
There are total two ways we can send the data to the MQTT broker
1. Using Apahe ActiveMQ UI
   * Using the ActiveMQ UI to send the Data to particular publisher. we can access the activeMQ UI from the link [http://localhost:8161/console/auth/login](http://localhost:8161/console/auth/login) the login credentials are (username : artemis & password : artemis), we will land up in below screen.
   ![Applciation Flow](/assets/ActiveMQ_1.png)
   * from the the above screen you have to select addressess dropdown and select the topic/publisher you want to send message, click on more and you will see the dropdown which contains the send message option select that.
   ![Applciation Flow](/assets/ActiveMQ_2.png)
   * Now the below screen will appear where you can enter the JSON message as you wish to send to the topic which will be sent to the subscribers who subscribed to the topic via web socket.
   ![Applciation Flow](/assets/ActiveMQ_3.png)
2. Using the API where we have to send the message (REST ENDPOINT is "http://{HOSTED_URL}/api/mqtt/publish" method is POST ).
   * for demonstration we will use postman to send call the REST Endpoint where the JSON is
    ```{JSON}
        {
            "topic": "<TOPIC_NAME>",
            "message": "<MESSAGE IN JSON FORMAT",
            "retained": true,
            "qos": 0
        }
    ```
    ![Applciation Flow](/assets/Postman_1.png)


<span style="color: red;">NOTE: we are actually calling this publish API from front end application, which is actually pishing the 100 records in 100 seconds (records per second) to the publisher, but the front end data populating is actually done from websocket which you can clearly understand in demo.</span>

## Running the Application In Local
1. <b>Method 1</b>: By downloading the docker compose, <a href="/assets/docker-compose.yml" download>Click to Download Docker Compose YAML file</a>. Now we have to run the command as shown below by changing the directory in terminal where this downloaded docker compose YAML is present. <span style="color: red;">NOTE: you can change the build SHA in docker compose YAML for both front end and backend to latest one by clicking the above links to docker hub for both front end and backend, You can pick one SHA and use both of them for better working.</span>
```{shell}
$ docker-compose up
```
2. <b>Method 2</b>: By Cloning this git repository and running the below commands sequentially as shown below
```{shell}
# change the terminal directory to git cloned directory and if you do "ls" you should see all files and folders you see in git

# now we have intially build the backend by executing the following commands
$ cd RealTimeSensorDataAnalyticsBackend
$ ./gradlew build
$ cd ..


# now we have run the docker compose up with the dockerConfig.env as parameter fed to it
$ docker-compose --env-file dockerConfig.env up
```

## Miscellenous and Useful commands
- #### To start Spring boot server
```{shell}
$ cd RealTimeSensorDataAnalyticsBackend
$ ./gradlew bootRun
```
- #### To start front end
```{shell}
$ cd sensor_analytics_frontend
$ npm start
```
- #### how to run all application at once with docker compose
```{shell}
# initally you have to build java spring boot application
$ cd RealTimeSensorDataAnalyticsBackend
$ ./gradlew build
$ cd ..

# Now we have run docker compose command the config are also pushed to github since we can change as per our needs
$ docker-compose --env-file dockerConfig.env up
```

## Limitations of running Application
* As of now we are only calling the api/mqtt/publish endpoint for 100 times if new topic is selected randomly since now IOT sensor is available to publish the data.
* we are not storing the published data in database rather we just sending it to the websocket.
* No History view of the previously published data of the sensor is available.

## Screenshots of Running Application
* Swagger UI
![swaggerUI](/assets/swaggerUI.png)
* User Logged in screen without any machines assigned
![userLoggedInScreen](/assets/userLoggedInScreen.png)
* Admin Logged in screen without any machines assigned
![adminLoggedInScreen](/assets/adminLoggedInScreen.png)
* Real time graph when we select any machine
![realtimeGraph_2](/assets/realtimeGraph_2.jpeg)
* Real time graph when we select any machine along with network tab and backend Logs
![realtimeGraph_1](/assets/realtimeGraph_1.jpeg)
* Machine assigned users table
![machineUserTable](/assets/machineUserTable.jpeg)

## What could be done in future to improve the Application
* To store the recieved sensor data in database along with sending it to the websocket clients.
* Enabling the history voew of the sensor data published.
* We can add alerts when the machine health is deviating from the usual flow using machine learning or AI.
* We can send the alerts via Emails, Phone text and more.

### Miscellenous Links for handy
#### Swagger UI Link after application is booted up
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

#### Apache Active MQ Management Console Link (Artemis)  (username : artemis & password : artemis)
[http://localhost:8161/console/auth/login](http://localhost:8161/console/auth/login)

