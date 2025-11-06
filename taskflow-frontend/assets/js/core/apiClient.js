(() => {
    // Define a URL base para a API do backend.
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
            const contentType = response.headers.get("content-type");

            if (!response.ok) {
                let errorMessage = `HTTP error! status: ${response.status}`;
                if (contentType && contentType.indexOf("application/json") !== -1) {
                    const errorData = await response.json();
                    errorMessage = errorData.message || JSON.stringify(errorData);
                } else {
                    errorMessage = await response.text();
                }
                throw new Error(errorMessage);
            }

            // Handle empty JSON response
            if (contentType && contentType.indexOf("application/json") !== -1) {
                 const text = await response.text();
                 return text ? JSON.parse(text) : {}; // Return empty object for empty response
            } 
            return response.text(); // Return as text if not json

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