package com.taskflow.user;

import com.taskflow.security.JwtService;
import com.taskflow.user.dto.AuthResponse;
import com.taskflow.user.dto.LoginRequest;
import com.taskflow.user.dto.RegistroRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private RegistroRequest registroRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registroRequest = RegistroRequest.builder()
                .name("Teste")
                .email("teste@example.com")
                .password("senha123")
                .build();

        loginRequest = LoginRequest.builder()
                .email("teste@example.com")
                .password("senha123")
                .build();

        user = User.builder()
                .id(1L)
                .name("Teste")
                .email("teste@example.com")
                .passwordHash("hashedPassword")
                .role(Role.COLLABORATOR)
                .build();
    }

    @Test
    void deveRegistrarNovoUsuarioComSucesso() {
        when(userRepository.findByEmail(registroRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registroRequest.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtTokenGerado");

        AuthResponse resultado = userService.registrarUsuario(registroRequest);

        assertNotNull(resultado);
        assertEquals("jwtTokenGerado", resultado.getToken());
        assertEquals("teste@example.com", resultado.getEmail());
        assertEquals("COLLABORATOR", resultado.getRole());
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaRegistrado() {
        when(userRepository.findByEmail(registroRequest.getEmail())).thenReturn(Optional.of(user));

        assertThrows(EmailJaRegistradoException.class, () -> userService.registrarUsuario(registroRequest));
        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    void deveAutenticarUsuarioComSucessoERetornarToken() {
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwtTokenGerado");

        String token = userService.autenticarUsuario(loginRequest);

        assertNotNull(token);
        assertEquals("jwtTokenGerado", token);
        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoNaAutenticacao() {
        doThrow(new RuntimeException("Bad credentials"))
                .when(authenticationManager).authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );

        assertThrows(RuntimeException.class, () -> userService.autenticarUsuario(loginRequest));
    }

    @Test
    void deveEncontrarUsuarioPorEmailComSucesso() {
        when(userRepository.findByEmail("teste@example.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserByEmail("teste@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("teste@example.com", foundUser.get().getEmail());
    }

    @Test
    void naoDeveEncontrarUsuarioPorEmailInexistente() {
        when(userRepository.findByEmail("naoexiste@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findUserByEmail("naoexiste@example.com");

        assertFalse(foundUser.isPresent());
    }
}
