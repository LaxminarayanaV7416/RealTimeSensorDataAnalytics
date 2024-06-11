package com.psd.RealTimeSensorDataAnalyticsBackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "machine_user")
public class UsersMachineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Long userId;
    private Long machineId;
    private String machineName;

}