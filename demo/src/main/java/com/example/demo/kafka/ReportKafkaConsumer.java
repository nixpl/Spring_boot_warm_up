package com.example.demo.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReportKafkaConsumer {

    @KafkaListener(topics = "delayed-returns-log", groupId = "report-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        String message = record.value();
        System.out.println("Received Kafka log message:");
        System.out.println(message);
    }
}
