package ru.sapegin.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Getter
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaAspectConsumer {

    @KafkaListener(topics = "service_logs")
    public void listenServiceLogs(ConsumerRecord<String, String> record){
        var type = new String(record.headers().lastHeader("type").value());
        var message = record.value();
        log.info("LISTENER service_logs [type={}]: {}", type, message);
    }
}
