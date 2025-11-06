package com.taskflow.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.user.dto.LoginRequest;
import com.taskflow.user.dto.RegistroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll(); // Limpa o banco de dados antes de cada teste
    }

    @Test
    void deveRegistrarNovoUsuarioComSucesso() throws Exception {
        RegistroRequest request = RegistroRequest.builder()
                .nome("Novo Usuário")
                .email("novo@example.com")
                .senha("senhaSegura123")
                .build();

        mockMvc.perform(post("/api/v1/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("novo@example.com"))
                .andExpect(jsonPath("$.perfil").value("COLLABORATOR"));
    }

    @Test
    void deveRetornarBadRequestQuandoEmailJaRegistrado() throws Exception {
        // Registrar um usuário primeiro via endpoint
        RegistroRequest request = RegistroRequest.builder()
                .nome("Usuario Existente")
                .email("existente@example.com")
                .senha("senhaExistente")
                .build();
        mockMvc.perform(post("/api/v1/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Tentar registrar com o mesmo email
        mockMvc.perform(post("/api/v1/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAutenticarUsuarioComSucessoERetornarToken() throws Exception {
        // Registrar um usuário para autenticar via endpoint
        RegistroRequest registroRequest = RegistroRequest.builder()
                .nome("Usuario Login")
                .email("login@example.com")
                .senha("senhaLogin123")
                .build();
        mockMvc.perform(post("/api/v1/auth/registrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = LoginRequest.builder()
                .email("login@example.com")
                .senha("senhaLogin123")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("login@example.com"))
                .andExpect(jsonPath("$.perfil").value("COLLABORATOR"));
    }

    @Test
    void deveRetornarUnauthorizedComCredenciaisInvalidas() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("naoexiste@example.com")
                .senha("senhaInvalida")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
