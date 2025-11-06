const { useState } = React;

const e = React.createElement;

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const data = await window.apiClient.post('/api/v1/auth/login', { email, password });
            window.storage.setToken(data.token);
            alert('Login bem-sucedido!');
            window.location.href = 'projects.html';
        } catch (error) {
            alert(`Erro no login: ${error.message}`);
        }
    };

    return e('form', { onSubmit: handleSubmit },
        e('h1', null, 'Login'),
        e('input', { type: 'email', placeholder: 'Email', value: email, onChange: (ev) => setEmail(ev.target.value), required: true }),
        e('input', { type: 'password', placeholder: 'Senha', value: password, onChange: (ev) => setPassword(ev.target.value), required: true }),
        e('button', { type: 'submit' }, 'Entrar')
    );
};

const RegisterForm = () => {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const data = await window.apiClient.post('/api/v1/auth/register', { name, email, password });
            window.storage.setToken(data.token);
            alert('Registro bem-sucedido!');
            window.location.href = 'projects.html';
        } catch (error) {
            alert(`Erro no registro: ${error.message}`);
        }
    };

    return e('form', { onSubmit: handleSubmit },
        e('h1', null, 'Registro'),
        e('input', { type: 'text', placeholder: 'Nome', value: name, onChange: (ev) => setName(ev.target.value), required: true }),
        e('input', { type: 'email', placeholder: 'Email', value: email, onChange: (ev) => setEmail(ev.target.value), required: true }),
        e('input', { type: 'password', placeholder: 'Senha', value: password, onChange: (ev) => setPassword(ev.target.value), required: true }),
        e('button', { type: 'submit' }, 'Registrar')
    );
};

const AuthPage = () => {
    const [showLogin, setShowLogin] = useState(true);

    const toggleForm = (e) => {
        e.preventDefault();
        setShowLogin(!showLogin);
    }

    if (showLogin) {
        return e('div', null, 
            e(LoginForm),
            e('p', null, 'Não tem uma conta? ', e('a', { href: '#', onClick: toggleForm }, 'Registre-se'))
        );
    }

    return e('div', null, 
        e(RegisterForm),
        e('p', null, 'Já tem uma conta? ', e('a', { href: '#', onClick: toggleForm }, 'Faça login'))
    );
}

const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(AuthPage));
