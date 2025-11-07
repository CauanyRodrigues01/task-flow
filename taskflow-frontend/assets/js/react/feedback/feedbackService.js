const feedbackService = (() => {
    const sendFeedback = async (message, context) => {
        try {
            const response = await window.apiClient.post('/api/v1/feedback', { message, context });
            return response;
        } catch (error) {
            console.error('Error sending feedback:', error);
            throw error;
        }
    };

    return {
        sendFeedback,
    };
})();
