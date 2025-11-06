(() => {
    const TOKEN_KEY = 'taskflow_token';

    function setToken(token) {
        localStorage.setItem(TOKEN_KEY, token);
    }

    function getToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    function removeToken() {
        localStorage.removeItem(TOKEN_KEY);
    }

    function isAuthenticated() {
        return !!getToken();
    }

    window.storage = {
        setToken,
        getToken,
        removeToken,
        isAuthenticated
    };
})();