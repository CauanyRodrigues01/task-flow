const DashboardWidgets = ({ data }) => {
    const e = React.createElement;

    if (!data) {
        return e('div', null, 'Nenhum dado de dashboard disponível.');
    }

    const { totalTasks, tasksByStatus, projectProgressSummary } = data;

    return e('div', { className: 'dashboard-widgets' },
        e('div', { className: 'widget' },
            e('h3', null, 'Total de Tarefas'),
            e('p', null, totalTasks)
        ),
        e('div', { className: 'widget' },
            e('h3', null, 'Tarefas por Status'),
            e('ul', null,
                Object.entries(tasksByStatus).map(([status, count]) =>
                    e('li', { key: status }, `${status}: ${count}`)
                )
            )
        ),
        e('div', { className: 'widget' },
            e('h3', null, 'Progresso do Projeto'),
            e('p', null, `Concluído: ${projectProgressSummary.completedPercentage}%`),
            e('p', null, `Total de Projetos: ${projectProgressSummary.totalProjects}`)
        )
    );
};
