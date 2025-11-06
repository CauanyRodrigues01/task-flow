package com.taskflow.user;

import com.taskflow.security.JwtService;
import com.taskflow.user.dto.AuthResponse;
import com.taskflow.user.dto.LoginRequest;
import com.taskflow.user.dto.RegistroRequest;
import com.taskflow.user.dto.UserCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrarUsuario(RegistroRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailJaRegistradoException("Email já registrado: " + request.getEmail());
        }

        var usuario = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.COLLABORATOR) // Perfil padrão
                .build();
        User savedUser = userRepository.save(usuario);
        var jwtToken = jwtService.generateToken(savedUser);
        return AuthResponse.builder()
                .token(jwtToken)
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();
    }

    public String autenticarUsuario(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var usuario = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado")); // Deveria ser tratado por AuthenticationManager
        return jwtService.generateToken(usuario);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User inviteUser(UserCreationRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailJaRegistradoException("Email já registrado: " + request.getEmail());
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.COLLABORATOR)
                .build();
        return userRepository.save(newUser);
    }

    @Transactional
    public void removeUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Usuário não encontrado com ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public User updateUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com ID: " + userId));
        user.setRole(newRole);
        return userRepository.save(user);
    }
}

