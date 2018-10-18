# salvo
REST API for salvo-style game

run: gradle clean build -> gradle wrapper -> ./gradlew bootRun

OS: Ubuntu 17.04

Tools:
Spring Boot 2.0.3 || Built with gradle 4.6


Issues:

- M4T1S8 => In Spring Boot 2.0 the addResources property no longer exists.
I added this in my build.gradle:
bootRun {
	sourceResources sourceSets.main
}

- M4T2S4 => I had to add @RestController anotation to my controller class instead of @Controller

- I  had to add @ComponentScan in Application class
