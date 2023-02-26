package me.zeroest.kafka.processor

import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.processor.ProcessorSupplier

class FilterProcessor : ProcessorSupplier<String, String> {
    override fun get(): Processor<String, String> {
        return object : Processor<String, String> {
            private lateinit var context: ProcessorContext

            override fun init(context: ProcessorContext) {
                this.context = context
            }

            override fun process(key: String?, value: String) {
                if (value.length > 5) {
                    context.forward(key, value)
                }
                context.commit()
            }

            override fun close() {
                // release resources
            }
        }
    }
}