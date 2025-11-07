package com.taskflow.feedback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.feedback.dto.FeedbackRequestDto;
import com.taskflow.user.Role;
import com.taskflow.user.User;
import com.taskflow.user.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FeedbackControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("Feedback User")
                .email("feedback@example.com")
                .passwordHash("password")
                .role(Role.COLLABORATOR)
                .build());
    }

    @Test
    @WithMockUser(username = "feedback@example.com")
    void submitFeedback_shouldReturnCreated() throws Exception {
        FeedbackRequestDto request = new FeedbackRequestDto();
        request.setMessage("This is a test feedback message.");
        request.setContext("Dashboard Page");

        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Feedback savedFeedback = feedbackRepository.findAll().get(0);
        assertThat(savedFeedback).isNotNull();
        assertThat(savedFeedback.getMessage()).isEqualTo("This is a test feedback message.");
        assertThat(savedFeedback.getContext()).isEqualTo("Dashboard Page");
        assertThat(savedFeedback.getUser().getEmail()).isEqualTo("feedback@example.com");
    }

    @Test
    void submitFeedback_whenNotAuthenticated_shouldReturnForbidden() throws Exception {
        FeedbackRequestDto request = new FeedbackRequestDto();
        request.setMessage("This should not be saved.");

        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
