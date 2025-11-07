(() => {
    // Apply saved theme immediately on script load to prevent FOUC (Flash of Unstyled Content)
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.documentElement.classList.add('dark-mode');
    } else {
        document.documentElement.classList.remove('dark-mode');
    }
})();

document.addEventListener('DOMContentLoaded', () => {
    const hamburgerMenu = document.getElementById('hamburger-menu');
    const dropdownMenu = document.getElementById('dropdown-menu');
    const logoutButton = document.getElementById('logout-button');
    const feedbackRoot = document.getElementById('feedback-root'); // Get feedback root

    if (hamburgerMenu && dropdownMenu) {
        hamburgerMenu.addEventListener('click', () => {
            const isVisible = dropdownMenu.style.display === 'block';
            dropdownMenu.style.display = isVisible ? 'none' : 'block';
        });
    }

    if (logoutButton) {
        logoutButton.addEventListener('click', (e) => {
            e.preventDefault();
            window.storage.removeToken();
            window.notificationService.show('Você foi desconectado.');
            window.location.href = 'auth.html';
        });
    }

    // Render FeedbackButton if feedbackRoot exists
    if (feedbackRoot) {
        // Dynamically load React components for feedback
        const scriptFeedbackService = document.createElement('script');
        scriptFeedbackService.src = 'assets/js/react/feedback/feedbackService.js';
        document.body.appendChild(scriptFeedbackService);

        const scriptFeedbackModal = document.createElement('script');
        scriptFeedbackModal.src = 'assets/js/react/feedback/FeedbackModal.js';
        document.body.appendChild(scriptFeedbackModal);

        const scriptFeedbackButton = document.createElement('script');
        scriptFeedbackButton.src = 'assets/js/react/feedback/FeedbackButton.js';
        scriptFeedbackButton.onload = () => {
            const feedbackReactRoot = ReactDOM.createRoot(feedbackRoot);
            feedbackReactRoot.render(React.createElement(FeedbackButton));
        };
        document.body.appendChild(scriptFeedbackButton);
    }
});
