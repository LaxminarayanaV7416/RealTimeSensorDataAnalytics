package com.psd.RealTimeSensorDataAnalyticsBackend.controllers;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.MqttPublishModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.services.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    @Autowired
    private MqttService mqttService;

    @PostMapping("/publish")
    public ResponseEntity<?> publish(@Validated @RequestBody MqttPublishModel mqttPublishModel) {
        try {
            mqttService.publish(mqttPublishModel);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
