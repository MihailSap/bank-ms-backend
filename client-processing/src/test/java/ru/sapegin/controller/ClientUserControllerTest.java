package ru.sapegin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.sapegin.dto.ClientDTO;
import ru.sapegin.dto.ClientFastDTO;
import ru.sapegin.dto.RegistrationDTO;
import ru.sapegin.dto.UserDTO;
import ru.sapegin.enums.DocumentTypeEnum;
import ru.sapegin.model.Client;
import ru.sapegin.repository.ClientRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClientRepository clientRepository;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void init() {
        clientRepository.deleteAll();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void registerTest() throws Exception {
        var userDto = new UserDTO(42L, "test_login", "secret", "mail@test.com");
        mockServer.expect(requestTo("http://localhost:8084/api/auth/register"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(userDto), MediaType.APPLICATION_JSON));
        var clientDto = new ClientDTO("first_name","middle_name","last_name",
                LocalDate.of(1990, 1, 1), DocumentTypeEnum.PASSPORT,12345L,
                "prefix","suffix",77,5);
        var registration = new RegistrationDTO(userDto, clientDto);
        String response = mockMvc.perform(post("/api/ms1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registration)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO returnedUser = objectMapper.readValue(response, UserDTO.class);
        assertThat(returnedUser.id()).isEqualTo(42L);
        assertThat(returnedUser.login()).isEqualTo("test_login");
        assertThat(returnedUser.email()).isEqualTo("mail@test.com");

        var savedClient = clientRepository.findAll().stream()
                .filter(c -> c.getFirstName().equals("first_name"))
                .findFirst()
                .orElse(null);

        assertThat(savedClient).isNotNull();
        assertThat(savedClient.getUserId()).isEqualTo(42L);
        assertThat(savedClient.getDocumentPrefix()).isEqualTo("prefix");
        mockServer.verify();
    }

    @Test
    @WithMockUser
    void getClientDataTest() throws Exception {
        var client = new Client();
        client.setFirstName("first_name");
        client.setLastName("last_name");
        clientRepository.save(client);

        String response = mockMvc.perform(get("/api/ms1/client/{id}", client.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ClientFastDTO dto = objectMapper.readValue(response, ClientFastDTO.class);
        assertThat(dto.getFirstName()).isEqualTo("first_name");
        assertThat(dto.getLastName()).isEqualTo("last_name");
    }
}
