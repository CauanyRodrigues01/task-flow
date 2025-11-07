package com.taskflow.feedback;

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
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        if (userRepository.findByEmail("feedback@example.com").isEmpty()) {
            User u = new User();
            u.setEmail("feedback@example.com");
            u.setName("Feedback User");
            u.setPasswordHash("123");
            u.setRole(Role.COLLABORATOR);
            userRepository.save(u);
        }
    }

    @Test
    @WithMockUser(username = "feedback@example.com", roles = {"COLLABORATOR"})
    void submitFeedback_shouldReturnCreated() throws Exception {

        String json = """
        {
            "message": "Gostei muito da plataforma!",
            "rating": 5
        }
        """;

        mockMvc.perform(post("/api/v1/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}
