import React from 'react';
import { Link } from 'react-router-dom';
import { FaHome, FaChartBar, FaUser, FaCog, FaSignOutAlt } from 'react-icons/fa'; 
import '../Styles/SideBar.css';

function SideBar() {
  return (
    <div className="sidebar">
      <Link to="/dashboard" className="sidebar-link"><FaHome /></Link>
      <Link to="/profile" className="sidebar-link"><FaUser /></Link>
      <Link to="/" className="sidebar-link"><FaSignOutAlt /></Link>
    </div>
  );
}

export default SideBar;