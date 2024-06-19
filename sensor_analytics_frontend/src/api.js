const API_URL = 'http://127.0.0.1:8080';

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

export const registerAdmin = async (name, username, email, password) => {
  const response = await fetch(`${API_URL}/register-admin`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, username, email, password }),
  });

  if (!response.ok) {
    throw new Error(`Signup failed with status: ${response.status}`);
  }

  return response.json();
};

export const onboardNewSensor = async (groupName, topicName, machineName, token) => {
  const response = await fetch(`${API_URL}/onboard-new-sensor`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ groupName, topicName, machineName }),
  });

  if (!response.ok) {
    throw new Error('Sensor Already Present');
  }

  return response.json();
};

export const assignMachineToUser = async (username, machineName, token) => {
  const response = await fetch(`${API_URL}/assign-machine-to-user`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ username, machineName }),
  });

  if (!response.ok) {
    throw new Error('Failed to assign machine to user');
  }

  return response.json();
};

export const getAllMachines = async (token) => {
  const response = await fetch(`${API_URL}/get-all-machines`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    },
  });

  if (!response.ok) {
    throw new Error('Failed to fetch machines');
  }

  return response.json();
};

export const getAllUsers = async (token) => {
  const response = await fetch(`${API_URL}/get-all-users`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    },
  });

  if (!response.ok) {
    throw new Error('Failed to fetch machines');
  }

  return response.json();
};

export const deleteMachine = async (machineId, groupName, topicName, token) => {
  const response = await fetch(`${API_URL}/delete-sensor`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({ groupName, topicName }),
  });

  if (!response.ok) {
    throw new Error('Failed to delete machine');
  }

  return response.json();
};