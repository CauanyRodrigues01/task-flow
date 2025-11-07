(() => {
    const baseURL = window.config.API_BASE_URL;
    let isRefreshing = false;
    let failedQueue = [];

    const processQueue = (error, token = null) => {
        failedQueue.forEach(prom => {
            if (error) {
                prom.reject(error);
            } else {
                prom.resolve(token);
            }
        });
        failedQueue = [];
    };

    async function refreshToken() {
        window.loadingService.show();
        isRefreshing = true;
        const refreshToken = window.storage.getRefreshToken();
        if (!refreshToken) {
            window.storage.clearTokens();
            window.location.href = 'auth.html';
            window.loadingService.hide();
            return Promise.reject(new Error('No refresh token available'));
        }

        try {
            const response = await fetch(`${baseURL}/api/v1/auth/refresh-token`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ refreshToken }),
            });

            if (!response.ok) {
                throw new Error('Failed to refresh token');
            }

            const data = await response.json();
            window.storage.saveAccessToken(data.accessToken);
            processQueue(null, data.accessToken);
            return data.accessToken;
        } catch (error) {
            window.storage.clearTokens();
            window.location.href = 'auth.html';
            processQueue(error, null);
            return Promise.reject(error);
        } finally {
            isRefreshing = false;
            window.loadingService.hide();
        }
    }

    async function request(method, path, body) {
        window.loadingService.show();
        const url = `${baseURL}${path}`;
        const init = {
            method,
            headers: { 'Content-Type': 'application/json' },
        };

        const token = window.storage ? window.storage.getAccessToken() : null;
        if (token) {
            init.headers['Authorization'] = `Bearer ${token}`;
        }

        if (body !== undefined) {
            init.body = JSON.stringify(body);
        }

        try {
            const response = await fetch(url, init);

            if (!response.ok) {
                if ((response.status === 401 || response.status === 403) && !url.includes('/api/v1/auth')) {
                    if (isRefreshing) {
                        return new Promise((resolve, reject) => {
                            failedQueue.push({ resolve, reject });
                        }).then(newToken => {
                            init.headers['Authorization'] = `Bearer ${newToken}`;
                            return fetch(url, init); // Retry the original request
                        }).finally(() => {
                            window.loadingService.hide();
                        });
                    }

                    return refreshToken().then(newAccessToken => {
                        init.headers['Authorization'] = `Bearer ${newAccessToken}`;
                        return fetch(url, init); // Retry the original request
                    }).finally(() => {
                        window.loadingService.hide();
                    });
                }

                let errorMessage = `HTTP error! status: ${response.status}`;
                const contentType = response.headers.get("content-type");
                if (contentType && contentType.indexOf("application/json") !== -1) {
                    const errorData = await response.json();
                    errorMessage = errorData.message || JSON.stringify(errorData);
                } else {
                    errorMessage = await response.text();
                }
                throw new Error(errorMessage);
            }
            
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                const text = await response.text();
                return text ? JSON.parse(text) : {};
            }
            return response.text();

        } catch (err) {
            console.error('API Error:', { url, method, body, error: err });
            throw err;
        } finally {
            window.loadingService.hide();
        }
    }

    window.apiClient = {
        get: (path) => request('GET', path),
        post: (path, body) => request('POST', path, body),
        put: (path, body) => request('PUT', path, body),
        del: (path) => request('DELETE', path),
    };
})();