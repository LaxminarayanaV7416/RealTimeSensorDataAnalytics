import {React, useContext} from 'react';
import { Link } from 'react-router-dom';
import { FaHome, FaChartBar, FaUser, FaCog, FaSignOutAlt } from 'react-icons/fa'; 
import '../Styles/SideBar.css';
import { UserContext } from '../UserContext';
function SideBar() {
  const { user, logoutUser } = useContext(UserContext);

  const handleLogout = () => {
    logoutUser(); // Call logoutUser function from UserContext
  };

  return (
    <div className="sidebar">
      <Link to="/dashboard" className="sidebar-link"><FaHome /></Link>
      <Link to="/profile" className="sidebar-link"><FaUser /></Link>
      <Link to="/" className="sidebar-link" onClick={handleLogout}><FaSignOutAlt /></Link>
    </div>
  );
}

export default SideBar;