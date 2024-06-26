package com.psd.RealTimeSensorDataAnalyticsBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;

@Repository
public interface TopicRepository extends JpaRepository<TopicsModel, Long> {
    TopicsModel findByTopicName(String topicName);
    List<TopicsModel> findByGroupName(String groupName);
    TopicsModel findByMachineName(String machineName);
    TopicsModel findByGroupNameAndTopicName(String groupName, String topicName);
}

