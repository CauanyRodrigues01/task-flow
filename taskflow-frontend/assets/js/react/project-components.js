const { useState, useEffect } = React;

const e = React.createElement;

// Componente para o formulário de projeto (criar/editar)
const ProjectForm = ({ projectToEdit, onSave, onCancel }) => {
    const [name, setName] = useState(projectToEdit ? projectToEdit.name : '');
    const [description, setDescription] = useState(projectToEdit ? projectToEdit.description : '');

    const handleSubmit = async (event) => {
        event.preventDefault();
        const projectData = { name, description };
        onSave(projectData);
    };

    return e('div', { className: 'modal' },
        e('div', { className: 'modal-content' },
            e('h2', null, projectToEdit ? 'Editar Projeto' : 'Criar Projeto'),
            e('form', { onSubmit: handleSubmit },
                e('input', { type: 'text', placeholder: 'Nome do Projeto', value: name, onChange: (ev) => setName(ev.target.value), required: true }),
                e('textarea', { placeholder: 'Descrição do Projeto', value: description, onChange: (ev) => setDescription(ev.target.value) }),
                e('button', { type: 'submit' }, 'Salvar'),
                e('button', { type: 'button', onClick: onCancel }, 'Cancelar')
            )
        )
    );
};

// Componente para a tabela de projetos
const ProjectTable = ({ projects, onEdit, onRemove }) => {
    return e('table', null,
        e('thead', null,
            e('tr', null,
                e('th', null, 'Nome do Projeto'),
                e('th', null, 'Descrição'),
                e('th', null, 'Ações')
            )
        ),
        e('tbody', null,
            projects.map(project => e('tr', { key: project.id },
                e('td', null, project.name),
                e('td', null, project.description),
                e('td', null,
                    e('button', { onClick: () => onEdit(project) }, 'Editar'),
                    e('button', { onClick: () => onRemove(project.id) }, 'Remover')
                )
            ))
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
                alert('Você não está autenticado ou não tem permissão.');
                window.location.href = 'auth.html'; // Redirecionar para login
                return;
            }
            // Em uma aplicação real, o token conteria a role
            // Para esta história, assumimos que o usuário logado é um PROJECT_MANAGER ou ADMIN

            const fetchedProjects = await window.apiClient.get('/api/v1/projects');
            setProjects(fetchedProjects);
        } catch (error) {
            alert(`Erro ao carregar projetos: ${error.message}`);
            console.error('Erro ao carregar projetos:', error);
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
                alert('Projeto atualizado com sucesso!');
            } else {
                await window.apiClient.post('/api/v1/projects', projectData);
                alert('Projeto criado com sucesso!');
            }
            setShowForm(false);
            fetchProjects(); // Recarregar a lista de projetos
        } catch (error) {
            alert(`Erro ao salvar projeto: ${error.message}`);
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
            alert('Projeto removido com sucesso!');
            fetchProjects();
        } catch (error) {
            alert(`Erro ao remover projeto: ${error.message}`);
            console.error('Erro ao remover projeto:', error);
        }
    };

    return e('div', null,
        e('h1', null, 'Gerenciamento de Projetos'),
        e('button', { onClick: handleCreateProject }, 'Criar Novo Projeto'),
        e(ProjectTable, { projects: projects, onEdit: handleEditProject, onRemove: handleRemoveProject }),
        showForm && e(ProjectForm, { projectToEdit: editingProject, onSave: handleSaveProject, onCancel: () => setShowForm(false) })
    );
};

const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(ProjectListPage));
