const DashboardPage = () => {
    const { useState, useEffect } = React;
    const e = React.createElement;

    const [selectedProjectId, setSelectedProjectId] = useState('');
    const [projects, setProjects] = useState([]);

    const { data, loading, error } = useDashboardData(selectedProjectId ? parseInt(selectedProjectId) : null);

    useEffect(() => {
        const fetchProjects = async () => {
            try {
                const response = await window.apiClient.get('/api/v1/projects');
                setProjects(response);
                if (response.length > 0 && !selectedProjectId) {
                    setSelectedProjectId(response[0].id); // Set first project as default
                }
            } catch (err) {
                console.error('Error fetching projects:', err);
            }
        };
        fetchProjects();
    }, []);

    if (loading) {
        return e('div', null, 'Carregando dashboard...');
    }

    if (error) {
        return e('div', null, e('h2', null, `Erro ao carregar dashboard: ${error.message || 'Erro desconhecido'}`));
    }

    return e('div', { className: 'dashboard-page' },
        e('h2', null, 'Painel Principal'),

        e('div', { className: 'dashboard-filters' },
            e('label', { htmlFor: 'project-filter' }, 'Filtrar por Projeto:'),
            e('select', {
                id: 'project-filter',
                value: selectedProjectId,
                onChange: (ev) => setSelectedProjectId(ev.target.value)
            },
                e('option', { value: '' }, 'Todos os Projetos'),
                projects.map(project => e('option', { key: project.id, value: project.id }, project.name))
            )
        ),

        data && e(DashboardWidgets, { data })
    );
};
