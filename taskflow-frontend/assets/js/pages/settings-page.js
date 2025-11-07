(() => {
    'use strict';

    const e = React.createElement;

    document.addEventListener('DOMContentLoaded', () => {
        // Dark Mode Toggle Logic
        const darkModeToggle = document.getElementById('dark-mode-toggle');
        if (darkModeToggle) {
            // Read current state from documentElement
            darkModeToggle.checked = document.documentElement.classList.contains('dark-mode');

            darkModeToggle.addEventListener('change', () => {
                if (darkModeToggle.checked) {
                    document.documentElement.classList.add('dark-mode');
                    localStorage.setItem('theme', 'dark');
                } else {
                    document.documentElement.classList.remove('dark-mode');
                    localStorage.setItem('theme', 'light');
                }
            });
        }

        // Render User Profile Preview
        const userProfileRoot = document.getElementById('user-profile-root');
        if (userProfileRoot && window.UserProfilePreview) {
            const root = ReactDOM.createRoot(userProfileRoot);
            root.render(e(window.UserProfilePreview));
        }
    });
})();
