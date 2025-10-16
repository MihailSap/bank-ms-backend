package ru.sapegin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.sapegin.dto.PaymentDTO;
import ru.sapegin.util.KafkaTestHelper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"client_payments"})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    @WithMockUser
    void createPaymentTest() throws Exception {
        PaymentDTO dto = new PaymentDTO(456L, new BigDecimal("999.99"), "TYPE");
        mockMvc.perform(post("/api/payment/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var consumerFactory = KafkaTestHelper.createConsumerFactory("paymentTestGroup", embeddedKafkaBroker);
        var consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "client_payments");

        ConsumerRecord<String, Object> record = KafkaTestUtils.getSingleRecord(consumer, "client_payments");
        assertThat(record).isNotNull();
        assertThat(record.value()).isNotNull();

        PaymentDTO received = objectMapper.convertValue(record.value(), PaymentDTO.class);
        assertThat(received.getAccountId()).isEqualTo(456L);
        assertThat(received.getAmount()).isEqualByComparingTo(new BigDecimal("999.99"));
        assertThat(received.getType()).isEqualTo("TYPE");
        consumer.close();
    }
}
