package me.zeroest.kafka.spring.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ConsumerSpringApplication

fun main(args: Array<String>) {
    runApplication<ConsumerSpringApplication>(*args)
}
