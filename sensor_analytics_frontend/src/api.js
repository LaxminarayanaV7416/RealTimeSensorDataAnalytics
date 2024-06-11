const API_URL = 'http://127.0.0.1:8080';
//test
export const login = async (username, password) => {
  const response = await fetch(`${API_URL}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*' },
    body: JSON.stringify({ username, password }),
  });

  if (!response.ok) {
    throw new Error('Please enter valid Credentials!');
  }

  return response.json();
};

export const register = async (name, username, email, password) => {
  const response = await fetch(`${API_URL}/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, username, email, password }),
  });

  if (!response.ok) {
    throw new Error(`Signup failed with status: ${response.status}`);
  }

  return response.json();
};