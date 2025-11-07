const { useState, useEffect } = React;

const e = React.createElement;

// Componente para o formulário de criação/edição de tarefas
const TaskForm = ({ projectId, onTaskCreated, initialData = {}, isEditing = false }) => {
    const [title, setTitle] = useState(initialData.title || '');
    const [description, setDescription] = useState(initialData.description || '');
    const [assigneeId, setAssigneeId] = useState(initialData.assigneeId || '');
    const [status, setStatus] = useState(initialData.status || 'TODO');
    const [priority, setPriority] = useState(initialData.priority || 'LOW');
    const [dueDate, setDueDate] = useState(initialData.dueDate ? initialData.dueDate.split('T')[0] : '');
    const [members, setMembers] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchMembers = async () => {
            try {
                const response = await window.apiClient.get(`/api/v1/projects/${projectId}/members`);
                console.log('Membros do projeto buscados:', response);
                setMembers(response);
                if (response.length > 0 && !assigneeId) {
                    setAssigneeId(response[0].id); // Set first member as default assignee if none selected
                }
            } catch (err) {
                console.error('Erro ao buscar membros do projeto:', err);
                setError('Não foi possível carregar os membros do projeto.');
            }
        };
        if (projectId) {
            fetchMembers();
        }
    }, [projectId, assigneeId]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError(null);

        const taskData = {
            title,
            description,
            assigneeId: assigneeId ? parseInt(assigneeId) : null,
            status,
            priority,
            dueDate: dueDate ? `${dueDate}T00:00:00Z` : null
        };

        try {
            let resultTask;
            if (isEditing) {
                resultTask = await window.apiClient.put(`/api/v1/projects/${projectId}/tasks/${initialData.id}`, taskData);
                window.notificationService.show('Tarefa atualizada com sucesso!');
            } else {
                resultTask = await window.apiClient.post(`/api/v1/projects/${projectId}/tasks`, taskData);
                window.notificationService.show('Tarefa criada com sucesso!');
            }
            if (onTaskCreated) {
                onTaskCreated(resultTask);
            }
            // Clear form if creating, or close modal if editing
            if (!isEditing) {
                setTitle('');
                setDescription('');
                setAssigneeId(members.length > 0 ? members[0].id : '');
                setStatus('TO_DO');
                setPriority('LOW');
                setDueDate('');
            }
        } catch (err) {
            console.error('Erro ao salvar tarefa:', err);
            window.notificationService.show(err.message, 'error');
        }
    };

    return e('form', { onSubmit: handleSubmit, className: 'task-form' },
        e('h2', null, isEditing ? 'Editar Tarefa' : 'Criar Nova Tarefa'),
        error && e('p', { className: 'error-message' }, error),

        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'title' }, 'Título:'),
            e('input', { type: 'text', id: 'title', value: title, onChange: (ev) => setTitle(ev.target.value), required: true })
        ),

        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'description' }, 'Descrição:'),
            e('textarea', { id: 'description', value: description, onChange: (ev) => setDescription(ev.target.value) })
        ),

        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'assignee' }, 'Responsável:'),
            e('select', { id: 'assignee', value: assigneeId, onChange: (ev) => setAssigneeId(ev.target.value) },
                members.length > 0 ? members.map(member => e('option', { key: member.id, value: member.id }, member.name)) : e('option', { value: '' }, 'Nenhum membro disponível')
            )
        ),

        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'status' }, 'Status:'),
            e('select', { id: 'status', value: status, onChange: (ev) => setStatus(ev.target.value) },
                e('option', { value: 'TODO' }, 'A Fazer'),
                e('option', { value: 'IN_PROGRESS' }, 'Em Progresso'),
                e('option', { value: 'DONE' }, 'Concluída'),
                e('option', { value: 'BLOCKED' }, 'Bloqueada')
            )
        ),

        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'priority' }, 'Prioridade:'),
            e('select', { id: 'priority', value: priority, onChange: (ev) => setPriority(ev.target.value) },
                e('option', { value: 'LOW' }, 'Baixa'),
                e('option', { value: 'MEDIUM' }, 'Média'),
                e('option', { value: 'HIGH' }, 'Alta'),
                e('option', { value: 'CRITICAL' }, 'Crítica')
            )
        ),

        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'dueDate' }, 'Data de Vencimento:'),
            e('input', { type: 'date', id: 'dueDate', value: dueDate, onChange: (ev) => setDueDate(ev.target.value) })
        ),

        e('button', { type: 'submit', className: 'btn' }, isEditing ? 'Salvar Alterações' : 'Criar Tarefa')
    );
};

// Componente para o botão de exclusão de tarefas
const TaskDeleteButton = ({ projectId, taskId, onTaskDeleted }) => {
    const handleDelete = async () => {
        if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
            try {
                await window.apiClient.del(`/api/v1/projects/${projectId}/tasks/${taskId}`);
                window.notificationService.show('Tarefa excluída com sucesso!');
                if (onTaskDeleted) {
                    onTaskDeleted(taskId);
                }
            } catch (err) {
                console.error('Erro ao excluir tarefa:', err);
                window.notificationService.show(err.message, 'error');
            }
        }
    };

    return e('button', { onClick: handleDelete, className: 'btn delete-button' }, 'Excluir');
};

// Componente para exibir um item de tarefa em lista
const TaskListItem = ({ task, onEdit, onDelete }) => {
    const assigneeName = task.assignee ? task.assignee.name : 'Não atribuído';
    return e('div', { className: 'task-list-item' },
        e('h3', null, task.title),
        e('p', null, `Responsável: ${assigneeName}`),
        e('p', null, `Status: ${task.status}`),
        e('div', { className: 'task-actions' },
            e('button', { onClick: () => onEdit(task), className: 'btn' }, 'Editar'),
            e(TaskDeleteButton, { projectId: task.projectId, taskId: task.id, onTaskDeleted: onDelete })
        )
    );
};

// Componente para a lista de tarefas
const TaskList = ({ tasks, onEdit, onDelete }) => {
    return e('div', { className: 'task-list' },
        e('h2', null, 'Lista de Tarefas'),
        tasks.length > 0 ? tasks.map(task => e(TaskListItem, { 
            key: task.id, 
            task, 
            onEdit, 
            onDelete 
        })) : e('p', null, 'Nenhuma tarefa encontrada.')
    );
};

// Componente para um cartão Kanban
const KanbanCard = ({ task, onDragStart }) => {
    const assigneeName = task.assignee ? task.assignee.name : 'Não atribuído';
    return e('div', { 
        className: 'kanban-card', 
        draggable: true, 
        onDragStart: (event) => onDragStart(event, task.id)
    },
        e('h4', null, task.title),
        e('p', null, `Responsável: ${assigneeName}`)
    );
};

// Componente para uma coluna Kanban
const KanbanColumn = ({ title, status, tasks, onDragOver, onDrop, onDragStart }) => {
    return e('div', { 
        className: 'kanban-column', 
        onDragOver: onDragOver, 
        onDrop: (event) => onDrop(event, status)
    },
        e('h3', null, title),
        tasks.map(task => e(KanbanCard, { key: task.id, task, onDragStart }))
    );
};

// Componente para o quadro Kanban
const KanbanBoard = ({ tasks, onTaskStatusChange }) => {
    const handleDragStart = (event, taskId) => {
        event.dataTransfer.setData('taskId', taskId);
    };

    const handleDragOver = (event) => {
        event.preventDefault();
    };

    const handleDrop = (event, newStatus) => {
        const taskId = event.dataTransfer.getData('taskId');
        onTaskStatusChange(parseInt(taskId), newStatus);
    };

    const columns = {
        'TO_DO': 'A Fazer',
        'IN_PROGRESS': 'Em Andamento',
        'DONE': 'Concluído'
    };

    return e('div', { className: 'kanban-board' },
        e('h2', null, 'Quadro Kanban'),
        Object.keys(columns).map(status => e(KanbanColumn, {
            key: status,
            title: columns[status],
            status: status,
            tasks: tasks.filter(task => task.status === status),
            onDragOver: handleDragOver,
            onDrop: handleDrop,
            onDragStart: handleDragStart
        }))
    );
};

// Novo componente para filtros de tarefa
const TaskFilters = ({ onApplyFilters }) => {
    const [status, setStatus] = useState('');
    const [assigneeId, setAssigneeId] = useState('');
    const [priority, setPriority] = useState('');
    const [keyword, setKeyword] = useState('');
    const [members, setMembers] = useState([]);

    // Fetch members for assignee filter
    useEffect(() => {
        const fetchMembers = async () => {
            // Assuming projectId is available globally or passed as prop
            const urlParams = new URLSearchParams(window.location.search);
            const projectId = urlParams.get('projectId');
            if (projectId) {
                try {
                    const response = await window.apiClient.get(`/api/v1/projects/${projectId}/members`);
                    setMembers(response);
                } catch (err) {
                    console.error('Erro ao buscar membros para filtro:', err);
                }
            }
        };
        fetchMembers();
    }, []);

    const handleFilterChange = () => {
        onApplyFilters({ status, assigneeId, priority, keyword });
    };

    return e('div', { className: 'section task-filters' },
        e('h3', null, 'Filtros de Tarefas'),
        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'filterStatus' }, 'Status:'),
            e('select', { id: 'filterStatus', value: status, onChange: (ev) => setStatus(ev.target.value) },
                e('option', { value: '' }, 'Todos'),
                e('option', { value: 'TO_DO' }, 'A Fazer'),
                e('option', { value: 'IN_PROGRESS' }, 'Em Progresso'),
                e('option', { value: 'DONE' }, 'Concluída'),
                e('option', { value: 'BLOCKED' }, 'Bloqueada')
            )
        ),
        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'filterAssignee' }, 'Responsável:'),
            e('select', { id: 'filterAssignee', value: assigneeId, onChange: (ev) => setAssigneeId(ev.target.value) },
                e('option', { value: '' }, 'Todos'),
                members.map(member => e('option', { key: member.id, value: member.id }, member.name))
            )
        ),
        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'filterPriority' }, 'Prioridade:'),
            e('select', { id: 'filterPriority', value: priority, onChange: (ev) => setPriority(ev.target.value) },
                e('option', { value: '' }, 'Todas'),
                e('option', { value: 'LOW' }, 'Baixa'),
                e('option', { value: 'MEDIUM' }, 'Média'),
                e('option', { value: 'HIGH' }, 'Alta'),
                e('option', { value: 'CRITICAL' }, 'Crítica')
            )
        ),
        e('div', { className: 'form-group' },
            e('label', { htmlFor: 'filterKeyword' }, 'Palavra-chave:'),
            e('input', { type: 'text', id: 'filterKeyword', value: keyword, onChange: (ev) => setKeyword(ev.target.value) })
        ),
        e('button', { onClick: handleFilterChange, className: 'btn' }, 'Aplicar Filtros')
    );
};

const ContextNoteModal = ({ task, newStatus, onSave, onCancel }) => {
    const [contextNote, setContextNote] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        onSave(contextNote);
    };

    return e('div', { className: 'modal-overlay' },
        e('div', { className: 'modal-content' },
            e('h3', null, `Adicionar Nota para "${task.title}" (Status: ${newStatus})`),
            e('form', { onSubmit: handleSubmit },
                e('div', { className: 'form-group' },
                    e('label', { htmlFor: 'contextNote' }, 'Nota Rápida (Opcional):'),
                    e('textarea', {
                        id: 'contextNote',
                        value: contextNote,
                        onChange: (ev) => setContextNote(ev.target.value),
                        rows: 4,
                        placeholder: 'Ex: "Iniciei a implementação da API." ou "Aguardando feedback do UX."'})
                ),
                e('div', { className: 'modal-actions' },
                    e('button', { type: 'submit', className: 'btn' }, 'Salvar'),
                    e('button', { type: 'button', className: 'btn btn-secondary', onClick: onCancel }, 'Cancelar')
                )
            )
        )
    );
};

// Componente principal para gerenciamento de tarefas (agora inclui filtros, lista e Kanban)
const TaskManagementPage = () => {
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [editingTask, setEditingTask] = useState(null);
    const [filters, setFilters] = useState({ status: '', assigneeId: '', priority: '', keyword: '' });

    // New state for context note modal
    const [showContextNoteModal, setShowContextNoteModal] = useState(false);
    const [taskToUpdateStatus, setTaskToUpdateStatus] = useState(null);
    const [newStatusForContext, setNewStatusForContext] = useState(null);

    const urlParams = new URLSearchParams(window.location.search);
    const projectId = urlParams.get('projectId');

    useEffect(() => {
        if (projectId) {
            fetchTasks();
        }
    }, [projectId, filters]); // Re-fetch tasks when filters change

    const fetchTasks = async () => {
        setLoading(true);
        setError(null);
        try {
            const queryParams = new URLSearchParams();
            if (filters.status) queryParams.append('status', filters.status);
            if (filters.assigneeId) queryParams.append('assigneeId', filters.assigneeId);
            if (filters.priority) queryParams.append('priority', filters.priority);
            if (filters.keyword) queryParams.append('keyword', filters.keyword);

            const url = `/api/v1/projects/${projectId}/tasks?${queryParams.toString()}`;
            const response = await window.apiClient.get(url);
            setTasks(response);
        } catch (err) {
            console.error('Erro ao buscar tarefas:', err);
            setError('Não foi possível carregar as tarefas.');
        } finally {
            setLoading(false);
        }
    };

    const handleTaskUpdated = (updatedTask) => {
        setTasks(tasks.map(task => task.id === updatedTask.id ? updatedTask : task));
        setEditingTask(null);
    };

    const handleTaskDeleted = (deletedTaskId) => {
        setTasks(tasks.filter(task => task.id !== deletedTaskId));
    };

    // Modified handleTaskStatusChange to open modal
    const handleTaskStatusChange = (taskId, newStatus) => {
        const task = tasks.find(t => t.id === taskId);
        if (task) {
            setTaskToUpdateStatus(task);
            setNewStatusForContext(newStatus);
            setShowContextNoteModal(true);
        }
    };

    // New function to save status change with context note
    const handleSaveStatusChangeWithContext = async (contextNote) => {
        if (!taskToUpdateStatus || !newStatusForContext) return;

        const originalTasks = [...tasks];
        const updatedTasks = tasks.map(task =>
            task.id === taskToUpdateStatus.id ? { ...task, status: newStatusForContext } : task
        );
        setTasks(updatedTasks);
        setShowContextNoteModal(true); // Keep modal open until API call is done or error

        try {
            await window.apiClient.patch(
                `/api/v1/projects/${projectId}/tasks/${taskToUpdateStatus.id}`,
                { status: newStatusForContext, contextNote: contextNote }
            );
            window.notificationService.show('Status da tarefa atualizado com sucesso!');
            setShowContextNoteModal(false);
            setTaskToUpdateStatus(null);
            setNewStatusForContext(null);
            fetchTasks(); // Re-fetch tasks to get updated history
        } catch (err) {
            console.error('Erro ao atualizar status da tarefa:', err);
            setTasks(originalTasks); // Revert on error
            window.notificationService.show('Não foi possível atualizar o status da tarefa.', 'error');
            setShowContextNoteModal(false);
            setTaskToUpdateStatus(null);
            setNewStatusForContext(null);
        }
    };

    const handleApplyFilters = (newFilters) => {
        setFilters(newFilters);
    };

    if (!projectId) {
        return e('div', null, e('h2', null, 'Erro: ID do Projeto não especificado.'));
    }

    if (loading) {
        return e('div', null, 'Carregando tarefas...');
    }

    if (error) {
        return e('div', null, e('h2', null, error));
    }

    return e('div', { className: 'container task-views' },
        editingTask && e('div', { className: 'modal-overlay' },
            e('div', { className: 'modal-content' },
                e(TaskForm, { 
                    projectId: parseInt(projectId), 
                    initialData: editingTask, 
                    isEditing: true, 
                    onTaskCreated: handleTaskUpdated
                }),
                e('button', { className: 'btn', onClick: () => setEditingTask(null) }, 'Cancelar')
            )
        ),
        e(TaskFilters, { onApplyFilters: handleApplyFilters }),
        e(TaskList, { tasks, onEdit: setEditingTask, onDelete: handleTaskDeleted }),
        e(KanbanBoard, { tasks, onTaskStatusChange: handleTaskStatusChange }), // Pass modified handler

        // Render ContextNoteModal
        showContextNoteModal && e(ContextNoteModal, {
            task: taskToUpdateStatus,
            newStatus: newStatusForContext,
            onSave: handleSaveStatusChangeWithContext,
            onCancel: () => {
                setShowContextNoteModal(false);
                setTaskToUpdateStatus(null);
                setNewStatusForContext(null);
            }
        })
    );
};

// Componente para a página de criação de tarefa (reintroduzido)
const CreateTaskPage = () => {
    const urlParams = new URLSearchParams(window.location.search);
    const projectId = urlParams.get('projectId');

    if (!projectId) {
        return e('div', null, e('h2', null, 'Erro: ID do Projeto não especificado.'));
    }

    const handleTaskCreated = (newTask) => {
        console.log('Tarefa criada:', newTask);
        // Redireciona para a página de projetos após a criação
        window.location.href = `projects.html?projectId=${projectId}`;
    };

    return e('div', { className: 'container create-task-page' },
        e(TaskForm, { projectId: parseInt(projectId), onTaskCreated: handleTaskCreated })
    );
};

// Lógica de Renderização Condicional
document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const projectId = urlParams.get('projectId');

    const taskManagementContainer = document.querySelector('#task-list-root');
    const createTaskContainer = document.querySelector('#root');

    // Só renderiza os componentes de gerenciamento de tarefas se houver um projectId na URL
    if (taskManagementContainer && projectId) {
        const taskManagementRoot = ReactDOM.createRoot(taskManagementContainer);
        taskManagementRoot.render(e(TaskManagementPage));
    }

    // Renderiza a página de criação de tarefa se estiver no HTML correto
    if (createTaskContainer && window.location.pathname.includes('task-form.html')) {
        const createTaskRoot = ReactDOM.createRoot(createTaskContainer);
        createTaskRoot.render(e(CreateTaskPage));
    }
});
