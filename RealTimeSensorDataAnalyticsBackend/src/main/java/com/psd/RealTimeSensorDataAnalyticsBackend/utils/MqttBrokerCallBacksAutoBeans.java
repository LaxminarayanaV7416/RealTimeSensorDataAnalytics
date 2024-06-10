package com.psd.RealTimeSensorDataAnalyticsBackend.utils;

import java.util.Objects;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.ActiveMQMqttBeans;
import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.WebSocketBeans;


@Component
public class MqttBrokerCallBacksAutoBeans implements MqttCallback {

    @Autowired
    private WebSocketBeans mqttWebSocketHandler;

    public MqttBrokerCallBacksAutoBeans() {
            IMqttClient mqttClient = ActiveMQMqttBeans.getInstance();
            mqttClient.setCallback(this);        
    }

    @Override
    public void connectionLost(Throwable cause){
        System.out.println("Connection to MQTT Broker Lost!!");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("TOPIC MESSAGE RECIEVED FROM "+topic);
        String content = new String(message.getPayload());
        if(Objects.nonNull(mqttWebSocketHandler)){
            mqttWebSocketHandler.sendMessageToClients(content, topic);
        }
    };
    
    @Override
    public void deliveryComplete(IMqttDeliveryToken token){}

}
