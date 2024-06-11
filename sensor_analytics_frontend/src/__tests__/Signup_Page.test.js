import React from 'react';
import { render, screen, fireEvent, act } from '@testing-library/react'; 
import { MemoryRouter } from 'react-router-dom';
import '@testing-library/jest-dom';
import SignupPage from '../SignupPage';

describe('Sign up page components', () => {
    test('Contains the input field to enter Name', () => {
        render(
          <MemoryRouter> 
          <SignupPage />
        </MemoryRouter>
            );
      const name = screen.getByPlaceholderText("NAME");
      expect(name).toBeInTheDocument();
    });
  
    test('Contains the input field to enter Username', () => {
      render(
        <MemoryRouter> 
        <SignupPage />
      </MemoryRouter>
    );
      const username = screen.getByPlaceholderText("USERNAME");
      expect(username).toBeInTheDocument();
    });

    test('Contains the input field to enter Email Address', () => {
      render(
        <MemoryRouter> 
        <SignupPage />
      </MemoryRouter>
    );
      const username = screen.getByPlaceholderText("EMAIL ADDRESS");
      expect(username).toBeInTheDocument();
    });

    test('Contains the input field to enter password', () => {
      render(
        <MemoryRouter> 
        <SignupPage />
      </MemoryRouter>
    );
      const username = screen.getByPlaceholderText("PASSWORD");
      expect(username).toBeInTheDocument();
    });
  
    test('Contains the button for signup', () => {
      render(
        <MemoryRouter> 
        <SignupPage />
      </MemoryRouter>
    );
      const signupButton = screen.getAllByText(/SIGN UP/i);
      expect(signupButton[0]).toBeInTheDocument();
    });
  });