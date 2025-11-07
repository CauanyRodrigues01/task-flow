package com.taskflow.user;

import com.taskflow.user.dto.AuthResponse;
import com.taskflow.user.dto.LoginRequest;
import com.taskflow.user.dto.RegistroRequest;
import com.taskflow.user.dto.RefreshTokenRequest;
import com.taskflow.user.dto.RefreshTokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro, login e renovação de token")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Registra um novo usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou email já registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        AuthResponse authResponse = userService.registrarUsuario(request);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Autentica um usuário e retorna tokens de acesso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.autenticarUsuario(request));
    }

    @Operation(summary = "Gera um novo token de acesso usando um refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token de acesso renovado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Refresh token inválido ou expirado")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return userService.refreshToken(request.getRefreshToken())
                .map(newAccessToken -> ResponseEntity.ok(new RefreshTokenResponse(newAccessToken)))
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
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
