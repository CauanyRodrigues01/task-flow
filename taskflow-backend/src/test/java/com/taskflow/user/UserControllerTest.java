package com.taskflow.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.security.JwtService;
import com.taskflow.security.SecurityConfig;
import com.taskflow.user.dto.UserCreationRequest;
import com.taskflow.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.context.annotation.Import;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private UserRepository userRepository; // Add this line

    @Autowired
    private ObjectMapper objectMapper;

    private User adminUser;
    private User collaboratorUser;
    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .id(1L)
                .name("Admin User")
                .email("admin@example.com")
                .role(Role.ADMIN)
                .build();

        collaboratorUser = User.builder()
                .id(2L)
                .name("Collaborator User")
                .email("collaborator@example.com")
                .role(Role.COLLABORATOR)
                .build();

        userCreationRequest = new UserCreationRequest("New User", "newuser@example.com", "password", Role.COLLABORATOR);
        userResponse = new UserResponse(2L, "New User", "newuser@example.com", Role.COLLABORATOR);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        List<User> users = Arrays.asList(adminUser, collaboratorUser);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(adminUser.getEmail()))
                .andExpect(jsonPath("$[1].email").value(collaboratorUser.getEmail()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void inviteUser_shouldReturnCreatedUser() throws Exception {
        when(userService.inviteUser(any(UserCreationRequest.class))).thenReturn(collaboratorUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(collaboratorUser.getEmail()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void updateUserRole_shouldReturnUpdatedUser() throws Exception {
        User updatedUser = User.builder()
                .id(2L)
                .name("Collaborator User")
                .email("collaborator@example.com")
                .role(Role.ADMIN)
                .build();
        when(userService.updateUserRole(eq(2L), eq(Role.ADMIN))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/{userId}/role", 2L)
                        .param("newRole", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value(Role.ADMIN.name()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void removeUser_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).removeUser(any(Long.class));

        mockMvc.perform(delete("/api/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = {"COLLABORATOR"})
    void getAllUsers_asCollaborator_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
