const FeedbackButton = () => {
    const { useState } = React;
    const e = React.createElement;

    const [showModal, setShowModal] = useState(false);

    return e(React.Fragment, null,
        e('button', {
            className: 'btn feedback-button',
            onClick: () => setShowModal(true)
        }, 'Feedback'),
        showModal && e(FeedbackModal, {
            onClose: () => setShowModal(false)
        })
    );
};
