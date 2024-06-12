package com.psd.RealTimeSensorDataAnalyticsBackend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.psd.RealTimeSensorDataAnalyticsBackend.controllers.UserLoginManagementController;
import com.psd.RealTimeSensorDataAnalyticsBackend.controllers.OnBoardingSensorController;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.UsersModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.TopicsModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.TopicRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UserRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
public class CombinedControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TopicRepository topicRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // User Controller Tests

    @Test
    public void givenNonAdminUser_whenGetAllUsers_thenUnauthorized() throws Exception {
        String token = "Bearer valid_token";

        // Mock token validation and user retrieval
        when(jwtTokenUtil.validateToken("valid_token")).thenReturn(true);
        when(jwtTokenUtil.getUsernameFromToken("valid_token")).thenReturn("user");
        UsersModel user = new UsersModel();
        user.setUserType("USER");
        when(userRepository.findByUsername("user")).thenReturn(user);

        mockMvc.perform(get("/get-all-users")
                .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    // On-Boarding Sensor Controller Tests

    @Test
    public void givenNoToken_whenOnBoardNewSensor_thenUnauthorized() throws Exception {
        TopicsModel topicsModel = new TopicsModel();
        topicsModel.setGroupName("group");
        topicsModel.setTopicName("topic");

        mockMvc.perform(post("/onboard-new-sensor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(topicsModel)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNonAdminUser_whenOnBoardNewSensor_thenUnauthorized() throws Exception {
        String token = "Bearer valid_token";
        TopicsModel topicsModel = new TopicsModel();
        topicsModel.setGroupName("group");
        topicsModel.setTopicName("topic");

        when(jwtTokenUtil.validateToken("valid_token")).thenReturn(true);
        when(jwtTokenUtil.getUsernameFromToken("valid_token")).thenReturn("user");
        UsersModel user = new UsersModel();
        user.setUserType("USER");
        when(userRepository.findByUsername("user")).thenReturn(user);

        mockMvc.perform(post("/onboard-new-sensor")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(topicsModel)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenAdminUser_whenOnBoardNewSensor_thenCreated() throws Exception {
        String token = "Bearer valid_token";

        TopicsModel topicsModel = new TopicsModel();
        topicsModel.setGroupName("group");
        topicsModel.setTopicName("topic");

        TopicsModel savedTopicsModel = new TopicsModel();
        savedTopicsModel.setGroupName("group");
        savedTopicsModel.setTopicName("topic");
        savedTopicsModel.setId(1L); 

        when(jwtTokenUtil.validateToken("valid_token")).thenReturn(true);
        when(jwtTokenUtil.getUsernameFromToken("valid_token")).thenReturn("admin");

        UsersModel user = new UsersModel();
        user.setUserType("IS_ADMIN");
        when(userRepository.findByUsername("admin")).thenReturn(user);

        when(topicRepository.save(any(TopicsModel.class))).thenReturn(savedTopicsModel);

        mockMvc.perform(post("/onboard-new-sensor")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(topicsModel)))
                .andExpect(status().isCreated());

        verify(topicRepository, times(1)).save(any(TopicsModel.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
