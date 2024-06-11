package com.psd.RealTimeSensorDataAnalyticsBackend.services;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.MqttPublishModel;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private MqttClient mqttClient;

    // Assume mqttClient is initialized somewhere

    public void publish(MqttPublishModel mqttPublishModel) throws MqttException {
        MqttMessage message = new MqttMessage(mqttPublishModel.getMessage().getBytes());
        message.setQos(mqttPublishModel.getQos());
        message.setRetained(mqttPublishModel.getRetained());
        mqttClient.publish(mqttPublishModel.getTopic(), message);
    }
}
