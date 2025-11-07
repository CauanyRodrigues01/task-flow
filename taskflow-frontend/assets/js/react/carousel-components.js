const { useState, useEffect } = React;
const e = React.createElement;

const TaskCarousel = ({ projectId }) => {
    const [tasks, setTasks] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTasks = async () => {
            setLoading(true);
            setError(null);
            try {
                const fetchedTasks = await window.apiClient.get(`/api/v1/projects/${projectId}/tasks`);
                setTasks(fetchedTasks);
            } catch (err) {
                console.error('Erro detalhado ao carregar tarefas:', err);
                setError(`Não foi possível carregar as tarefas. Causa: ${err.message}`);
            } finally {
                setLoading(false);
            }
        };
        fetchTasks();
    }, [projectId]);

    const handlePrev = () => {
        setCurrentIndex((prevIndex) => (prevIndex === 0 ? tasks.length - 1 : prevIndex - 1));
    };

    const handleNext = () => {
        setCurrentIndex((prevIndex) => (prevIndex === tasks.length - 1 ? 0 : prevIndex + 1));
    };

    if (loading) {
        return e('p', null, 'Carregando tarefas...');
    }

    if (error) {
        return e('div', { className: 'error-message' },
            e('h3', null, 'Erro'),
            e('p', null, error)
        );
    }

    if (tasks.length === 0) {
        return e('p', null, 'Nenhuma tarefa encontrada para este projeto.');
    }

    const task = tasks[currentIndex];

    return e('div', { className: 'carousel-container' },
        e('button', { onClick: handlePrev, className: 'carousel-btn' }, '‹'),
        e('div', { className: 'carousel-slide' },
            e('h3', null, task.title),
            e('p', null, task.description),
            e('ul', null,
                e('li', { className: `status-${task.status}` }, `Status: ${task.status}`),
                e('li', { className: `priority-${task.priority}` }, `Prioridade: ${task.priority}`),
                e('li', null, `Prazo: ${task.dueDate ? new Date(task.dueDate).toLocaleDateString() : 'N/A'}`),
                e('li', null, `Atribuído a: ${task.assigneeName || 'Ninguém'}`)
            )
        ),
        e('button', { onClick: handleNext, className: 'carousel-btn' }, '›')
    );
};

// Torna o componente acessível globalmente
window.TaskCarousel = TaskCarousel;
