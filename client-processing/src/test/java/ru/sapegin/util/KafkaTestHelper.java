package ru.sapegin.util;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;

public class KafkaTestHelper {

    public static DefaultKafkaConsumerFactory<String, Object> createConsumerFactory(String groupId, EmbeddedKafkaBroker broker) {
        Map<String, Object> consumerProps =
                KafkaTestUtils.consumerProps(groupId, "true", broker);
        consumerProps.put("key.deserializer", StringDeserializer.class);
        consumerProps.put("value.deserializer", org.springframework.kafka.support.serializer.JsonDeserializer.class);
        consumerProps.put("spring.json.trusted.packages", "*");
        return new DefaultKafkaConsumerFactory<>(consumerProps);
    }
}
