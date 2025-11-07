const { useState, useEffect } = React;

const e = React.createElement;

// Importar o componente TaskCarousel
const TaskCarousel = window.TaskCarousel; // Assumindo que TaskCarousel é globalmente disponível após o carregamento do script

// Componente para o formulário de projeto (criar/editar)
const ProjectForm = ({ projectToEdit, onSave, onCancel }) => {
    const [name, setName] = useState(projectToEdit ? projectToEdit.name : '');
    const [description, setDescription] = useState(projectToEdit ? projectToEdit.description : '');

    const handleSubmit = async (event) => {
        event.preventDefault();
        const projectData = { name, description };
        onSave(projectData);
    };

    return e('div', { className: 'modal-overlay' },
        e('div', { className: 'modal-content' },
            e('h2', null, projectToEdit ? 'Editar Projeto' : 'Criar Projeto'),
            e('form', { onSubmit: handleSubmit },
                e('input', { type: 'text', placeholder: 'Nome do Projeto', value: name, onChange: (ev) => setName(ev.target.value), required: true }),
                e('textarea', { placeholder: 'Descrição do Projeto', value: description, onChange: (ev) => setDescription(ev.target.value) }),
                e('button', { type: 'submit', className: 'btn' }, 'Salvar'),
                e('button', { type: 'button', className: 'btn', onClick: onCancel }, 'Cancelar')
            )
        )
    );
};

// Componente para a tabela de projetos
const ProjectTable = ({ projects, onEdit, onRemove }) => {
    return e('div', { className: 'table-wrap' },
        e('table', null,
            e('thead', null,
                e('tr', null,
                    e('th', null, 'ID'), // Nova coluna
                    e('th', null, 'Nome do Projeto'),
                    e('th', null, 'Descrição'),
                    e('th', null, 'Ações'),
                    e('th', null, 'Tarefas')
                )
            ),
            e('tbody', null,
                projects.map(project => e('tr', { key: project.id },
                    e('td', null, project.id), // Exibir ID
                    e('td', null, project.name),
                    e('td', null, project.description),
                    e('td', null,
                        e('button', { className: 'btn', onClick: () => onEdit(project) }, 'Editar'),
                        e('button', { className: 'btn delete-button', onClick: () => onRemove(project.id) }, 'Remover')
                    ),
                    e('td', { style: { display: 'flex', gap: '10px' } }, // Novos links como botões
                        e('a', { href: `task-form.html?projectId=${project.id}`, className: 'btn' }, 'Criar Tarefa'),
                        e('a', { href: `tasks.html?projectId=${project.id}`, className: 'btn' }, 'Ver Tarefas')
                    )
                ))
            )
        )
    );
};

// Novo componente para adicionar membros
const AddMemberForm = ({ onAddMember }) => {
    const [projectId, setProjectId] = useState('');
    const [userId, setUserId] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        if (projectId && userId) {
            onAddMember(parseInt(projectId), parseInt(userId));
            setProjectId('');
            setUserId('');
        }
    };

    return e('div', { className: 'section' },
        e('h2', null, 'Adicionar Membro a um Projeto'),
        e('form', { onSubmit: handleSubmit },
            e('input', { type: 'number', placeholder: 'ID do Projeto', value: projectId, onChange: (ev) => setProjectId(ev.target.value), required: true }),
            e('input', { type: 'number', placeholder: 'ID do Colaborador', value: userId, onChange: (ev) => setUserId(ev.target.value), required: true }),
            e('button', { type: 'submit', className: 'btn' }, 'Adicionar Membro')
        )
    );
};

// Componente principal da página de gerenciamento de projetos
const ProjectListPage = () => {
    const [projects, setProjects] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [editingProject, setEditingProject] = useState(null);

    const fetchProjects = async () => {
        try {
            const token = window.storage.getToken();
            if (!token) {
                window.notificationService.show('Você não está autenticado ou não tem permissão.', 'error');
                window.location.href = 'auth.html'; // Redirecionar para login
                return;
            }
            // Em uma aplicação real, o token conteria a role
            // Para esta história, assumimos que o usuário logado é um PROJECT_MANAGER ou ADMIN

            const fetchedProjects = await window.apiClient.get('/api/v1/projects');
            setProjects(fetchedProjects);
        } catch (error) {
            window.notificationService.show(error.message, 'error');
            console.error('Erro detalhado ao carregar projetos:', error);
        }
    };

    useEffect(() => {
        fetchProjects();
    }, []);

    const handleCreateProject = () => {
        setEditingProject(null);
        setShowForm(true);
    };

    const handleSaveProject = async (projectData) => {
        try {
            if (editingProject) {
                await window.apiClient.put(`/api/v1/projects/${editingProject.id}`, projectData);
                window.notificationService.show('Projeto atualizado com sucesso!');
            } else {
                await window.apiClient.post('/api/v1/projects', projectData);
                window.notificationService.show('Projeto criado com sucesso!');
            }
            setShowForm(false);
            fetchProjects(); // Recarregar a lista de projetos
        } catch (error) {
            window.notificationService.show(error.message, 'error');
            console.error('Erro ao salvar projeto:', error);
        }
    };

    const handleEditProject = (project) => {
        setEditingProject(project);
        setShowForm(true);
    };

    const handleRemoveProject = async (projectId) => {
        if (!confirm('Tem certeza que deseja remover este projeto?')) return;
        try {
            await window.apiClient.del(`/api/v1/projects/${projectId}`);
            window.notificationService.show('Projeto removido com sucesso!');
            fetchProjects();
        } catch (error) {
            window.notificationService.show(error.message, 'error');
            console.error('Erro ao remover projeto:', error);
        }
    };

    const handleAddMember = async (projectId, userId) => {
        try {
            await window.apiClient.post(`/api/v1/projects/${projectId}/members`, userId);
            window.notificationService.show('Membro adicionado com sucesso!');
        } catch (error) {
            window.notificationService.show(error.message, 'error');
            console.error('Erro ao adicionar membro:', error);
        }
    };

    return e('div', { className: 'container' },
        e('h1', null, 'Gerenciamento de Projetos'),
        e('div', { className: 'page-actions' },
            e('button', { className: 'btn', onClick: handleCreateProject }, 'Criar Novo Projeto')
        ),
        e(ProjectTable, { projects: projects, onEdit: handleEditProject, onRemove: handleRemoveProject }),
        showForm && e(ProjectForm, { projectToEdit: editingProject, onSave: handleSaveProject, onCancel: () => setShowForm(false) }),
        e(AddMemberForm, { onAddMember: handleAddMember })
    );
};

// Renderiza a lista de projetos na div #root
const projectDomContainer = document.querySelector('#root');
if (projectDomContainer) {
    const root = ReactDOM.createRoot(projectDomContainer);
    root.render(e(ProjectListPage));
}
