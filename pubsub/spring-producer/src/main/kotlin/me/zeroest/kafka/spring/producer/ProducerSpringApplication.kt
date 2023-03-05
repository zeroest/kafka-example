package me.zeroest.kafka.spring.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ProducerSpringApplication

fun main(args: Array<String>) {
    runApplication<ProducerSpringApplication>(*args)
}