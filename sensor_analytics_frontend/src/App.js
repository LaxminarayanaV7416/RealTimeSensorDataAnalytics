import './Styles/App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import SignupPage from './SignupPage';
import LoginPage from './LoginPage';
import Dashboard from './Dashboard';
import Machines from './Machines';
import Graphs from './Graphs';
import MachinesAdd from './MachinesAdd';

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/signup" element={<SignupPage/>}/>
          <Route path="/" element={<LoginPage/>}/>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/machines" element={<Machines />} />
          <Route path="/add_assign_machine" element={<MachinesAdd />} />
          <Route path="/graphs" element={<Graphs />} />
        </Routes>
    </Router>               
  );
}

export default App;
