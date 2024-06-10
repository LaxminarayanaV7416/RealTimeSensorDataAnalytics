import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserContext } from './UserContext';
import { login } from './api';
import './Styles/LoginSignupPage.css';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();
  const { loginUser } = useContext(UserContext);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage('');

    try {
      const data = await login(username, password);
      console.log('Login successful:', data);
      loginUser(data);
      navigate('/dashboard');
    } catch (error) {
      console.error('Login error:', error);
      setErrorMessage(error.message);
    }
  };

  return (
    <div className='C1'>
      <div className='container'>
        <div className='Header'>
          <div className='TEXT'>LOGIN</div>
          <div className='UNDERLINE'></div>
        </div>
        <div className='Inputs'>
          <div className='input'>
            <input
              type='email'
              placeholder='USERNAME'
              value={username}
              onChange={(e) => setUsername(e.target.value)}
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
        {errorMessage && <div className='error-message'><span style={{ color: 'red' }}>{errorMessage}</span></div>}
        <div className='Forgot-Password'>
          Not A Member ?<a href='/signup'><span>Click Here!</span></a>
        </div>
        <div className='Submit-Container'>
          <button className='Submit' onClick={handleSubmit}>
            Login
          </button>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;