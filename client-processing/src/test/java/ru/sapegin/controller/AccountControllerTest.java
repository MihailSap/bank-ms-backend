package ru.sapegin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.sapegin.dto.ClientProductKeyDTO;
import ru.sapegin.enums.KeyEnum;
import ru.sapegin.model.Product;
import ru.sapegin.repository.ProductRepository;
import ru.sapegin.util.KafkaTestHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, topics = {"client_products", "client_credit_products"})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        productRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void createClientProductTest() throws Exception {
        var product = new Product();
        product.setName("card");
        product.setKey(KeyEnum.DC);
        productRepository.save(product);

        ClientProductKeyDTO dto = new ClientProductKeyDTO(101L, product.getId(), 5, null);
        mockMvc.perform(post("/api/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var consumerFactory = KafkaTestHelper.createConsumerFactory("client_product_test", embeddedKafkaBroker);
        var consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "client_products");

        ConsumerRecord<String, Object> record = KafkaTestUtils.getSingleRecord(consumer, "client_products");
        assertThat(record).isNotNull();
        ClientProductKeyDTO received = objectMapper.convertValue(record.value(), ClientProductKeyDTO.class);
        assertThat(received.getClientId()).isEqualTo(101L);
        assertThat(received.getProductId()).isEqualTo(product.getId());
        assertThat(received.getKeyType()).isEqualTo(KeyEnum.DC);
        consumer.close();
    }

    @Test
    @WithMockUser
    void createClientProductTest2() throws Exception {
        var product = new Product();
        product.setName("name");
        product.setKey(KeyEnum.AC);
        productRepository.save(product);

        ClientProductKeyDTO dto = new ClientProductKeyDTO(202L, product.getId(), 10, null);
        mockMvc.perform(post("/api/account/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        var consumerFactory = KafkaTestHelper.createConsumerFactory("client_credit_product_test", embeddedKafkaBroker);
        var consumer = consumerFactory.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "client_credit_products");

        ConsumerRecord<String, Object> record = KafkaTestUtils.getSingleRecord(consumer, "client_credit_products");
        assertThat(record).isNotNull();
        ClientProductKeyDTO received = objectMapper.convertValue(record.value(), ClientProductKeyDTO.class);
        assertThat(received.getClientId()).isEqualTo(202L);
        assertThat(received.getProductId()).isEqualTo(product.getId());
        assertThat(received.getKeyType()).isEqualTo(KeyEnum.AC);
        consumer.close();
    }
}
