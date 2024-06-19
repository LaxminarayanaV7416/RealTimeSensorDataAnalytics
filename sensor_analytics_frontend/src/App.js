import './Styles/App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import SignupPage from './SignupPage';
import LoginPage from './LoginPage';
import Dashboard from './Dashboard';
import Machines from './Machines';
import MachinesAdd from './MachinesAdd';
import ProfilePage from './Profile';
import AdminSignupPage from './AdminSignupPage'

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/signup" element={<SignupPage/>}/>
          <Route path="/" element={<LoginPage/>}/>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/machines" element={<Machines />} />
          <Route path="/add_assign_machine" element={<MachinesAdd />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/RSA_ADMIN_SECURE_TEAM_AIR(3)_SIGNUP" element={<AdminSignupPage />} />
        </Routes>
    </Router>               
  );
}

export default App;
