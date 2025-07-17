package com.example.demo.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaLogService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String topic = "delayed-returns-log";

    public void sendLog(String message) {
        kafkaTemplate.send(topic, message);
    }
}
