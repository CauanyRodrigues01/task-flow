(() => {
const { useState, useEffect } = React;
const e = React.createElement;

const UserProfilePreview = () => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserProfile = async () => {
            setLoading(true);
            setError(null);
            try {
                const fetchedUser = await window.apiClient.get('/api/users/me');
                setUser(fetchedUser);
            } catch (err) {
                console.error('Erro ao carregar perfil do usuário:', err);
                setError(`Não foi possível carregar o perfil. Causa: ${err.message}`);
            } finally {
                setLoading(false);
            }
        };
        fetchUserProfile();
    }, []);

    if (loading) {
        return e('div', { className: 'profile-preview' }, e('p', null, 'Carregando perfil...'));
    }

    if (error) {
        return e('div', { className: 'profile-preview error-message' }, e('p', null, error));
    }

    if (!user) {
        return e('div', { className: 'profile-preview' }, e('p', null, 'Nenhum usuário logado.'));
    }

    // Conditional rendering based on role
    let roleSpecificContent = null;
    if (user.role === 'ADMIN') {
        roleSpecificContent = e('p', { className: 'role-admin' }, 'Acesso total ao sistema.');
    } else if (user.role === 'PROJECT_MANAGER') {
        roleSpecificContent = e('p', { className: 'role-project-manager' }, 'Gerencia projetos e tarefas.');
    } else if (user.role === 'DEVELOPER') {
        roleSpecificContent = e('p', { className: 'role-developer' }, 'Atribuição e desenvolvimento de tarefas.');
    } else if (user.role === 'VIEWER') {
        roleSpecificContent = e('p', { className: 'role-viewer' }, 'Apenas visualização.');
    }

    return e('div', { className: 'profile-preview' },
        e('h3', null, `Olá, ${user.name}!`),
        e('p', null, `Email: ${user.email}`),
        e('p', null, `Cargo: ${user.role}`),
        roleSpecificContent,
        e('p', { className: 'text-sm' }, 'Esta é uma prévia do seu perfil.')
    );
};

// Expose globally for use in other scripts
window.UserProfilePreview = UserProfilePreview;
})();