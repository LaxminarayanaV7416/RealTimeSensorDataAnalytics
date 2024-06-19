package com.psd.RealTimeSensorDataAnalyticsBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.UsersMachineModel;

@Repository
public interface UsersMachineRepository extends JpaRepository<UsersMachineModel, Long> {
    List<UsersMachineModel> findByUsername(String username);
    void deleteByMachineName(String machineName);
    List<UsersMachineModel> findByMachineName(String machineName);
}