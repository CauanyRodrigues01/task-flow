package com.taskflow.user;

import com.taskflow.security.JwtService;
import com.taskflow.user.dto.UserCreationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

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

    private User adminUser;
    private User collaboratorUser;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .id(1L)
                .name("Admin User")
                .email("admin@example.com")
                .passwordHash("hashed_password")
                .role(Role.ADMIN)
                .build();

        collaboratorUser = User.builder()
                .id(2L)
                .name("Collaborator User")
                .email("collaborator@example.com")
                .passwordHash("hashed_password")
                .role(Role.COLLABORATOR)
                .build();
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(adminUser, collaboratorUser));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(adminUser.getEmail(), users.get(0).getEmail());
    }

    @Test
    void inviteUser_shouldCreateNewUser() {
        UserCreationRequest request = new UserCreationRequest("New User", "newuser@example.com", "password", Role.COLLABORATOR);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed_password");
        User newUserToReturn = User.builder()
                .id(3L)
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash("hashed_password")
                .role(request.getRole())
                .build();
        when(userRepository.save(any(User.class))).thenReturn(newUserToReturn);

        User newUser = userService.criarUsuario(request);

        assertNotNull(newUser);
        assertEquals(request.getEmail(), newUser.getEmail());
        assertEquals(Role.COLLABORATOR, newUser.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void inviteUser_withExistingEmail_shouldThrowException() {
        UserCreationRequest request = new UserCreationRequest("Admin User", "admin@example.com", "password", Role.COLLABORATOR);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(adminUser));

        assertThrows(EmailJaRegistradoException.class, () -> userService.criarUsuario(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void removeUser_shouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.removeUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void removeUser_withNonExistingUser_shouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.removeUser(1L));
        verify(userRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void updateUserRole_shouldUpdateUserRole() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(collaboratorUser));
        when(userRepository.save(any(User.class))).thenReturn(adminUser); // Simulating collaborator becoming admin

        User updatedUser = userService.updateUserRole(2L, Role.ADMIN);

        assertNotNull(updatedUser);
        assertEquals(Role.ADMIN, updatedUser.getRole());
        verify(userRepository, times(1)).save(collaboratorUser);
    }

    @Test
    void updateUserRole_withNonExistingUser_shouldThrowException() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUserRole(1L, Role.ADMIN));
        verify(userRepository, never()).save(any(User.class));
    }
}