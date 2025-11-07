const MyFocusTaskCard = ({ task, onStatusChange, onCommentAdd }) => {
    const { useState } = React;
    const e = React.createElement;

    const [showCommentInput, setShowCommentInput] = useState(false);
    const [newComment, setNewComment] = useState('');

    const handleStatusChange = (event) => {
        onStatusChange(task.id, event.target.value);
    };

    const handleAddComment = () => {
        if (newComment.trim()) {
            onCommentAdd(task.id, newComment);
            setNewComment('');
            setShowCommentInput(false);
        }
    };

    const statusClass = `status-${task.status}`;
    const priorityClass = `priority-${task.priority}`;

    return e('div', { className: 'my-focus-task-card' },
        e('h3', null, task.title),
        e('p', { className: 'task-description' }, task.description),
        e('div', { className: 'task-meta' },
            e('span', { className: statusClass }, task.status),
            e('span', { className: priorityClass }, task.priority),
            task.dueDate && e('span', null, `Vencimento: ${new Date(task.dueDate).toLocaleDateString()}`)
        ),
        e('div', { className: 'task-actions' },
            e('select', { value: task.status, onChange: handleStatusChange },
                e('option', { value: 'TODO' }, 'A Fazer'),
                e('option', { value: 'IN_PROGRESS' }, 'Em Progresso'),
                e('option', { value: 'DONE' }, 'Concluída'),
                e('option', { value: 'BLOCKED' }, 'Bloqueada')
            ),
            e('button', { onClick: () => setShowCommentInput(!showCommentInput), className: 'btn btn-secondary' }, 'Comentar')
        ),
        showCommentInput && e('div', { className: 'comment-input-section' },
            e('textarea', {
                value: newComment,
                onChange: (e) => setNewComment(e.target.value),
                placeholder: 'Adicionar um comentário...',
                rows: 3
            }),
            e('button', { onClick: handleAddComment, className: 'btn' }, 'Adicionar Comentário')
        )
    );
};
