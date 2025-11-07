const MyFocusPage = () => {
    const { useState, useEffect } = React;
    const e = React.createElement;

    const [sortBy, setSortBy] = useState('dueDate');
    const [order, setOrder] = useState('asc');

    const { tasks, loading, error } = useMyFocusTasks(sortBy, order);

    const handleStatusChange = async (taskId, newStatus) => {
        try {
            await window.apiClient.patch(`/api/v1/projects/0/tasks/${taskId}`, { status: newStatus }); // projectId is not relevant for my-focus endpoint, but required by patch
            window.notificationService.show('Status da tarefa atualizado com sucesso!');
            // Re-fetch tasks to reflect changes
            // This would ideally be handled by a state update in useMyFocusTasks or a direct re-fetch
            // For simplicity, we'll just show notification and assume backend updates
        } catch (err) {
            console.error('Erro ao atualizar status da tarefa:', err);
            window.notificationService.show('Não foi possível atualizar o status da tarefa.', 'error');
        }
    };

    const handleCommentAdd = async (taskId, commentContent) => {
        try {
            await window.apiClient.post(`/api/v1/tasks/${taskId}/comments`, { content: commentContent });
            window.notificationService.show('Comentário adicionado com sucesso!');
            // Re-fetch tasks or update task state to show new comment
        } catch (err) {
            console.error('Erro ao adicionar comentário:', err);
            window.notificationService.show('Não foi possível adicionar o comentário.', 'error');
        }
    };

    if (loading) {
        return e('div', null, 'Carregando suas tarefas...');
    }

    if (error) {
        return e('div', null, e('h2', null, `Erro ao carregar tarefas: ${error.message || 'Erro desconhecido'}`));
    }

    return e('div', { className: 'my-focus-page' },
        e('h2', null, 'Meu Foco'),
        e('div', { className: 'sort-options' },
            e('label', { htmlFor: 'sortBy' }, 'Ordenar por:'),
            e('select', { value: sortBy, onChange: (e) => setSortBy(e.target.value) },
                e('option', { value: 'dueDate' }, 'Data de Vencimento'),
                e('option', { value: 'priority' }, 'Prioridade')
            ),
            e('select', { value: order, onChange: (e) => setOrder(e.target.value) },
                e('option', { value: 'asc' }, 'Crescente'),
                e('option', { value: 'desc' }, 'Decrescente')
            )
        ),
        e('div', { className: 'my-focus-task-list' },
            tasks.length > 0 ? tasks.map(task => e(MyFocusTaskCard, {
                key: task.id,
                task: task,
                onStatusChange: handleStatusChange,
                onCommentAdd: handleCommentAdd
            })) : e('p', null, 'Nenhuma tarefa no seu foco.')
        )
    );
};
