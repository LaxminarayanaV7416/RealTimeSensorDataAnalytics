plugins {
    java
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.psd"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("org.postgresql:postgresql")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.4")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")
	implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.13")
	implementation("org.springdoc:springdoc-openapi-data-rest:1.6.13")
	implementation("org.springdoc:springdoc-openapi-security:1.6.13")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
	testImplementation("org.mockito:mockito-core:4.2.0")
	testImplementation("org.mockito:mockito-junit-jupiter:4.2.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")

	implementation ("org.springframework.boot:spring-boot-starter-security")
	
	implementation("org.springdoc:springdoc-openapi-ui:1.6.7")
	implementation("org.springdoc:springdoc-openapi-data-rest:1.6.7")


    implementation("org.springframework.security:spring-security-crypto:6.1.3")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.integration:spring-integration-mqtt")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.springframework.boot:spring-boot-starter-security")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.mockito:mockito-core:4.2.0")
    testImplementation("org.mockito:mockito-junit-jupiter:4.2.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
