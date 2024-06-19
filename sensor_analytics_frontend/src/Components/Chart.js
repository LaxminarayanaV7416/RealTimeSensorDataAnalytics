import { useState, useEffect, useRef } from "react";
import { Line, Bar } from 'react-chartjs-2';
import useWebSocket from 'react-use-websocket';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
    TimeScale,
} from 'chart.js';
import 'chartjs-adapter-date-fns';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
    TimeScale
);

const Chart = ({ groupName, topicName, chartType, setRealTimeData, isChartReset, handleChartReset }) => {
    const [useWebsocketData, setUseWebsocketData] = useState([]);
    const [chartData, setChartData] = useState({
        labels: [],
        datasets: [],
    });

    const colorPalette = [
        'rgba(75, 192, 192, 1)',
        'rgba(255, 99, 132, 1)',
        'rgba(54, 162, 235, 1)',
        'rgba(255, 206, 86, 1)',
        'rgba(153, 102, 255, 1)',
        'rgba(255, 159, 64, 1)',
    ];

    const socketUrl = `ws://localhost:8080/topic?groupName=${groupName}&topicName=${topicName}`;

    const processMessages = (event) => {
        try {
            const parsedData = JSON.parse(event.data);
            const timestamp = new Date();

            setUseWebsocketData((prevData) => {
                if (isChartReset) {
                    prevData = [];
                    handleChartReset();
                }
                let recievedData = { ...parsedData, timestamp };
                let updatedData = [];
                if(prevData.length > 0){ 
                    let recievedKeys = Object.keys(recievedData); 
                    let prevDataKeys = Object.keys(prevData[0]);
                    let newKey = recievedKeys.filter((element) => !prevDataKeys.includes(element));
                    let newData = [];
                    if(newKey.length > 0){
                        for(let i = 0; i < prevData.length; i++){
                            let temp = prevData[i];
                            for(let j = 0; j < newKey.length; j++){
                                temp[newKey[j]] = 0;
                            }
                            newData.push(temp);
                        }

                        newKey = prevDataKeys.filter((element) => !recievedKeys.includes(element));
                        if(newKey.length > 0){
                            for(let i = 0; i < newKey.length; i++){
                                recievedData[newKey[i]] = 0;
                            }
                        }
                        updatedData = [...newData, recievedData];
                    } else {
                        newKey = prevDataKeys.filter((element) => !recievedKeys.includes(element));
                        if(newKey.length > 0){
                            for(let i = 0; i < newKey.length; i++){
                                recievedData[newKey[i]] = 0;
                            }
                        }
                        updatedData = [...prevData, recievedData];
                    }
                } else {
                    updatedData = [recievedData];
                }
                
                updateChart(updatedData);
                return updatedData;
            });
        } catch (e) {
            console.log("Data is not in format of JSON");
        }
    };

    const updateChart = (data) => {
        const datasetNames = Object.keys(data[0]).filter(key => key !== 'timestamp');

        setChartData({
            labels: data.map(item => item.timestamp),
            datasets: datasetNames.map((name, index) => ({
                label: name,
                data: data.map(item => item[name]),
                borderColor: colorPalette[index % colorPalette.length],
                backgroundColor: colorPalette[index % colorPalette.length].replace('1)', '0.2)'),
            })),
        });

        // Pass real-time data to parent component (Dashboard)
        setRealTimeData(data);
    };

    const { lastJsonMessage, sendJsonMessage, getWebSocket } = useWebSocket(socketUrl, {
        onOpen: () => console.log('WebSocket connection opened.'),
        onClose: () => console.log('WebSocket connection closed.'),
        shouldReconnect: (closeEvent) => true,
        reconnectInterval: 3000,
        reconnectAttempts: 1,
        onMessage: processMessages,
        share: true, // Share the WebSocket connection between components
    });

    useEffect(() => {
        const currentWebSocket = getWebSocket();
        return () => {
            if (currentWebSocket) {
                currentWebSocket.close();
            }
        };
    }, [socketUrl]);

    const options = {
        scales: {
            x: {
                type: 'time',
                time: {
                    unit: 'second',
                    tooltipFormat: 'HH:mm:ss',
                    displayFormats: {
                        second: 'HH:mm:ss'
                    }
                },
                title: {
                    display: true,
                    text: 'Time'
                }
            },
            y: {
                title: {
                    display: true,
                    text: 'Values'
                }
            }
        }
    };

    const chartComponents = {
        line: Line,
        bar: Bar,
    };

    const ChartComponent = chartComponents[chartType] || Line;

    return (
        <div>
            <h4 style={{color : "white"}}>Real-Time Machine Data Chart</h4>
            <ChartComponent data={chartData} options={options} />
        </div>
    );
};

export default Chart;