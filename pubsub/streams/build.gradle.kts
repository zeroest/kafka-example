plugins {
    java
}

group = "me.zeroest.kafka.streams"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
