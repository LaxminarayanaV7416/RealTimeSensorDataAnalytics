package com.psd.RealTimeSensorDataAnalyticsBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.UsersModel;


@Repository
public interface UserRepository extends JpaRepository<UsersModel, Long> {
    UsersModel findByUsername(String username);
    UsersModel findByEmail(String email);
}