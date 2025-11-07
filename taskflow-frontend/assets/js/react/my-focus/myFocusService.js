const myFocusService = (() => {
    const getMyFocusTasks = async (sortBy = 'dueDate', order = 'asc') => {
        const url = `/api/v1/tasks/my-focus?sortBy=${sortBy}&order=${order}`;
        try {
            const response = await window.apiClient.get(url);
            return response;
        } catch (error) {
            console.error('Error fetching My Focus tasks:', error);
            throw error;
        }
    };

    return {
        getMyFocusTasks,
    };
})();
