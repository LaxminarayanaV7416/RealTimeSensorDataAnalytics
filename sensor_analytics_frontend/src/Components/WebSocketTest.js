// file created for test purpose


// import { useState } from "react";
// import useWebSocket, { ReadyState } from 'react-use-websocket';

// const WSS_FEED_URL = "ws://localhost:8080/topic?groupName=SLU&topicName=ECM"


// const WebSocketTest = () => {

//     function processMessages(event){
//         console.log("WEBSOCKET MESSAGE RECIEVED",event.data);
//         useWebsocketData.push(event.data)
//         setUseWebsocketData(useWebsocketData);
//         console.log("After Data = ", useWebsocketData);
//     }

//     const [useWebsocketData, setUseWebsocketData] = useState([]);

//     const { sendJsonMessage, getWebSocket } = useWebSocket(WSS_FEED_URL, {
//         onOpen: () => console.log('WebSocket connection opened.'),
//         onClose: () => console.log('WebSocket connection closed.'),
//         shouldReconnect: (closeEvent) => false,
//         reconnectInterval: 3000, // milliseconds
//         reconnectAttempts: 1, // or set to a suitable number
//         onMessage: (event) =>  processMessages(event)
//         });

//     return <div>Hello</div>;

// }

// export default WebSocketTest;


