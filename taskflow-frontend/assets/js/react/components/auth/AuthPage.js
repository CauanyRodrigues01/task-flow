const { useState } = React;
const e = React.createElement;

const AuthPage = () => {
    const [showLogin, setShowLogin] = useState(true);

    return showLogin ? 
        e(LoginForm, { onSwitchToRegister: () => setShowLogin(false) }) : 
        e(RegisterForm, { onSwitchToLogin: () => setShowLogin(true) });
};

const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(AuthPage));