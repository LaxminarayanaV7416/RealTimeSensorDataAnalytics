import React, { useState, useEffect, useContext } from 'react';
import NavBar from './Components/NavBar';
import SideBar from './Components/SideBar';
import { UserContext } from './UserContext';
import './Styles/Machines.css'
import { getAllMachines, deleteMachine } from './api';
import { useNavigate } from 'react-router-dom';

function Machines() {
  const navigate = useNavigate();
  const { user, loading } = useContext(UserContext);
  const [machines, setMachines] = useState([]);
  const [error, setError] = useState('');
  const [showConfirm, setShowConfirm] = useState(false);
  const [machineToDelete, setMachineToDelete] = useState(null);

  useEffect(() => {
    const timeout = setTimeout(() => {
      if (user == null) {
        navigate('/');
      } else {
        fetchMachines();
      }
    }, 100);
    if (!loading) {
      const timeout = setTimeout(() => {
        if (user == null) {
          navigate('/');
        }
      }, 0);
      return () => clearTimeout(timeout);
    }

    return () => clearTimeout(timeout); // Cleanup timeout to prevent memory leaks
    
  }, [user, machines, loading]);

  const fetchMachines = async () => {
    try {
      const token = user.token; // Assuming you have a token in the user object
      const response = await getAllMachines(token);
      setMachines(response.results);
    } catch (err) {
      setError('Failed to fetch machines');
    }
  };

  const handleDeleteClick = (machine) => {
    setMachineToDelete(machine);
    setShowConfirm(true);
  };

  const handleConfirmDelete = async () => {
    try {
      await deleteMachine(machineToDelete.id, machineToDelete.groupName, machineToDelete.topicName, user.token);
      setMachines(machines.filter(machine => machine.id !== machineToDelete.id));
      setShowConfirm(false);
      setMachineToDelete(null);
    } catch (err) {
      setError('Failed to delete machine');
      setShowConfirm(false);
      setMachineToDelete(null);
    }
  };

  const handleCancelDelete = () => {
    setShowConfirm(false);
    setMachineToDelete(null);
  };

  return (
    <div className='pageContainer'>
      <NavBar />
      <SideBar />
      {error && <div style={{ color: 'red' }}>{error}</div>}
      <div className='scrollable-table'>
        <table>
          <thead>
            <tr>
              <th>Machine Name</th>
              <th>Topic Name</th>
              <th>Group Name</th>
              {user?.userType === 'IS_ADMIN' && <th>Users</th>}
              {user?.userType === 'IS_ADMIN' && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {machines.length > 0 ? (
              machines.map(machine => (
                <tr key={machine.id}>
                  <td>{machine.machineName}</td>
                  <td>{machine.topicName}</td>
                  <td>{machine.groupName}</td>
                  {user?.userType === 'IS_ADMIN' && (
                    <>
                      {machine?.users.length > 0 ? (<td>{machine.users.join(', ')}</td>) : (<td>No User Assigned</td>)}
                      <td>
                        <button onClick={() => handleDeleteClick(machine)}>Delete</button>
                      </td>
                    </>
                  )}
                </tr>
              ))
            ) : (
              user?.userType === 'IS_ADMIN' ? (
                <tr>
                  <td colSpan="5" style={{ color: "black", textAlign: "center", fontWeight: "bold", fontSize: "20px" }}>NO MACHINES ON BOARDED</td>
                </tr>
              ) : (
                <tr>
                  <td colSpan="3" style={{ color: "black", textAlign: "center", fontWeight: "bold", fontSize: "20px" }}>NO MACHINES ARE ASSIGNED TO YOU</td>
                </tr>
              )
            )}
          </tbody>
        </table>
      </div>

      {showConfirm && (
        <div className="confirm-popup">
          <div className="confirm-popup-content">
            <h3>Confirm Delete</h3>
            <p>Are you sure you want to delete {machineToDelete.machineName}?</p>
            <button onClick={handleConfirmDelete}>Yes, Delete</button>
            <button onClick={handleCancelDelete}>Cancel</button>
          </div>
        </div>
      )}
    </div>
  );
}

export default Machines;