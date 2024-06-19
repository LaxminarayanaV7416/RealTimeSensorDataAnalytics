import React, { useState, useContext, useEffect } from 'react';
import NavBar from './Components/NavBar';
import SideBar from './Components/SideBar';
// import { Link } from 'react-router-dom';
import { UserContext } from './UserContext';
import './Styles/MachinesAddPage.css';
import { onboardNewSensor, assignMachineToUser, getAllMachines, getAllUsers } from './api';
import { useNavigate } from 'react-router-dom';

const MachinesAdd = () => {
    const navigate = useNavigate();
    const { user, loading } = useContext(UserContext);
    const [groupName, setGroupName] = useState('');
    const [topicName, setTopicName] = useState('');
    const [userNames, setUserNames] = useState([]);
    const [machineNames, setMachineNames] = useState([]);
    const [topicNames, setTopicNames] = useState([]);
    const [selectedGroup, setSelectedGroup] = useState('');
    const [selectedTopic, setSelectedTopic] = useState('');
    const [selectedUserName, setSelectedUserName] = useState('');
    const [errorMessage1, seterrorMessage1] = useState('');
    const [errorMessage2, seterrorMessage2] = useState('');
    const [isTopicDropdownDisabled, setIsTopicDropdownDisabled] = useState(true); // State for topic dropdown enablement

    useEffect(() => {
        const timeout = setTimeout(() => {
            if (user == null) {
                navigate('/');
            }
            else {
                fetchData();
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
    }, [user, selectedGroup, loading]);

    const fetchData = async () => {
        try {
            const data = await getAllMachines(user.token);
            const userData = await getAllUsers(user.token);
            const uniqueGroups = Array.from(new Set(data.results.map(machine => machine.groupName)));
            setMachineNames(uniqueGroups);
            // If a group is selected, filter topics
            if (selectedGroup) {
                const filteredTopics = data.results
                    .filter(machine => machine.groupName === selectedGroup)
                    .map(machine => machine.topicName);
                setTopicNames(filteredTopics);

                const filteredUsers = userData.results
                    .map(user => user.username);
                setUserNames(filteredUsers);
            } else {
                // If no group selected, set empty array for topics
                setTopicNames([]);
                setUserNames([]);
            }
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const handleMachineAdd = async (event) => {
        event.preventDefault();
        seterrorMessage1('');
    
        // Check if groupName or topicName is empty
        if (!groupName) {
            seterrorMessage1('Please enter Group Name');
            return;
        }
        if (!topicName) {
            seterrorMessage1('Please enter Topic Name');
            return;
        }
    
        const machineName = `${groupName}_${topicName}`;
    
        try {
            await onboardNewSensor(groupName, topicName, machineName, user.token);
            console.log('Machine added successfully');
            // Reset input values
            setGroupName('');
            setTopicName('');
            // Show success alert
            alert('Machine added successfully');
            // Fetch updated data
            fetchData();
        } catch (error) {
            seterrorMessage1(error.message);
        }
    };



    const handleMachineAssign = async (event) => {
        event.preventDefault();
        seterrorMessage2('');
    
        // Check if selectedGroup, selectedTopic, or userName is empty
        if (!selectedGroup) {
            seterrorMessage2('Please select a Group');
            return;
        }
        if (!selectedTopic) {
            seterrorMessage2('Please select a Topic');
            return;
        }
        if (!selectedUserName) {
            seterrorMessage2('Please select Username');
            return;
        }
    
        const machineName = `${selectedGroup}_${selectedTopic}`;
    
        try {
            await assignMachineToUser(selectedUserName, machineName, user.token);
            console.log('Machine assigned successfully');
            // Reset input values
            setSelectedGroup('');
            setSelectedTopic('');
            setSelectedUserName('');
            // Show success alert
            alert('Machine assigned successfully');
        } catch (error) {
            seterrorMessage2(error.message);
        }
    };

    const handleGroupChange = (e) => {
        const selected = e.target.value;
        setSelectedGroup(selected);
        // Enable topic dropdown if a group is selected
        setIsTopicDropdownDisabled(selected === '');
        // Fetch topics for the selected group
        const filteredTopics = selected !== ''
            ? topicNames.filter(topic => topic.groupName === selected)
            : [];
        setTopicNames(filteredTopics);
    };

    
      
    return (
        
        <div className='pageContainer'>
            <NavBar />
            <SideBar />
            <div className='sensorDetails_Container'>
                <input
                    name='groupName'
                    value={groupName}
                    placeholder='Enter Group Name'
                    onChange={(e) => setGroupName(e.target.value)}
                />

                <input
                    name='topicName'
                    value={topicName}
                    placeholder='Enter Topic Name'
                    onChange={(e) => setTopicName(e.target.value)}
                />

                <button onClick={handleMachineAdd}> Add Machine </button>
                {errorMessage1 && <div className="error1">{errorMessage1}</div>}
            </div>

            <div className='assignSensors_Container'>
                <select id="groups" value={selectedGroup} onChange={handleGroupChange}>
                    <option value="" disabled>Select a Group</option>
                    {machineNames.map((machine, index) => (
                        <option key={index} value={machine}>
                            {machine}
                        </option>
                    ))}
                </select>

                <select id="topics" value={selectedTopic} onChange={(e) => setSelectedTopic(e.target.value)} disabled={isTopicDropdownDisabled}>
                    <option value="" disabled>Select a Topic</option>
                    {topicNames.map((topic, index) => (
                        <option key={index} value={topic}>
                            {topic}
                        </option>
                    ))}
                </select>

                <select id="topics" value={selectedUserName} onChange={(e) => setSelectedUserName(e.target.value)} disabled={isTopicDropdownDisabled}>
                    <option value="" disabled>Select a UserName</option>
                    {userNames.map((topic, index) => (
                        <option key={index} value={topic}>
                            {topic}
                        </option>
                    ))}
                </select>

                <button onClick={handleMachineAssign}> Assign Machine to User </button>
                {errorMessage2 && <div className="error2">{errorMessage2}</div>}
            </div>
        </div>
    );
}

export default MachinesAdd;