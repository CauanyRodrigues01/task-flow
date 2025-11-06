(() => {
    // Usar caminho relativo para a API para evitar problemas de CORS em desenvolvimento.
     const baseURL = "http://localhost:8080";

    async function request(method, path, body) {
        const url = `${baseURL}${path}`;
        const init = { 
            method,
            headers: { 
                'Content-Type': 'application/json'
            }
        };

        const token = window.storage ? window.storage.getToken() : null;
        if (token) {
            init.headers['Authorization'] = `Bearer ${token}`;
        }

        if (body !== undefined) {
            init.body = JSON.stringify(body);
        }

        try {
            const response = await fetch(url, init);
            const data = await response.json();

            if (!response.ok) {
                const errorMessage = data.message || response.statusText;
                throw new Error(errorMessage);
            }

            return data;
        } catch (err) {
            console.error('API Error:', { url, method, body, error: err });
            throw err;
        }
    }

    window.apiClient = {
        get: (path) => request('GET', path),
        post: (path, body) => request('POST', path, body),
        put: (path, body) => request('PUT', path, body),
        del: (path) => request('DELETE', path),
    };
})();