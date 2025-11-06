package com.taskflow.user;

import com.taskflow.user.dto.AuthResponse;
import com.taskflow.user.dto.LoginRequest;
import com.taskflow.user.dto.RegistroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@RequestBody RegistroRequest request) {
        AuthResponse authResponse = userService.registrarUsuario(request);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String jwt = userService.autenticarUsuario(request);
        User usuario = userService.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado após autenticação"));
        return ResponseEntity.ok(AuthResponse.builder()
                .token(jwt)
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build());
    }

    @ExceptionHandler(EmailJaRegistradoException.class)
    public ResponseEntity<String> handleEmailJaRegistradoException(EmailJaRegistradoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAuthenticationException(Exception ex) {
        return new ResponseEntity<>("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
