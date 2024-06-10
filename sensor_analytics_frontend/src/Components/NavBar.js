import React, { useState, useEffect, useRef } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { FaUserCircle } from 'react-icons/fa';
import { FiChevronDown } from 'react-icons/fi';
import Clock from './Clock';
import '../Styles/NavBar.css';
import Logo from '../public/icons/logo.png';

function NavBar() {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const dropdownRef = useRef(null);

  const handleDropdownClick = () => {
    setDropdownOpen(!dropdownOpen);
  };

  const handleClickOutside = (event) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
      setDropdownOpen(false);
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <div className="brand"><img src={Logo} /></div>
        <NavLink exact to="/dashboard" className = "nav-link" activeclassname="active">Home</NavLink>
        <NavLink exact to="/graphs" className = "nav-link" activeclassname="active">Graphs</NavLink>
      </div>
      <div className="navbar-right">
        <Clock />
        <div className="dropdown" ref={dropdownRef}>
          <div className="dropdown-toggle" onClick={handleDropdownClick}>
            <FaUserCircle size={24} />
            <FiChevronDown size={20} />
          </div>
          {dropdownOpen && (
            <div className="dropdown-content">
              <Link to="/profile" className="dropdown-link">Profile</Link>
              <Link to="/settings" className="dropdown-link">Settings</Link>
              <Link to="/" className="dropdown-link">Logout</Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}

export default NavBar;