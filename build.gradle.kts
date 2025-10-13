plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "6.2.0.5505"
    jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok:1.18.32")
	annotationProcessor("org.projectlombok:lombok:1.18.32")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
    implementation("org.hibernate.orm:hibernate-core:7.1.1.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0")
    implementation("com.h2database:h2:2.3.232")
    implementation("redis.clients:jedis:6.2.0")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.named<JacocoReport>("jacocoTestReport"))
}

sonar {
    properties {
        property("sonar.projectKey", "com.example:demo")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.token", "squ_7008828242d6d45803b7fde3798a923828b2b680")
        property("sonar.junit.reportPaths", "build/test-results/test")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")


    }
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.withType<Test>())
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.named("sonar") {
    dependsOn("jacocoTestReport")
}
