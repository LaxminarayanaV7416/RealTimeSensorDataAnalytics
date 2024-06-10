import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

const Chart = () => {
  const [data, setData] = useState({
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
      {
        label: 'Machine 1',
        data: [],
        fill: false,
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1,
      },
      {
        label: 'Machine 2',
        data: [],
        fill: false,
        borderColor: 'rgb(255, 99, 132)',
        tension: 0.1,
      },
    ],
  });
  const [updateCount, setUpdateCount] = useState(0); 

  const generateRandomData = () => {
    setData((prevData) => {
      const newData = { ...prevData };
      newData.datasets.forEach((dataset) => {
        dataset.data.push(Math.floor(Math.random() * 100));
      });
      return newData;
    });
    setUpdateCount(updateCount + 1); 
  };

  useEffect(() => {
    const intervalId = setInterval(generateRandomData, 2000);
    return () => clearInterval(intervalId);
  }, []); 

  return (
    <div>
      <h2>Chart</h2>
      <Line data={data} key={updateCount} options={options} />
    </div>
  );
};

const options = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top',
    },
    title: {
      display: true,
      text: 'Machine Data Over Time',
    },
  },
};

export default Chart;