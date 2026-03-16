package com.project.typingspeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ========================================================================================
 * TypingSpeedApplication.java - THE MAIN ENTRY POINT OF THE APPLICATION
 * ========================================================================================
 *
 * HOW IT WORKS:
 * When you run "mvn spring-boot:run", Java looks for the main() method in this class.
 * The @SpringBootApplication annotation does 3 things automatically:
 *
 *   1. @Configuration     - Marks this class as a source of bean definitions (Spring config).
 *   2. @EnableAutoConfiguration - Tells Spring Boot to automatically configure your app
 *                                 based on the dependencies in pom.xml. For example,
 *                                 since we have spring-boot-starter-web, it auto-configures
 *                                 an embedded Tomcat server on port 8080.
 *   3. @ComponentScan     - Tells Spring to scan this package (com.project.typingspeed)
 *                           and all sub-packages (controller/, service/) to find classes
 *                           annotated with @Controller, @Service, etc., and register them
 *                           as Spring-managed beans.
 *
 * FLOW:
 *   1. User runs "mvn spring-boot:run"
 *   2. Java calls main() method below
 *   3. SpringApplication.run() boots up the entire Spring framework
 *   4. Embedded Tomcat server starts on port 8080
 *   5. Spring scans for @Controller and @Service classes and wires them together
 *   6. Application is now ready to accept HTTP requests at http://localhost:8080
 */
@SpringBootApplication
public class TypingSpeedApplication {

    public static void main(String[] args) {
        // This single line starts the entire Spring Boot application:
        // - Initializes the Spring context
        // - Starts the embedded web server
        // - Registers all controllers and services
        SpringApplication.run(TypingSpeedApplication.class, args);
    }

}
