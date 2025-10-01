package ru.sapegin.kafka;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        var properties = kafkaProperties.buildProducerProperties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic clientCardsTopic() {
        return TopicBuilder.name("client_cards")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic clientProductsTopic() {
        return TopicBuilder.name("client_products")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic clientCreditProductsTopic() {
        return TopicBuilder.name("client_credit_products")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic serviceLogsTopic() {
        return TopicBuilder.name("service_logs")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
