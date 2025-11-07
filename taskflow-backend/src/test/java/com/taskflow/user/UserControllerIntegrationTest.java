package com.taskflow.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User admin;
    private User collaborator;

    @BeforeEach
    void setUp() {
        admin = userRepository.save(User.builder().name("Admin").email("admin.user@example.com").role(Role.ADMIN).passwordHash("pass").build());
        collaborator = userRepository.save(User.builder().name("Collaborator").email("collab.user@example.com").role(Role.COLLABORATOR).passwordHash("pass").build());
    }

    @Test
    @WithMockUser(username = "admin.user@example.com", roles = {"ADMIN"})
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "collab.user@example.com", roles = {"COLLABORATOR"})
    void getAllUsers_whenNotAdmin_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin.user@example.com", roles = {"ADMIN"})
    void updateUserRole_shouldChangeUserRole() throws Exception {
        mockMvc.perform(put("/api/v1/users/{userId}/role", collaborator.getId())
                        .param("newRole", "MANAGER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("MANAGER"));
    }

    @Test
    @WithMockUser(username = "admin.user@example.com", roles = {"ADMIN"})
    void deleteUser_shouldRemoveUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{userId}", collaborator.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
