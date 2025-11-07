package com.taskflow.feedback;

import com.taskflow.feedback.dto.FeedbackRequestDto;
import com.taskflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Transactional
    public Feedback saveFeedback(FeedbackRequestDto feedbackRequestDto, User user) {
        Feedback feedback = Feedback.builder()
                .message(feedbackRequestDto.getMessage())
                .context(feedbackRequestDto.getContext())
                .user(user)
                .build();
        return feedbackRepository.save(feedback);
    }
}
