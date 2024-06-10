package com.psd.RealTimeSensorDataAnalyticsBackend.controllers;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.ActiveMQMqttBeans;
import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.WebSocketBeans;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.TopicRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;

@RestController
@RequestMapping
public class OnBoardingSensorController {

    @Autowired
    public TopicRepository topicRepository;

    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    @Autowired
    public WebSocketBeans mqttWebSocketHandler;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/onboard-new-sensor")
    public ResponseEntity<Object> onBoardNewSensorAsTopic(
            @RequestBody TopicsModel topicsModel,
            @RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, String> result = new HashMap<>();

        if (token == null) {
            result.put("Message", "Authorization Token Is required to Proceed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        } else {
            String realToken = token.substring(7);
            boolean tokenCheckResult = jwtTokenUtil.validateToken(realToken);
            if (tokenCheckResult) {
                if (topicRepository.save(topicsModel).getId() > 0) {
                    try {
                        IMqttClient mqttClient = ActiveMQMqttBeans.getInstance();
                        mqttClient.subscribe(topicsModel.getGroupName() + "_" + topicsModel.getTopicName());
                        result.put("Message", "Sensor " + topicsModel.getTopicName() + " onboarded Succefully");
                        return ResponseEntity.status(HttpStatus.CREATED).body(result);
                    } catch (MqttException exception) {
                        result.put("Message", "Failed to subscribe to MQTT topic: " + exception.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
                    }
                }
            } else {
                result.put("Message",
                        "Sensor " + topicsModel.getTopicName() + " exsists already so the onboarding is failed");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
            }
        }
        result.put("Message", "Internal Server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);

    }

    @GetMapping("/get-all-sensors/{groupName}")
    public ResponseEntity<Object> getMethodName(@PathVariable String groupName) {
        Map<String, String> result = new HashMap<>();
        List<TopicsModel> topicResults = topicRepository.findByGroupName(groupName);
        if (Objects.nonNull(topicResults)) {
            Map<String, List<TopicsModel>> response = new HashMap<>();
            response.put("Results", topicResults);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        result.put("Message", "Group not exisits");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
