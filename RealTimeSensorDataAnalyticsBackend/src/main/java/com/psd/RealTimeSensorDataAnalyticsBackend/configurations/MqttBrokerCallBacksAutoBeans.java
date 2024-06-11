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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Configuration
public class MqttBrokerCallBacksAutoBeans implements MqttCallback {

    @Autowired
    private WebSocketBeans mqttWebSocketHandler;

    @Autowired
    private CredentialsConfBean credentialsConf;

    private static IMqttClient mqttClient;

    public MqttBrokerCallBacksAutoBeans() {}

    public static IMqttClient getInstance(String mqttInstanceURL, String mqttPublisherID,
            String mqttUserName, String mqttPassword) {
        try {
            if (mqttClient == null) {
                mqttClient = new MqttClient(mqttInstanceURL, mqttPublisherID);
            }

            MqttConnectOptions options = new MqttConnectOptions();
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

}
