const { useState } = React;

const e = React.createElement;

const LoginForm = ({ onSwitchToRegister }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [keepLoggedIn, setKeepLoggedIn] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const data = await window.apiClient.post('/api/v1/auth/login', { email, password });
            window.storage.saveTokens(data.accessToken, data.refreshToken);
            window.notificationService.show('Login bem-sucedido!');
            window.location.href = 'index.html';
        } catch (error) {
            window.notificationService.show(`Erro no login: ${error.message}`, 'error');
        }
    };

    return e('div', { className: 'auth-box' },
        e('h1', null, 'Login'),
        e('form', { onSubmit: handleSubmit },
            e('div', { className: 'form-group' },
                e('input', { type: 'email', placeholder: 'Email', value: email, onChange: (ev) => setEmail(ev.target.value), required: true })
            ),
            e('div', { className: 'form-group' },
                e('input', { type: 'password', placeholder: 'Senha', value: password, onChange: (ev) => setPassword(ev.target.value), required: true })
            ),
            e('div', { className: 'auth-options' },
                e('label', null,
                    e('input', { type: 'checkbox', checked: keepLoggedIn, onChange: (ev) => setKeepLoggedIn(ev.target.checked) }),
                    ' Manter-me logado'
                ),
                e('a', { href: '#' }, 'Recuperar senha')
            ),
            e('button', { type: 'submit', className: 'btn' }, 'Entrar'),
            e('div', { className: 'auth-links' },
                e('a', { href: '#', onClick: onSwitchToRegister }, 'Não tem uma conta? Registre-se')
            )
        )
    );
};

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

const AuthPage = () => {
    const [showLogin, setShowLogin] = useState(true);

    return showLogin ? 
        e(LoginForm, { onSwitchToRegister: () => setShowLogin(false) }) : 
        e(RegisterForm, { onSwitchToLogin: () => setShowLogin(true) });
};

const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(AuthPage));