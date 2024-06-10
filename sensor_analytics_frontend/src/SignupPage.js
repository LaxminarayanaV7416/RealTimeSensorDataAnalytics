import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from './api';
import './Styles/LoginSignupPage.css';

const SignupPage = () => {
  const [name, setName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setErrorMessage('');

    try {
      const data = await register(name, username, email, password);
      console.log('Signup successful:', data);
      navigate('/dashboard');
    } catch (error) {
      console.error('Signup error:', error);
      setErrorMessage(error.message);
    }
  };

  return (
    <div className='C1'>
      <div className='container'>
        <div className='Header'>
          <div className='TEXT'>SIGN UP</div>
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
        {errorMessage && <div className='error-message'>{errorMessage}</div>}
        <div className='Forgot-Password'>Already A Member ?<a href='/'><span>Click Here!</span></a></div>
        <div className='Submit-Container'>
          <button className='Submit' onClick={handleSubmit}>
            Sign Up
          </button>
        </div>
      </div>
    </div>
  );
};

export default SignupPage;