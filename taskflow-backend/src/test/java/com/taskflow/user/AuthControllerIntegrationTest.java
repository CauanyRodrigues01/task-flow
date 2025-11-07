package com.taskflow.user;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.user.dto.LoginRequest;
import com.taskflow.user.dto.RegistroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.taskflow.BaseIntegrationTest;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration"})
class AuthControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // userRepository.deleteAll(); // Limpa o banco de dados antes de cada teste
    }

    @Test
    void deveRegistrarNovoUsuarioComSucesso() throws Exception {
        RegistroRequest request = RegistroRequest.builder()
                .name("Novo Usuário")
                .email("novo@example.com")
                .password("senhaSegura123")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("novo@example.com"))
                .andExpect(jsonPath("$.role").value("COLLABORATOR"));
    }

    @Test
    void deveRetornarBadRequestQuandoEmailJaRegistrado() throws Exception {
        // Registrar um usuário primeiro via endpoint
        RegistroRequest request = RegistroRequest.builder()
                .name("Usuario Existente")
                .email("existente@example.com")
                .password("senhaExistente")
                .build();
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Tentar registrar com o mesmo email
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAutenticarUsuarioComSucessoERetornarToken() throws Exception {
        // Registrar um usuário para autenticar via endpoint
        RegistroRequest registroRequest = RegistroRequest.builder()
                .name("Usuario Login")
                .email("login@example.com")
                .password("senhaLogin123")
                .build();
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = LoginRequest.builder()
                .email("login@example.com")
                .password("senhaLogin123")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("login@example.com"))
                .andExpect(jsonPath("$.role").value("COLLABORATOR"));
    }

    @Test
    void deveRetornarUnauthorizedComCredenciaisInvalidas() throws Exception {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("naoexiste@example.com")
                .password("senhaInvalida")
                .build();

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornarBadRequestParaRegistroComDadosInvalidos() throws Exception {
        RegistroRequest request = RegistroRequest.builder()
                .name("No") // Nome muito curto
                .email("emailinvalido") // Email mal formatado
                .password("123") // Senha muito curta
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("O nome deve ter entre 3 e 100 caracteres"))
                .andExpect(jsonPath("$.email").value("Formato de email inválido"))
                .andExpect(jsonPath("$.password").value("A senha deve ter pelo menos 8 caracteres"));
    }
}
