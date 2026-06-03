plugins {
	java
	id("org.springframework.boot") version providers.gradleProperty("springBootVersion").get()
	id("io.spring.dependency-management") version providers.gradleProperty("springDependencyManagementVersion").get()
}

group = "ai.gelej"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(providers.gradleProperty("javaVersion").get().toInt())
	}
}

repositories {
	mavenCentral()
}

val telegramBotsVersion = providers.gradleProperty("telegramBotsVersion").get()

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.ai:spring-ai-starter-model-anthropic")

	implementation("org.telegram:telegrambots-client:$telegramBotsVersion")
	implementation("org.telegram:telegrambots-springboot-longpolling-starter:$telegramBotsVersion")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.ai:spring-ai-bom:${providers.gradleProperty("springAiVersion").get()}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
