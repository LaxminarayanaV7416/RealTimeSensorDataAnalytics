import React, { useContext, useEffect, useState } from 'react';
import NavBar from './Components/NavBar';
import SideBar from './Components/SideBar';
import Chart from './Components/Chart';
import './Styles/Dashboard.css';
import { Link } from 'react-router-dom';
import { faCogs } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { UserContext } from './UserContext';
import { faPlus, faUserPlus } from '@fortawesome/free-solid-svg-icons';

const Dashboard = () => {
  const { user } = useContext(UserContext);
  console.log('Dashboard User:', user);
  const [isLoading, setIsLoading] = useState(true);
  useEffect(() => {
    if (user !== null) {
      setIsLoading(false);
    }
  }, [user]);


  if (isLoading) {
    return <div>Loading...</div>; 
  }

  return (
    <div className='pageContainer'>
      <NavBar />
      <SideBar />

      <Link to='/machines' className='dash-link'>
        <div className='machineCountStatus'>
          <h1>Machines Assigned<FontAwesomeIcon icon={faCogs} size='2x' className='machineIcon' /></h1>
          <h5 id='machinesCount'>3</h5>
        </div>
      </Link>

      <div className='chartContainer'>
        <Chart />
      </div>

      {user?.user_type === 'IS_ADMIN' && (
        <div className='adminOptions'>
          <Link to='/add_assign_machine' className='dash-link'>
            <button className='icon-button'>
              <FontAwesomeIcon icon={faPlus} />
              Add Machine
            </button>
          </Link>
          <Link to='/add_assign_machine' className='dash-link'>
            <button className='icon-button'>
              <FontAwesomeIcon icon={faUserPlus} />
              Assign Machine
            </button>
          </Link>
        </div>
      )}
    </div>
  );
};

export default Dashboard;