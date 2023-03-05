import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "1.7.22"
    kotlin("jvm") version "1.7.22"
    kotlin("kapt") version "1.6.21"
}

java.sourceCompatibility = JavaVersion.VERSION_17


allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    group = "me.zeroest.kafka"
    version = "0.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {

//    val developmentOnly = configurations.create("developmentOnly")
//    configurations {
//        runtimeClasspath {
//            extendsFrom(developmentOnly)
//        }
//        compileOnly {
//            extendsFrom(configurations.annotationProcessor.get())
//        }
//    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.apache.kafka:kafka-clients:3.4.0")
        implementation("org.apache.kafka:kafka-streams:3.4.0")
//        implementation("org.slf4j:slf4j-simple:2.0.6")

        // Kotlin 로깅
        implementation("io.github.microutils:kotlin-logging:1.12.5")

        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.kafka:spring-kafka")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.kafka:spring-kafka-test")

        testImplementation("org.jetbrains.kotlin", "kotlin-test-junit5", "1.3.72")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}
