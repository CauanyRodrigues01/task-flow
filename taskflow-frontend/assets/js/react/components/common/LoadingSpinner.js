const { useState, useEffect } = React;
const e = React.createElement;

const LoadingSpinner = () => {
    const [isLoading, setIsLoading] = useState(window.loadingService.getIsLoading());

    useEffect(() => {
        const unsubscribe = window.loadingService.subscribe(setIsLoading);
        return () => unsubscribe();
    }, []);

    if (!isLoading) {
        return null;
    }

    return e('div', { className: 'loading-overlay' },
        e('div', { className: 'spinner' })
    );
};
