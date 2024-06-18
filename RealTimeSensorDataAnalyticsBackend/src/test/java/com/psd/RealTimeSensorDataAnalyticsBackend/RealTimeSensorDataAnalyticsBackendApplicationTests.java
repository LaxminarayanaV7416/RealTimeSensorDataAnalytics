// package com.psd.RealTimeSensorDataAnalyticsBackend;

// import com.psd.RealTimeSensorDataAnalyticsBackend.controllers.UserLoginManagementController;
// import com.psd.RealTimeSensorDataAnalyticsBackend.models.UsersModel;
// import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UserRepository;
// import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import java.util.HashMap;
// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.Mockito.*;

// class RealTimeSensorDataAnalyticsBackendApplicationTests {

//     @Mock
//     private JwtTokenUtil jwtTokenUtil;

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private BCryptPasswordEncoder bCryptPasswordEncoder;

//     @InjectMocks
//     private UserLoginManagementController controller;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void testRegisterUser_Success() {
//         UsersModel user = new UsersModel();
//         user.setUsername("testuser");
//         user.setPassword("password");

//         when(userRepository.findByUsername(anyString())).thenReturn(null);
//         when(userRepository.save(any(UsersModel.class))).thenReturn(user);

//         ResponseEntity<Object> responseEntity = controller.registerUser(user);

//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//         Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
//         assertEquals("User Registered Succesfully", responseBody.get("message"));
//     }

//     @Test
//     void testRegisterUser_UserAlreadyExists() {
//         UsersModel user = new UsersModel();
//         user.setUsername("existinguser");
//         user.setPassword("password");

//         when(userRepository.findByUsername(anyString())).thenReturn(user);

//         ResponseEntity<Object> responseEntity = controller.registerUser(user);

//         assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
//         Map<String, String> responseBody = (Map<String, String>) responseEntity.getBody();
//         assertEquals("User Not Saved, User already exists", responseBody.get("message"));
//     }

//     @Test
//     public void testGenerateTokenUtil() {
//         String token = jwtTokenUtil.generateToken("testuser");
//         assertNotNull(token);
//     }

//     @Test
//     public void testGetUsernameFromTokenUtil() {
//         String token = jwtTokenUtil.generateToken("testuser");
//         String username = jwtTokenUtil.getUsernameFromToken(token);
//         assertEquals("testuser", username);
//     }

//     @Test
//     public void testValidateTokenUtil() {
//         String token = jwtTokenUtil.generateToken("testuser");
//         boolean result = jwtTokenUtil.validateToken(token);
//         assertEquals(true, result);
//     }

// }

package com.psd.RealTimeSensorDataAnalyticsBackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.CredentialsConfBean;
import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.MqttBrokerCallBacksAutoBeans;
import com.psd.RealTimeSensorDataAnalyticsBackend.configurations.WebSocketBeans;
import com.psd.RealTimeSensorDataAnalyticsBackend.controllers.OnBoardingSensorController;
import com.psd.RealTimeSensorDataAnalyticsBackend.controllers.MqttController;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.MqttPublishModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.UsersModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.UsersMachineModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.TopicRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UserRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UsersMachineRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "spring.mqtt.host=localhost",
        "spring.mqtt.port=1883",
        "spring.mqtt.serveridentity=testServer",
        "spring.mqtt.protocol=tcp",
        "spring.mqtt.username=testUser",
        "spring.mqtt.password=testPass"
})
public class RealTimeSensorDataAnalyticsBackendApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private CredentialsConfBean credentialsConfBean;

    @Mock
    private WebSocketBeans mqttWebSocketHandler;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsersMachineRepository usersMachineRepository;

    @Mock
    private IMqttClient mqttClient;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private MqttBrokerCallBacksAutoBeans mqttBrokerCallBacksAutoBeans;

    @InjectMocks
    private MqttController mqttController;

    @InjectMocks
    private OnBoardingSensorController onBoardingSensorController;

    @BeforeEach
    public void setUp() throws org.eclipse.paho.client.mqttv3.MqttException {
        MockitoAnnotations.openMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(mqttController, onBoardingSensorController).build();

        when(credentialsConfBean.getMqttServerURL()).thenReturn("tcp://localhost:1883");
        when(credentialsConfBean.getServerID()).thenReturn("testServer");
        when(credentialsConfBean.getUsername()).thenReturn("testUser");
        when(credentialsConfBean.getPassword()).thenReturn("testPass");

        when(MqttBrokerCallBacksAutoBeans.getInstance(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(mqttClient);

        mqttBrokerCallBacksAutoBeans.initializeMqttClient();

        // Mocking IMqttClient methods that will be called in initializeMqttClient
        when(mqttClient.isConnected()).thenReturn(false);
        doNothing().when(mqttClient).connect(any(MqttConnectOptions.class));
        doNothing().when(mqttClient).setCallback(any());
    }

    // Test cases for CredentialsConfBean
    @Test
    public void testMqttServerURL() {
        String expectedUrl = "tcp://localhost:1883";
        assertEquals(expectedUrl, credentialsConfBean.getMqttServerURL());
    }

    @Test
    public void testServerID() {
        assertEquals("testServer", credentialsConfBean.getServerID());
    }

    @Test
    public void testUsername() {
        assertEquals("testUser", credentialsConfBean.getUsername());
    }

    @Test
    public void testPassword() {
        assertEquals("testPass", credentialsConfBean.getPassword());
    }

    // Test cases for MqttBrokerCallBacksAutoBeans
    @Test
    public void testInitializeMqttClient() throws org.eclipse.paho.client.mqttv3.MqttException {
        verify(mqttClient, times(1)).setCallback(mqttBrokerCallBacksAutoBeans);
    }

    @Test
    public void testConnectionLost() throws org.eclipse.paho.client.mqttv3.MqttException, InterruptedException {
        doNothing().when(mqttClient).reconnect();
        when(mqttClient.isConnected()).thenReturn(false, true);

        mqttBrokerCallBacksAutoBeans.connectionLost(new Throwable("Test connection lost"));

        verify(mqttClient, atLeastOnce()).reconnect();
    }

    @Test
    public void testMessageArrived() throws Exception {
        MqttMessage message = new MqttMessage("testMessage".getBytes());
        mqttBrokerCallBacksAutoBeans.messageArrived("testTopic", message);

        verify(mqttWebSocketHandler, times(1)).sendMessageToClients("testMessage", "testTopic");
    }

    @Test
    public void testResubscribeToDataBaseTopics() throws org.eclipse.paho.client.mqttv3.MqttException {
        TopicsModel topic1 = new TopicsModel();
        TopicsModel topic2 = new TopicsModel();
        List<TopicsModel> topics = Arrays.asList(topic1, topic2);

        when(topicRepository.findAll()).thenReturn(topics);

        mqttBrokerCallBacksAutoBeans.resubscribeToDataBaseTopics();

        verify(mqttClient, times(1)).subscribe(topic1.getGroupName() + "_" + topic1.getTopicName());
        verify(mqttClient, times(1)).subscribe(topic2.getGroupName() + "_" + topic2.getTopicName());
    }

    // Test cases for MqttController
    @Test
    public void testPublishMessage_Success() throws Exception {
        MqttPublishModel publishModel = new MqttPublishModel();
        publishModel.setTopic("testTopic");
        publishModel.setMessage("testMessage");
        publishModel.setQos(1);
        publishModel.setRetained(false);

        MqttMessage mqttMessage = new MqttMessage(publishModel.getMessage().getBytes());
        mqttMessage.setQos(publishModel.getQos());
        mqttMessage.setRetained(publishModel.getRetained());

        doNothing().when(mqttClient).publish(publishModel.getTopic(), mqttMessage);

        mockMvc.perform(post("/api/mqtt/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"topic\": \"testTopic\", \"message\": \"testMessage\", \"qos\": 1, \"retained\": false}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPublishMessage_InvalidParameters() throws Exception {
        mockMvc.perform(post("/api/mqtt/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"topic\": \"\", \"message\": \"\", \"qos\": 1, \"retained\": false}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSubscribeChannel_Success() throws Exception {
        MqttMessage mqttMessage = new MqttMessage("testMessage".getBytes());
        mqttMessage.setQos(1);
        mqttMessage.setId(123);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(invocation -> {
            ((org.eclipse.paho.client.mqttv3.IMqttMessageListener) invocation.getArgument(1))
                    .messageArrived("testTopic", mqttMessage);
            countDownLatch.countDown();
            return null;
        }).when(mqttClient).subscribeWithResponse(eq("testTopic"), any());

        mockMvc.perform(get("/api/mqtt/subscribe")
                .param("topic", "testTopic")
                .param("wait_millis", "1000"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSubscribeChannel_Timeout() throws Exception {
        mockMvc.perform(get("/api/mqtt/subscribe")
                .param("topic", "testTopic")
                .param("wait_millis", "10"))
                .andExpect(status().isOk());
    }

    // Test cases for OnBoardingSensorController
    @Test
    public void testAddMachine_Success() throws Exception {
        UsersModel user = new UsersModel();
        user.setId(1L);
        user.setUsername("testUser");

        Optional<UsersModel> optionalUser = Optional.of(user);

        when(userRepository.findByUsername("testUser")).thenAnswer(invocation -> optionalUser);

        UsersMachineModel machine = new UsersMachineModel();
        machine.setId(1L);
        machine.setMachineName("testMachine");
        machine.setUsername("testUser");

        when(usersMachineRepository.save(any(UsersMachineModel.class))).thenReturn(machine);

        mockMvc.perform(post("/api/onboarding/add-machine")
                .header("Authorization", "Bearer jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"machineName\": \"testMachine\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddMachine_UserNotFound() throws Exception {

        Optional<UsersModel> optionalUser = Optional.empty();
        when(userRepository.findByUsername("nonExistingUser")).thenAnswer(invocation -> optionalUser);

        mockMvc.perform(post("/api/onboarding/add-machine")
                .header("Authorization", "Bearer jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"machineName\": \"testMachine\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMachine_Success() throws Exception {
        UsersModel user = new UsersModel();
        user.setId(1L);
        user.setUsername("testUser");

        UsersMachineModel machine = new UsersMachineModel();
        machine.setId(1L);
        machine.setMachineName("testMachine");
        machine.setUsername("testUser");
        Optional<UsersModel> optionalUser = Optional.of(user);

        when(userRepository.findByUsername("testUser")).thenAnswer(invocation -> optionalUser);
        when(usersMachineRepository.findByMachineName("testMachine")).thenReturn(Arrays.asList(machine));
        doNothing().when(usersMachineRepository).deleteByMachineName("testMachine");

        mockMvc.perform(delete("/api/onboarding/delete-machine")
                .header("Authorization", "Bearer jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"machineName\": \"testMachine\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMachine_UserNotFound() throws Exception {
        Optional<UsersModel> optionalUser = Optional.empty();
        when(userRepository.findByUsername("nonExistingUser")).thenAnswer(invocation -> optionalUser);

        mockMvc.perform(delete("/api/onboarding/delete-machine")
                .header("Authorization", "Bearer jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"machineName\": \"testMachine\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteMachine_MachineNotFound() throws Exception {
        UsersModel user = new UsersModel();
        user.setId(1L);
        user.setUsername("testUser");
        Optional<UsersModel> optionalUser = Optional.of(user);

        when(userRepository.findByUsername("testUser")).thenAnswer(invocation -> optionalUser);
        when(usersMachineRepository.findByMachineName("nonExistingMachine")).thenReturn(Arrays.asList());

        mockMvc.perform(delete("/api/onboarding/delete-machine")
                .header("Authorization", "Bearer jwtToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"machineName\": \"nonExistingMachine\"}"))
                .andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CredentialsConfBean credentialsConfBean() {
            return new CredentialsConfBean();
        }

        @Bean
        public IMqttClient mqttClient() throws org.eclipse.paho.client.mqttv3.MqttException {
            return mock(IMqttClient.class);
        }

        @Bean
        public JwtTokenUtil jwtTokenUtil() {
            return mock(JwtTokenUtil.class);
        }
    }
}