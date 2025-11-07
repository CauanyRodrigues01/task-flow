(() => {
    const ACCESS_TOKEN_KEY = 'taskflow_access_token';
    const REFRESH_TOKEN_KEY = 'taskflow_refresh_token';

    function saveAccessToken(token) {
        localStorage.setItem(ACCESS_TOKEN_KEY, token);
    }

    function getAccessToken() {
        return localStorage.getItem(ACCESS_TOKEN_KEY);
    }

    function saveRefreshToken(token) {
        localStorage.setItem(REFRESH_TOKEN_KEY, token);
    }

    function getRefreshToken() {
        return localStorage.getItem(REFRESH_TOKEN_KEY);
    }

    function saveTokens(accessToken, refreshToken) {
        saveAccessToken(accessToken);
        saveRefreshToken(refreshToken);
    }

    function clearTokens() {
        localStorage.removeItem(ACCESS_TOKEN_KEY);
        localStorage.removeItem(REFRESH_TOKEN_KEY);
    }

    function isAuthenticated() {
        return !!getAccessToken();
    }

    window.storage = {
        saveTokens,
        clearTokens,
        getAccessToken,
        saveAccessToken,
        getRefreshToken,
        isAuthenticated
    };
})();