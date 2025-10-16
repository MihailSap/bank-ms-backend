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
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.util.KafkaTestHelper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"client_transactions"})
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    @WithMockUser
    void createTransactionTest() throws Exception {
        TransactionDTO expected = new TransactionDTO(789L,456L,"ACCRUAL", new BigDecimal("250.75"), "SUCCESS", LocalDateTime.now());
        mockMvc.perform(post("/api/transaction/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected)))
                .andExpect(status().isOk());

        var consumerFactory = KafkaTestHelper.createConsumerFactory("transactionTestGroup", embeddedKafkaBroker);
        var consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "client_transactions");

        ConsumerRecord<String, Object> record = KafkaTestUtils.getSingleRecord(consumer, "client_transactions");
        assertThat(record).isNotNull();
        assertThat(record.value()).isNotNull();

        TransactionDTO received = objectMapper.convertValue(record.value(), TransactionDTO.class);
        assertEquals(expected, received);
        consumer.close();
    }
}
