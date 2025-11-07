const { useState } = React;
const e = React.createElement;

const RegisterForm = ({ onSwitchToLogin }) => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const data = await window.apiClient.post('/api/v1/auth/register', { name, email, password });
            window.storage.saveTokens(data.accessToken, data.refreshToken);
            window.notificationService.show('Registro bem-sucedido!');
            window.location.href = 'index.html';
        } catch (error) {
            window.notificationService.show(`Erro no registro: ${error.message}`, 'error');
        }
    };

    return e('div', { className: 'auth-box' },
        e('h1', null, 'Registro'),
        e('form', { onSubmit: handleSubmit },
            e('div', { className: 'form-group' },
                e('input', { type: 'text', placeholder: 'Nome', value: name, onChange: (ev) => setName(ev.target.value), required: true })
            ),
            e('div', { className: 'form-group' },
                e('input', { type: 'email', placeholder: 'Email', value: email, onChange: (ev) => setEmail(ev.target.value), required: true })
            ),
            e('div', { className: 'form-group' },
                e('input', { type: 'password', placeholder: 'Senha', value: password, onChange: (ev) => setPassword(ev.target.value), required: true })
            ),
            e('button', { type: 'submit', className: 'btn' }, 'Registrar'),
            e('div', { className: 'auth-links' },
                e('a', { href: '#', onClick: onSwitchToLogin }, 'Já tem uma conta? Faça login')
            )
        )
    );
};