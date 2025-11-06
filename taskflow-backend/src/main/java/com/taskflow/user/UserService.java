package com.taskflow.user;

import com.taskflow.security.JwtService;
import com.taskflow.user.dto.LoginRequest;
import com.taskflow.user.dto.RegistroRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User registrarUsuario(RegistroRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailJaRegistradoException("Email já registrado: " + request.getEmail());
        }

        var usuario = User.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .hashSenha(passwordEncoder.encode(request.getSenha()))
                .perfil("COLLABORATOR") // Perfil padrão
                .build();
        return userRepository.save(usuario);
    }

    public String autenticarUsuario(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );
        var usuario = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")); // Deveria ser tratado por AuthenticationManager
        return jwtService.generateToken(usuario);
    }
}
