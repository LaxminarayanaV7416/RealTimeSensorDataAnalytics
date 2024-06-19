import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerAdmin } from './api';
import './Styles/LoginSignupPage.css';

const AdminSignupPage = () => {
  const [name, setName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
  };

  const validateUsername = (username) => {
    const re = /^[a-zA-Z]+$/;
    return re.test(username);
  };

  const validateName = (name) => {
    const re = /^[a-zA-Z\s]+$/;
    return re.test(name);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage('');

    if (!name || !username || !email || !password) {
      setErrorMessage('All fields are required');
      return;
    }

    if (!validateName(name)) {
      setErrorMessage('Name must contain only letters and spaces');
      return;
    }

    if (!validateUsername(username)) {
      setErrorMessage('Username must contain only letters without spaces');
      return;
    }

    if (!validateEmail(email)) {
      setErrorMessage('Invalid email format');
      return;
    }

    try {
      const data = await registerAdmin(name, username, email, password);
      console.log('Signup successful:', data);
      navigate('/');
    } catch (error) {
      console.error('Signup error:', error);
      setErrorMessage('Username or Email already exists');
    }
  };

  return (
    <div className='C1'>
      <div className='container'>
        <div className='Header'>
          <div className='TEXT'>ADMIN - SIGN UP</div>
          <div className='UNDERLINE'></div>
        </div>
        <div className='Inputs'>
          <div className='input'>
            <input
              type='text'
              placeholder='NAME'
              value={name}
              onChange={(e) => setName(e.target.value)}
            />
          </div>
          <div className='input'>
            <input
              type='text'
              placeholder='USERNAME'
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className='input'>
            <input
              type='email'
              placeholder='EMAIL ADDRESS'
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className='input'>
            <input
              type='password'
              placeholder='PASSWORD'
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
        </div>
        {errorMessage && <div className='error-message' style={{color:"red", marginTop:"-40px", marginBottom:"20px"  }}>{errorMessage}</div>}
        <div className='Submit-Container'>
          <button className='Submit' onClick={handleSubmit}>
            Sign Up
          </button>
        </div>
      </div>
    </div>
  );
};

export default AdminSignupPage;