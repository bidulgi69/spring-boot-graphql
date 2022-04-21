import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.20"
	kotlin("plugin.spring") version "1.6.20"
}

group = "kr.dove"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	val springGraphQLVersion = "12.0.0"

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-redis-reactive
	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-mongodb-reactive
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("com.graphql-java-kickstart:graphql-spring-boot-starter:$springGraphQLVersion")

	// https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.2")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	// testing facilities
	testImplementation("com.graphql-java-kickstart:graphql-spring-boot-starter-test:$springGraphQLVersion")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}