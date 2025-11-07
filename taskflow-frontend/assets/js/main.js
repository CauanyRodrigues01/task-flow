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
});