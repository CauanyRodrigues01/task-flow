document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const showRegisterLink = document.getElementById('show-register');
    const showLoginLink = document.getElementById('show-login');

    // Lógica para alternar entre formulários
    if (showRegisterLink) {
        showRegisterLink.addEventListener('click', (e) => {
            e.preventDefault();
            if (loginForm) loginForm.style.display = 'none';
            if (registerForm) registerForm.style.display = 'block';
        });
    }

    if (showLoginLink) {
        showLoginLink.addEventListener('click', (e) => {
            e.preventDefault();
            if (registerForm) registerForm.style.display = 'none';
            if (loginForm) loginForm.style.display = 'block';
        });
    }

    // Lógica de Registro
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const name = registerForm.querySelector('input[type="text"]').value;
            const email = registerForm.querySelector('input[type="email"]').value;
            const password = registerForm.querySelector('input[type="password"]').value;

            try {
                const data = await window.apiClient.post('/api/v1/auth/register', { name, email, password });
                window.storage.setToken(data.token);
                alert('Registro bem-sucedido!');
                window.location.href = 'projects.html'; // Redirecionar após registro
            } catch (error) {
                alert(`Erro no registro: ${error.message}`);
            }
        });
    }

    // Lógica de Login
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = loginForm.querySelector('input[type="email"]').value;
            const password = loginForm.querySelector('input[type="password"]').value;

            try {
                const data = await window.apiClient.post('/api/v1/auth/login', { email, password });
                window.storage.setToken(data.token);
                alert('Login bem-sucedido!');
                window.location.href = 'projects.html'; // Redirecionar após login
            } catch (error) {
                alert(`Erro no login: ${error.message}`);
            }
        });
    }
});