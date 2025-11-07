const FeedbackModal = ({ onClose }) => {
    const { useState } = React;
    const e = React.createElement;

    const [message, setMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        setError(null);
        setSuccess(false);

        const context = window.location.pathname; // Capture current page as context

        try {
            await feedbackService.sendFeedback(message, context);
            setSuccess(true);
            setMessage('');
            window.notificationService.show('Feedback enviado com sucesso! Obrigado.');
            onClose();
        } catch (err) {
            setError(err);
            window.notificationService.show('Erro ao enviar feedback.', 'error');
        } finally {
            setLoading(false);
        }
    };

    return e('div', { className: 'modal-overlay' },
        e('div', { className: 'modal-content' },
            e('h3', null, 'Enviar Feedback'),
            e('form', { onSubmit: handleSubmit },
                e('div', { className: 'form-group' },
                    e('label', { htmlFor: 'feedbackMessage' }, 'Sua mensagem:'),
                    e('textarea', {
                        id: 'feedbackMessage',
                        value: message,
                        onChange: (ev) => setMessage(ev.target.value),
                        rows: 5,
                        required: true,
                        placeholder: 'Descreva sua experiência ou sugestão...'
                    })
                ),
                error && e('p', { className: 'error-message' }, error.message || 'Erro desconhecido'),
                e('div', { className: 'modal-actions' },
                    e('button', { type: 'submit', className: 'btn', disabled: loading }, loading ? 'Enviando...' : 'Enviar'),
                    e('button', { type: 'button', className: 'btn btn-secondary', onClick: onClose, disabled: loading }, 'Cancelar')
                )
            )
        )
    );
};
