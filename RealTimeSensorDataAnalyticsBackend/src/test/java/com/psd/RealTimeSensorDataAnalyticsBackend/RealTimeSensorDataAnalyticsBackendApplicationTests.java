// package com.psd.RealTimeSensorDataAnalyticsBackend;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.*;

// import org.eclipse.paho.client.mqttv3.IMqttClient;
// import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
// import org.eclipse.paho.client.mqttv3.MqttException;
// import org.eclipse.paho.client.mqttv3.MqttMessage;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.TestConfiguration;
// import org.springframework.context.annotation.Bean;
// import org.springframework.test.context.TestPropertySource;

// import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.CredentialsConfBean;
// import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.MqttBrokerCallBacksAutoBeans;
// import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.WebSocketBeans;
// import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;
// import com.psd.RealTimeSensorDataAnalyticsBackend.repository.TopicRepository;

// import java.util.Arrays;
// import java.util.List;

// @SpringBootTest
// @ExtendWith(MockitoExtension.class)
// @TestPropertySource(properties = {
//         "spring.mqtt.host=localhost",
//         "spring.mqtt.port=1883",
//         "spring.mqtt.serveridentity=testServer",
//         "spring.mqtt.protocol=tcp",
//         "spring.mqtt.username=testUser",
//         "spring.mqtt.password=testPass"
// })
// public class RealTimeSensorDataAnalyticsBackendApplicationTests {

//     @Autowired
//     private CredentialsConfBean credentialsConfBean;

//     @Mock
//     private WebSocketBeans mqttWebSocketHandler;

//     @Mock
//     private TopicRepository topicRepository;

//     @Mock
//     private IMqttClient mqttClient;

//     @InjectMocks
//     private MqttBrokerCallBacksAutoBeans mqttBrokerCallBacksAutoBeans;

//     @BeforeEach
//     public void setUp() throws MqttException {
//         MockitoAnnotations.openMocks(this);

//         when(credentialsConfBean.getMqttServerURL()).thenReturn("tcp://localhost:1883");
//         when(credentialsConfBean.getServerID()).thenReturn("testServer");
//         when(credentialsConfBean.getUsername()).thenReturn("testUser");
//         when(credentialsConfBean.getPassword()).thenReturn("testPass");

//         mqttBrokerCallBacksAutoBeans.initializeMqttClient();

//         // Mocking IMqttClient methods that will be called in initializeMqttClient
//         when(mqttClient.isConnected()).thenReturn(false);
//         doNothing().when(mqttClient).connect(any(MqttConnectOptions.class));
//         doNothing().when(mqttClient).setCallback(any());
//     }

//     // Test cases for CredentialsConfBean
//     @Test
//     public void testMqttServerURL() {
//         String expectedUrl = "tcp://localhost:1883";
//         assertEquals(expectedUrl, credentialsConfBean.getMqttServerURL());
//     }

//     @Test
//     public void testServerID() {
//         assertEquals("testServer", credentialsConfBean.getServerID());
//     }

//     @Test
//     public void testUsername() {
//         assertEquals("testUser", credentialsConfBean.getUsername());
//     }

//     @Test
//     public void testPassword() {
//         assertEquals("testPass", credentialsConfBean.getPassword());
//     }

//     // Test cases for MqttBrokerCallBacksAutoBeans
//     @Test
//     public void testInitializeMqttClient() throws MqttException {
//         verify(mqttClient, times(1)).setCallback(mqttBrokerCallBacksAutoBeans);
//     }

//     @Test
//     public void testConnectionLost() throws MqttException, InterruptedException {
//         doNothing().when(mqttClient).reconnect();
//         when(mqttClient.isConnected()).thenReturn(false, true);

//         mqttBrokerCallBacksAutoBeans.connectionLost(new Throwable("Test connection lost"));

//         verify(mqttClient, atLeastOnce()).reconnect();
//     }

//     @Test
//     public void testMessageArrived() throws Exception {
//         MqttMessage message = new MqttMessage("testMessage".getBytes());
//         mqttBrokerCallBacksAutoBeans.messageArrived("testTopic", message);

//         verify(mqttWebSocketHandler, times(1)).sendMessageToClients("testMessage", "testTopic");
//     }

//     @Test
//     public void testResubscribeToDataBaseTopics() throws MqttException {
//         TopicsModel topic1 = new TopicsModel();
//         TopicsModel topic2 = new TopicsModel();
//         List<TopicsModel> topics = Arrays.asList(topic1, topic2);

//         when(topicRepository.findAll()).thenReturn(topics);

//         mqttBrokerCallBacksAutoBeans.resubscribeToDataBaseTopics();

//         verify(mqttClient, times(1)).subscribe("group1_topic1");
//         verify(mqttClient, times(1)).subscribe("group2_topic2");
//     }

//     @TestConfiguration
//     static class TestConfig {
//         @Bean
//         public CredentialsConfBean credentialsConfBean() {
//             return new CredentialsConfBean();
//         }

//         @Bean
//         public IMqttClient mqttClient() throws MqttException {
//             return mock(IMqttClient.class);
//         }
//     }
// }
