import React, { useContext, useEffect, useState, useRef } from 'react';
import NavBar from './Components/NavBar';
import SideBar from './Components/SideBar';
import Chart from './Components/Chart';
import './Styles/Dashboard.css';
import { Link, useNavigate } from 'react-router-dom';
import { faCogs } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { UserContext } from './UserContext';
import { faPlus, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { getAllMachines } from './api'; // Assuming this is the correct path to the API function

const Dashboard = () => {
  const intervalIdRef = useRef(null);
  const navigate = useNavigate();
  const { user, loading } = useContext(UserContext);
  const [machines, setMachines] = useState([]);
  const [groups, setGroups] = useState([]);
  const [topics, setTopics] = useState([]);
  const [selectedGroup, setSelectedGroup] = useState('');
  const [selectedTopic, setSelectedTopic] = useState('');
  const [chartType, setChartType] = useState('line');
  const [realTimeData, setRealTimeData] = useState([]);
  const [displayCount, setDisplayCount] = useState(100); // Number of records to display 
  const [loadedCount, setLoadedCount] = useState(0); // Number of records already loaded
  const [chartReset, setChartReset] = useState(false); //variable to define the chart/graph state to change it when needed
  const tableRef = useRef(null);

  const handleChartReset = () => {
    setChartReset(false);
  };

  useEffect(() => {
    const fetchMachines = async () => {
      try {
        const token = user.token;
        const data = await getAllMachines(token);

        // Extract the results from the response
        const machines = data.results;

        if (Array.isArray(machines) && machines.length > 0) {
          setMachines(machines);
          const uniqueGroups = [...new Set(machines.map(machine => machine.groupName))];
          setGroups(uniqueGroups);
        } else {
          setMachines([]);
        }
      } catch (error) {
        console.error('Error fetching machines:', error);
        setMachines([]);
      }
    };
    

    if (!loading) {
      const timeout = setTimeout(() => {
        if (user == null) {
          navigate('/');
        }
      }, 0);

      if (user !== null) {
        
        fetchMachines();
      }

      return () => clearTimeout(timeout);
    }
  }, [user, loading]);

  useEffect(() => {
    if (selectedGroup && selectedTopic) {
      const sendPostRequests = async () => {
        const topic = `${selectedGroup}_${selectedTopic}`; // Replace with your logic for the dynamic topic
        let counter = 0;

        const intervalId = setInterval(async () => {
          // Stop the interval after 100 iterations
          if (counter >= 100) {
            clearInterval(intervalId);
            intervalIdRef.current = null; // Clear the ref
            return;
          }

          // Generate random values for rpm and utilization
          const rpm = Math.floor(Math.random() * 50) + 1; // random value between 1 and 50
          const utilization = Math.floor(Math.random() * 51) + 50; // random value between 50 and 100 (including 100)

          const payload = {
            topic,
            message: JSON.stringify({ rpm, utilization }),
            retained: true,
            qos: 0
          };

          try {
            const response = await fetch('http://127.0.0.1:8080/api/mqtt/publish', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify(payload)
            });
            if (!response.ok) {
              throw new Error(`Error sending record ${counter + 1}: ${response.statusText}`);
            }
            console.log(`Record ${counter + 1} sent successfully`);
          } catch (error) {
            console.error(error);
          }

          counter++;
        }, 1000); // Interval set to 1000ms (1 second)

        // Store the interval ID in the ref
        intervalIdRef.current = intervalId;
      };

      // Clear the previous interval if it exists
      if (intervalIdRef.current) {
        clearInterval(intervalIdRef.current);
      }

      sendPostRequests();
    }

    // Cleanup function to clear the interval on unmount or dependencies change
    return () => {
      if (intervalIdRef.current) {
        clearInterval(intervalIdRef.current);
      }
    };
  }, [selectedGroup, selectedTopic, chartReset]);
  
  // Handle group and topic changes
  const handleGroupChange = (event) => {
    setRealTimeData([]);
    const groupName = event.target.value;
    setSelectedGroup(groupName);
    setSelectedTopic('');

    const filteredTopics = machines.filter(machine => machine.groupName === groupName);
    setTopics(filteredTopics);
  };

  const handleTopicChange = (event) => {
    if (selectedTopic != "") {
      setChartReset(true);
    }
    setSelectedTopic(event.target.value);
  };

  const handleChartTypeChange = (event) => {
    setChartType(event.target.value);
  };

  // Download CSV function
  const downloadCSV = () => {
    // Include headers for CSV
    const headers = ['Timestamp', ...Object.keys(realTimeData[0]).filter(key => key !== 'timestamp')];

    // Convert realTimeData to CSV format
    const csvContent = "data:text/csv;charset=utf-8,"
      + headers.join(',') + '\n'
      + realTimeData.map(row => {
        return [
          row.timestamp.toLocaleTimeString(), // Ensure timestamp is formatted as needed
          ...headers.slice(1).map(header => row[header])
        ].join(',');
      }).join('\n');

    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', selectedGroup + "_" + selectedTopic + " data.csv");
    document.body.appendChild(link);
    link.click();
  };

  // Handle scroll event to load more rows
  const handleScroll = () => {
    const tableWrapper = tableRef.current;
    if (tableWrapper) {
      const { scrollTop, scrollHeight, clientHeight } = tableWrapper;
      if (scrollTop + clientHeight >= scrollHeight) {
        // User has scrolled to the bottom, load more rows
        setDisplayCount(displayCount + 8);
      }
    }
  };



  return (
    <div className='pageContainer'>
      <NavBar />
      <SideBar />
      <Link to='/machines' className='dash-link'>
        <div className='machineCountStatus'>
          <h1> {user?.userType != 'IS_ADMIN' ? ('Machines Assigned') : ('Number of Machines')}<FontAwesomeIcon icon={faCogs} size='2x' className='machineIcon' /></h1>
          <h5 id='machinesCount'> {machines.length != 0 ? (machines.length) : (0)} </h5>
        </div>
      </Link>

      <div className='chartContainer'>
        <div className='controls'>
          <select onChange={handleGroupChange} value={selectedGroup}>
            <option value=''>Select Group</option>
            {groups.map((group) => (
              <option key={group} value={group}>
                {group}
              </option>
            ))}
          </select>

          <select onChange={handleTopicChange} value={selectedTopic} disabled={!selectedGroup}>
            <option value=''>Select Topic</option>
            {selectedGroup && topics.map((topic) => (
              <option key={topic.topicName} value={topic.topicName}>
                {topic.topicName}
              </option>
            ))}
          </select>

          <h5 style={{ marginTop: "5px", padding: "0", width: "8rem", marginLeft: "5%", marginRight: "1%" }}>Select Chart Type</h5>
          <select onChange={handleChartTypeChange} value={chartType}>
            <option value='line'>Line</option>
            <option value='bar'>Bar</option>
          </select>
        </div>
        {selectedGroup && selectedTopic ? (
          <Chart groupName={selectedGroup} topicName={selectedTopic} chartType={chartType} setRealTimeData={setRealTimeData} isChartReset = {chartReset} handleChartReset={handleChartReset} />
        ) : (
          <h1 style={{ marginTop: "25%" }}>
            {selectedGroup ? "Please select the Sensor's topic above" : "Please select the Sensor's group and topic above"}
          </h1>
        )}
      </div>
      <button className='download-button' onClick={downloadCSV} disabled={realTimeData.length === 0} hidden={realTimeData.length === 0}>
        Download CSV
      </button>


      {/* Real-time Data Table */}
      <div className='realTimeDataTable' hidden={realTimeData.length === 0}>
        {/* Download CSV Button */}

        <div className='tableWrapper' ref={tableRef} onScroll={handleScroll} style={{ width: '100%', overflowX: 'auto', maxHeight: '400px', overflowY: 'auto' }}>
          <table>
            <thead>
              <tr>
                <th>Timestamp</th>
                {realTimeData.length > 0 && Object.keys(realTimeData[0]).sort().map((key) => (
                  key !== 'timestamp' && <th key={key}>{key}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {realTimeData.slice(0, displayCount).map((data, index) => {
                let dataKeys = Object.keys(data).filter(key => key !== 'timestamp').sort();
                return (
                  <tr key={index}>
                    <td>{data.timestamp.toLocaleTimeString()}</td>
                    {dataKeys.map((key) => (
                      <td key={key}>{data[key]}</td>
                    ))}
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        </div>


        {/* Admin Options */}
        {user?.userType === 'IS_ADMIN' && (
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