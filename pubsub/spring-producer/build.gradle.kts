
group = "me.zeroest.kafka.spring-producer"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
