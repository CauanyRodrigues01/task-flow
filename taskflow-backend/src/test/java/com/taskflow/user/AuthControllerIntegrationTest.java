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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.taskflow.TestDatabaseConfig;
import org.springframework.context.annotation.Import;

import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
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
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void deveRetornarBadRequestQuandoEmailJaRegistrado() throws Exception {
        userRepository.save(User.builder()
                .name("Usuario Existente")
                .email("existente@example.com")
                .passwordHash(passwordEncoder.encode("senhaExistente"))
                .role(Role.COLLABORATOR)
                .build());

        RegistroRequest request = RegistroRequest.builder()
                .name("Usuario Existente")
                .email("existente@example.com")
                .password("senhaExistente")
                .build();

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAutenticarUsuarioComSucessoERetornarToken() throws Exception {
        userRepository.save(User.builder()
                .name("Usuario Login")
                .email("login@example.com")
                .passwordHash(passwordEncoder.encode("senhaLogin123"))
                .role(Role.COLLABORATOR)
                .build());

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
}

