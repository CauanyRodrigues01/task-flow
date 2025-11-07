const dashboardService = (() => {
    const getDashboardData = async (projectId = null) => {
        let url = '/api/v1/dashboard';
        if (projectId) {
            url += `?projectId=${projectId}`;
        }
        try {
            const response = await window.apiClient.get(url);
            return response;
        } catch (error) {
            console.error('Error fetching dashboard data:', error);
            throw error;
        }
    };

    return {
        getDashboardData,
    };
})();
