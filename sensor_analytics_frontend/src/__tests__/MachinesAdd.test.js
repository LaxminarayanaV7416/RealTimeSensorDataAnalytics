import React from 'react';
import { render, screen } from '@testing-library/react'; 
import { MemoryRouter } from 'react-router-dom';
import '@testing-library/jest-dom';
import { UserContext } from '../UserContext';
import MachinesAdd from '../MachinesAdd';

const mockUser = {  
    "expiryTime": "60 Min",
    "username": "dinesh",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkaW5lc2ggD4gMSA8PiBJU19VU0VSIiwiaWF0IjoxNzE3OTA4NzcwLCJleHAiOjE3MTc5MTIzNzB9.HHRhEBRAVlfV6s1s9UVNSe9nCCez00qyi0FmUXLe3Vb3RiugbUU1Cys4-b6ojM1Q0EoTJidNOpixwz9A4GaeGw" 
};

describe('Components inside the Machine adding division', () => {
    test('Contains the input field to enter machine name', () => {
        render(
            <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <MachinesAdd />
                </UserContext.Provider>
            </MemoryRouter>
        );

        expect(screen.getByPlaceholderText("Enter Group Name")).toBeInTheDocument();
    });

    test('Contains the input field to enter topic name', () => {
        render(
            <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <MachinesAdd />
                </UserContext.Provider>
            </MemoryRouter>
        );

        expect(screen.getByPlaceholderText("Enter Topic Name")).toBeInTheDocument();
    });

    test('Contains the button for adding the machine', () => {
        render(
            <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <MachinesAdd />
                </UserContext.Provider>
            </MemoryRouter>
        );

        expect(screen.getByText(/Add Machine/i)).toBeInTheDocument();
    });
});

describe('Components inside the Machine assigning division', () => {
    test('Contains the dropdown field to select group name', () => {
        render(
            <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <MachinesAdd />
                </UserContext.Provider>
            </MemoryRouter>
        );
        const selectElement = screen.getByDisplayValue('Select a Group');
        expect(selectElement).toBeInTheDocument();
    });

    test('Contains the dropdown field to select topic name', () => {
        render(
            <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <MachinesAdd />
                </UserContext.Provider>
            </MemoryRouter>
        );
        const selectElement = screen.getByDisplayValue('Select a Topic');
        expect(selectElement).toBeInTheDocument();
    });

    

    test('Contains the input field to enter email address', () => {
        render(
            <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <MachinesAdd />
                </UserContext.Provider>
            </MemoryRouter>
        );

        const selectElement = screen.getByDisplayValue('Select a UserName');
        expect(selectElement).toBeInTheDocument();
    });

    test('Contains the button for assigning the machine', () => {
        render(
            <MemoryRouter>
                <UserContext.Provider value={{ user: mockUser }}>
                    <MachinesAdd />
                </UserContext.Provider>
            </MemoryRouter>
        );

        expect(screen.getByText(/Assign Machine to User/i)).toBeInTheDocument();
    });
});