package com.psd.RealTimeSensorDataAnalyticsBackend.configurations;

import java.util.Objects;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.psd.RealTimeSensorDataAnalyticsBackend.repository.TopicRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Configuration
public class MqttBrokerCallBacksAutoBeans implements MqttCallback {

    @Autowired
    private WebSocketBeans mqttWebSocketHandler;

    @Autowired
    private CredentialsConfBean credentialsConf;

    @Autowired
    private TopicRepository topicRepository;

    private static IMqttClient mqttClient;

    private static MqttConnectOptions options;

    public MqttBrokerCallBacksAutoBeans() {
    }

    public static IMqttClient getInstance(String mqttInstanceURL, String mqttPublisherID,
            String mqttUserName, String mqttPassword) {
        try {
            if (mqttClient == null) {
                mqttClient = new MqttClient(mqttInstanceURL, mqttPublisherID);
            }

            options = new MqttConnectOptions();
            options.setUserName(mqttUserName);
            options.setPassword(mqttPassword.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            if (!mqttClient.isConnected()) {
                mqttClient.connect(options);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return mqttClient;
    }

    @PostConstruct
    public void initializeMqttClient() {
        mqttClient = getInstance(credentialsConf.getMqttServerURL(), credentialsConf.getServerID(),
                credentialsConf.getUsername(), credentialsConf.getPassword());
        mqttClient.setCallback(this);
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection to MQTT Broker Lost!!");
        System.out.println(cause.getMessage() + "<<<<<=======>>>>" + cause.getCause());
        System.out.println("Attempting to reconnect!");
        try {
            while(!mqttClient.isConnected()){
                mqttClient.reconnect();
                Thread.sleep(10000);
                System.out.println("Reconnecting!!");
            }
            System.out.println("Reconnection Sucessful!");
            mqttClient.setCallback(this);
            resubscribeToDataBaseTopics();
        } catch (MqttException | InterruptedException exception) {
            System.out.println("attempt to reconnect failed!");
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("TOPIC MESSAGE RECIEVED FROM " + topic);
        String content = new String(message.getPayload());
        if (Objects.nonNull(mqttWebSocketHandler)) {
            mqttWebSocketHandler.sendMessageToClients(content, topic);
        }
    };

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    public void resubscribeToDataBaseTopics() {
        try {
            List<TopicsModel> allTopics = topicRepository.findAll();
            for (TopicsModel topicFromDB : allTopics) {
                mqttClient.subscribe(topicFromDB.getGroupName() + "_" + topicFromDB.getTopicName());
            }
        } catch (MqttException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
