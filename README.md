#### salvo
REST API for salvo-style game

OS: Ubuntu 17.04

Tools:
Spring Boot 2.0.3 || Built with gradle 4.6

Issues: =>
M4T1 Step8: In Spring Boot 2.0 the addResources property no longer exists.
I added this in my build.gradle:
bootRun {
	sourceResources sourceSets.main
}
