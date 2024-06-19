package com.psd.RealTimeSensorDataAnalyticsBackend.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.TopicRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;


@Configuration
@EnableWebSocket
public class WebSocketBeans implements WebSocketHandler, WebSocketConfigurer {

    private Map<String, List<WebSocketSession>> requestedSessionInfo = new HashMap<>();

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TopicRepository topicRepository;

    private String getTokenFromSessionHeader(WebSocketSession session){
        String token = null;
        List<String> tokens = session.getHandshakeHeaders().getValuesAsList("Authorization");
        if(tokens.size()>0){
            token = tokens.get(0);
        }
        return token;
    }

    private boolean performSocketConnectionAuthorizationCheck(WebSocketSession session){
        String token = this.getTokenFromSessionHeader(session);
        if(Objects.nonNull(token)){
            String strippedToken = token.substring(7);
            return jwtTokenUtil.validateToken(strippedToken);
        }
        return false;
    }

    private String getTopicAndGroupName(WebSocketSession session){
        String[] queryParams = session.getUri().getQuery().split("&");
        String groupName = null;
        String topicName = null;
        for(String param : queryParams){
            String[] groupTopic = param.split("=");
            if(groupTopic[0].equals("groupName")){
                groupName = groupTopic[1];
            }
            if(groupTopic[0].equals("topicName")){
                topicName = groupTopic[1];
            }
        }
        return groupName+"_"+topicName;
    }

    public boolean performDatabaseCheckForMachineExists(String machineName){
        TopicsModel topicResult = topicRepository.findByMachineName(machineName);
        if(Objects.nonNull(topicResult)){
            return true;
        }
        return false;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // work here with methods getUri,getHandshakeHeaders and form the dictionary to note the object type and to which we can send messages
        String topicAndGroupName = this.getTopicAndGroupName(session);
        if(this.performDatabaseCheckForMachineExists(topicAndGroupName)){
            List<WebSocketSession> allSessions = requestedSessionInfo.get(topicAndGroupName);
            if(Objects.nonNull(allSessions)){
                allSessions.add(session);
            } else {
                allSessions = new ArrayList<>();
                allSessions.add(session);
            }
            requestedSessionInfo.put(topicAndGroupName, allSessions);
            System.out.println("Connection established on session: " + session.getId());
        } else {
            session.close();
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String gameId = (String) message.getPayload();
        session.sendMessage(new TextMessage("Started processing game: " + gameId));
        Thread.sleep(1000);
        session.sendMessage(new TextMessage("Completed processing game: " + gameId));
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("Exception occured: {} on session: {}" + exception.getMessage() + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Connection closed on session: {} with status: {}" + session.getId() + closeStatus.getCode());
        // work here on removing the session from the hashmap such that there wont be any error of mqtt closing

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // Method to send a message to the connected clients
    public void sendMessageToClients(String message, String topicName) throws Exception {
        // we are looping things so we can easily make segegrations and send messages
        List<WebSocketSession> sessions = requestedSessionInfo.get(topicName);
        if(Objects.nonNull(sessions)){
            List<WebSocketSession> alteredSessions = new ArrayList<>();
            for(WebSocketSession session : sessions){
                if(session.isOpen()){
                    session.sendMessage(new TextMessage(message));
                    alteredSessions.add(session);
                    // this mechanism will be removing the closed sessions
                }
            }
            requestedSessionInfo.put(topicName, alteredSessions);
        }
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(this, "/topic").setAllowedOrigins("*");
    }
}