import {React, useContext, useState, useEffect} from 'react';
import './Styles/Profile.css';
import NavBar from './Components/NavBar';
import SideBar from './Components/SideBar';
import { UserContext } from './UserContext';
import { useNavigate } from 'react-router-dom';


const Profile = () => {
  const navigate = useNavigate();
  const { user, loading } = useContext(UserContext);
  const [username, setUserName] = useState('');
  const [email, setEmail] = useState('');

  
  useEffect(() => {
    const timeout = setTimeout(() => {
      if (user == null) {
        navigate('/');
      } else {
        setUserName(user.username);
        setEmail(user.email);
      }
    }, 0);

    if (!loading) {
      const timeout = setTimeout(() => {
        if (user == null) {
          navigate('/');
        }
      }, 0);
      return () => clearTimeout(timeout);
    }
}, [user, loading]);


  

return (
    <div>
        <NavBar />
        <SideBar />
        
        <div className="profile-container">
            <h3>My Profile</h3>
        <div className="profile-field">
            <label htmlFor="username">Username:</label>
            <input
            type="text"
            id="username"
            value={username}
            disabled
            />
        </div>
        <div className="profile-field">
            <label htmlFor="email">Email:</label>
            <input
            type="email"
            id="email"
            value={email}
            disabled
            />
        </div>
        </div>
    </div>
  );
};

export default Profile;