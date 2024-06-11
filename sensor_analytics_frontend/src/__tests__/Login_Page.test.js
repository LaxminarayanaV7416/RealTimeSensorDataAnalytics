import React from 'react';
import { render, screen, fireEvent, act } from '@testing-library/react'; 
import { MemoryRouter } from 'react-router-dom';
import '@testing-library/jest-dom';
import { UserContext } from '../UserContext';
import LoginPage from '../LoginPage';

const mockUser = {  
    "expiryTime": "60 Min",
    "username": "dinesh",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaW5lc2ggD4gMSA8PiBJU19VU0VSIiwiaWF0IjoxNzE3OTA4NzcwLCJleHAiOjE3MTc5MTIzNzB9.HHRhEBRAVlfV6s1s9UVNSe9nCCez00qyi0FmUXLe3Vb3RiugbUU1Cys4-b6ojM1Q0EoTJidNOpixwz9A4GaeGw" 
};

describe('Login page components', () => {
  
    test('Contains the input field to enter Username', () => {
      render(
        <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <LoginPage />
                </UserContext.Provider>
            </MemoryRouter>
    );
      const username = screen.getByPlaceholderText("USERNAME");
      expect(username).toBeInTheDocument();
    });


      test('Contains the input field to enter password', () => {
        render(
            <MemoryRouter>
            <UserContext.Provider value={{ user: mockUser }}>
                <LoginPage />
            </UserContext.Provider>
        </MemoryRouter>
      );
        const username = screen.getByPlaceholderText("PASSWORD");
        expect(username).toBeInTheDocument();
      });

    
  
    test('Contains the button for login', () => {
      render(
        <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <LoginPage />
                </UserContext.Provider>
            </MemoryRouter>
    );
      const signupButton = screen.getAllByText(/Login/i);
      expect(signupButton[0]).toBeInTheDocument();
    });
  });