# salvo
REST API for salvo-style game

run: gradle clean build -> gradle wrapper -> ./gradlew bootRun

OS: Ubuntu 17.04

Tools:
Spring Boot 2.0.3 || Built with gradle 4.6


Issues:

- In Spring Boot 2.0 the addResources property no longer exists.
I added this in my build.gradle:
bootRun {
	sourceResources sourceSets.main
},

- I  had to add @ComponentScan in Application class