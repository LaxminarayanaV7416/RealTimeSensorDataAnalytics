package com.psd.RealTimeSensorDataAnalyticsBackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "MQTT_Topics")
public class TopicsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String topicName;

    private String groupName;

}