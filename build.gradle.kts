plugins {
	java
	id("org.springframework.boot") version "4.0.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ai.gelej"
version = "0.0.1-SNAPSHOT"

val telegramBotsVersion = "8.3.0"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(26)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.ai:spring-ai-starter-model-anthropic")

	// Telegram Bot API: HTTP client + long polling receiver.
	// Webhook (push) mode is served by Spring MVC, so no embedded server module is needed.
	implementation("org.telegram:telegrambots-client:$telegramBotsVersion")
	implementation("org.telegram:telegrambots-longpolling:$telegramBotsVersion")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:2.0.0-M8")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
