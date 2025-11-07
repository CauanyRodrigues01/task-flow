window.loadingService = (() => {
    let isLoading = false;
    const subscribers = [];

    const setLoading = (state) => {
        isLoading = state;
        subscribers.forEach(callback => callback(isLoading));
    };

    return {
        show: () => setLoading(true),
        hide: () => setLoading(false),
        subscribe: (callback) => {
            subscribers.push(callback);
            return () => {
                const index = subscribers.indexOf(callback);
                if (index > -1) {
                    subscribers.splice(index, 1);
                }
            };
        },
        getIsLoading: () => isLoading
    };
})();
