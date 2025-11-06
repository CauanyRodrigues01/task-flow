const { useState, useEffect } = React;

const e = React.createElement;

// Componente para o formulário de usuário (convidar/editar)
const UserForm = ({ userToEdit, onSave, onCancel }) => {
    const [name, setName] = useState(userToEdit ? userToEdit.name : '');
    const [email, setEmail] = useState(userToEdit ? userToEdit.email : '');
    const [password, setPassword] = useState(''); // Senha sempre vazia para edição, ou nova para convite
    const [role, setRole] = useState(userToEdit ? userToEdit.role : 'COLLABORATOR');

    const handleSubmit = async (event) => {
        event.preventDefault();
        const userData = { name, email, password, role };
        onSave(userData);
    };

    return e('div', { className: 'modal' },
        e('div', { className: 'modal-content' },
            e('h2', null, userToEdit ? 'Editar Usuário' : 'Convidar Usuário'),
            e('form', { onSubmit: handleSubmit },
                e('input', { type: 'text', placeholder: 'Nome', value: name, onChange: (ev) => setName(ev.target.value), required: true }),
                e('input', { type: 'email', placeholder: 'Email', value: email, onChange: (ev) => setEmail(ev.target.value), required: true, disabled: !!userToEdit }), // Email não editável
                !userToEdit && e('input', { type: 'password', placeholder: 'Senha', value: password, onChange: (ev) => setPassword(ev.target.value), required: true }), // Senha apenas para convite
                e('select', { value: role, onChange: (ev) => setRole(ev.target.value) },
                    e('option', { value: 'COLLABORATOR' }, 'Colaborador'),
                    e('option', { value: 'PROJECT_MANAGER' }, 'Gerente de Projeto'),
                    e('option', { value: 'ADMIN' }, 'Administrador')
                ),
                e('button', { type: 'submit' }, 'Salvar'),
                e('button', { type: 'button', onClick: onCancel }, 'Cancelar')
            )
        )
    );
};

// Componente para a tabela de usuários
const UserTable = ({ users, onEdit, onRemove, onUpdateRole }) => {
    return e('table', null,
        e('thead', null,
            e('tr', null,
                e('th', null, 'Nome'),
                e('th', null, 'Email'),
                e('th', null, 'Perfil'),
                e('th', null, 'Ações')
            )
        ),
        e('tbody', null,
            users.map(user => e('tr', { key: user.id },
                e('td', null, user.name),
                e('td', null, user.email),
                e('td', null, user.role),
                e('td', null,
                    e('button', { onClick: () => onEdit(user) }, 'Editar'),
                    e('button', { onClick: () => onRemove(user.id) }, 'Remover')
                )
            ))
        )
    );
};

// Componente principal da página de gerenciamento de usuários
const UserManagementPage = () => {
    const [users, setUsers] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [editingUser, setEditingUser] = useState(null);

    const fetchUsers = async () => {
        try {
            const token = window.storage.getToken();
            if (!token) {
                alert('Você não está autenticado ou não tem permissão.');
                window.location.href = 'auth.html'; // Redirecionar para login
                return;
            }
            // Temporariamente, para testar, vamos simular um ADMIN role
            // Em uma aplicação real, o token conteria a role
            const userRole = 'ADMIN'; // Simulação

            if (userRole !== 'ADMIN') {
                alert('Você não tem permissão para acessar esta página.');
                window.location.href = 'projects.html'; // Redirecionar para uma página sem permissão
                return;
            }

            const fetchedUsers = await window.apiClient.get('/api/users');
            setUsers(fetchedUsers);
        } catch (error) {
            alert(`Erro ao carregar usuários: ${error.message}`);
            console.error('Erro ao carregar usuários:', error);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const handleInviteUser = () => {
        setEditingUser(null);
        setShowForm(true);
    };

    const handleSaveUser = async (userData) => {
        try {
            if (editingUser) {
                // Lógica de edição de perfil (apenas role pode ser alterada via PUT /users/{userId}/role)
                await window.apiClient.put(`/api/users/${editingUser.id}/role?newRole=${userData.role}`);
                alert('Perfil de usuário atualizado com sucesso!');
            } else {
                // Lógica de convite de novo usuário
                await window.apiClient.post('/api/users', userData);
                alert('Usuário convidado com sucesso!');
            }
            setShowForm(false);
            fetchUsers(); // Recarregar a lista de usuários
        } catch (error) {
            alert(`Erro ao salvar usuário: ${error.message}`);
            console.error('Erro ao salvar usuário:', error);
        }
    };

    const handleEditUser = (user) => {
        setEditingUser(user);
        setShowForm(true);
    };

    const handleRemoveUser = async (userId) => {
        if (!confirm('Tem certeza que deseja remover este usuário?')) return;
        try {
            await window.apiClient.del(`/api/users/${userId}`);
            alert('Usuário removido com sucesso!');
            fetchUsers();
        } catch (error) {
            alert(`Erro ao remover usuário: ${error.message}`);
            console.error('Erro ao remover usuário:', error);
        }
    };

    return e('div', null,
        e('h1', null, 'Gerenciamento de Usuários'),
        e('button', { onClick: handleInviteUser }, 'Convidar Novo Usuário'),
        e(UserTable, { users: users, onEdit: handleEditUser, onRemove: handleRemoveUser }),
        showForm && e(UserForm, { userToEdit: editingUser, onSave: handleSaveUser, onCancel: () => setShowForm(false) })
    );
};

const domContainer = document.querySelector('#root');
const root = ReactDOM.createRoot(domContainer);
root.render(e(UserManagementPage));
