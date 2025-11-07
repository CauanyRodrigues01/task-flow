(() => {
    'use strict';

    const e = React.createElement;

    document.addEventListener('DOMContentLoaded', () => {
        const domContainer = document.querySelector('#task-carousel-root');
        if (!domContainer) {
            console.error('#task-carousel-root não encontrado.');
            return;
        }

        const urlParams = new URLSearchParams(window.location.search);
        const projectId = urlParams.get('projectId');

        if (projectId) {
            const root = ReactDOM.createRoot(domContainer);
            // Acessa explicitamente o componente do escopo global (window)
            if (window.TaskCarousel) {
                root.render(e(window.TaskCarousel, { projectId }));
            } else {
                console.error('Componente TaskCarousel não foi encontrado no escopo global.');
                domContainer.innerHTML = '<p>Erro ao carregar o componente de tarefas. Verifique o console para mais detalhes.</p>';
            }
        } else {
            domContainer.innerHTML = '<p>ID do projeto não fornecido. Volte para a <a href="projects.html">lista de projetos</a>.</p>';
        }
    });
})();
