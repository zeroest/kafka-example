import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
    kotlin("kapt") version "1.6.21"
}

java.sourceCompatibility = JavaVersion.VERSION_17


allprojects {
    group = "me.zeroest.kafka"
    version = "0.0.0"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")

    val developmentOnly = configurations.create("developmentOnly")
    configurations {
        runtimeClasspath {
            extendsFrom(developmentOnly)
        }
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        implementation("org.apache.kafka:kafka-clients:3.4.0")
        implementation("org.slf4j:slf4j-simple:2.0.6")

        // Kotlin 로깅
        implementation("io.github.microutils:kotlin-logging:1.12.5")

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
