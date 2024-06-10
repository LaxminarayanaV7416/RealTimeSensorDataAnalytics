package com.psd.RealTimeSensorDataAnalyticsBackend.utils;

import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.springframework.context.ConfigurableApplicationContext;

import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.ActiveMQMqttBeans;
import com.psd.RealTimeSensorDataAnalyticsBackend.exceptions.MqttException;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.TopicRepository;

public class MiscellenousCallsPostInitofSpringApplication {

    public static void initiateAllTopics(ConfigurableApplicationContext context){
        		TopicRepository topicRepository = context.getBean(TopicRepository.class);
		IMqttClient mqttClient = ActiveMQMqttBeans.getInstance();

		try {
			List<TopicsModel> allTopics = topicRepository.findAll();
			for (TopicsModel topic : allTopics) {
				mqttClient.subscribe(topic.getGroupName() + "_" + topic.getTopicName());
			}
		} catch (MqttException exception) {
			exception.printStackTrace();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
    }
    
}
