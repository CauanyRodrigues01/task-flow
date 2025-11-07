const useMyFocusTasks = (sortBy = 'dueDate', order = 'asc') => {
    const { useState, useEffect } = React;
    const [tasks, setTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchTasks = async () => {
            setLoading(true);
            setError(null);
            try {
                const result = await myFocusService.getMyFocusTasks(sortBy, order);
                setTasks(result);
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchTasks();
    }, [sortBy, order]);

    return { tasks, loading, error };
};
