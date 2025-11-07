(() => {
    // Garante que as constantes do React estejam disponíveis neste escopo
    const { useState, useEffect } = React;
    const e = React.createElement;

    const Notification = ({ message, type, onDone }) => {
        useEffect(() => {
            const timer = setTimeout(() => {
                onDone();
            }, 3000); // A notificação desaparece após 3 segundos

            return () => clearTimeout(timer);
        }, [onDone]);

        return e('div', { className: `notification ${type}` }, message);
    };

    const NotificationContainer = () => {
        const [notifications, setNotifications] = useState([]);

        const show = (message, type = 'success') => {
            const id = Date.now();
            setNotifications(prev => [...prev, { id, message, type }]);
        };

        const remove = (id) => {
            setNotifications(prev => prev.filter(n => n.id !== id));
        };

        useEffect(() => {
            window.notificationService = { show };
            return () => { delete window.notificationService; }; // Limpeza
        }, []);

        return e('div', { className: 'notification-container' },
            notifications.map(n => e(Notification, { key: n.id, ...n, onDone: () => remove(n.id) }))
        );
    };

    // Renderiza o container de notificações em um div dedicado no corpo do documento
    const notificationRoot = document.createElement('div');
    notificationRoot.id = 'notification-root';
    document.body.appendChild(notificationRoot);

    const root = ReactDOM.createRoot(notificationRoot);
    root.render(e(NotificationContainer));
})();