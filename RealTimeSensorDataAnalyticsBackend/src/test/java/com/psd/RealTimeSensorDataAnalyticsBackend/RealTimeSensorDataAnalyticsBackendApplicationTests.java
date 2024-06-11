// package com.psd.RealTimeSensorDataAnalyticsBackend;

// import com.psd.RealTimeSensorDataAnalyticsBackend.controllers.EmployeeController;
// import com.psd.RealTimeSensorDataAnalyticsBackend.controllers.UserLoginManagementController;
// import com.psd.RealTimeSensorDataAnalyticsBackend.models.Users;
// import com.psd.RealTimeSensorDataAnalyticsBackend.models.TokenModel;
// import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UserRepository;
// import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;
// import com.psd.RealTimeSensorDataAnalyticsBackend.constants.UserEnum;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import java.util.HashMap;
// import java.util.Map;

// import static org.junit.jupiter.api.Assertions.*;

// @WebMvcTest({EmployeeController.class, UserLoginManagementController.class})
// public class RealTimeSensorDataAnalyticsBackendApplicationTests {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private JwtTokenUtil jwtTokenUtil;

//     @MockBean
//     private UserRepository userRepository;

//     @MockBean
//     private BCryptPasswordEncoder bCryptPasswordEncoder;

//     private static final ObjectMapper objectMapper = new ObjectMapper();

//     @Test
//     public void testGetAllEmployee() throws Exception {
//         mockMvc.perform(MockMvcRequestBuilders.get("/api/all"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello, world!"));
//     }

//     @Test
//     public void testWelcomeMessage() throws Exception {
//         mockMvc.perform(MockMvcRequestBuilders.get("/api/welcome"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("Welcome to My API"));
//     }

//     @Test
//     public void testRegisterUser() throws Exception {
//         Users user = new Users();
//         user.setUsername("testuser");
//         user.setPassword("password");

//         Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(user);

//         Map<String, String> resultResponse = new HashMap<>();
//         resultResponse.put("message", "User Registered Succesfully");

//         mockMvc.perform(MockMvcRequestBuilders.post("/register")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User Registered Succesfully"));
//     }

//     @Test
//     public void testRegisterAdminUser() throws Exception {
//         Users user = new Users();
//         user.setUsername("adminuser");
//         user.setPassword("password");

//         Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(user);

//         Map<String, String> resultResponse = new HashMap<>();
//         resultResponse.put("message", "User Registered Succesfully");

//         mockMvc.perform(MockMvcRequestBuilders.post("/register-admin")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User Registered Succesfully"));
//     }

//     @Test
//     public void testGenerateToken() throws Exception {
//         Users user = new Users();
//         user.setUsername("testuser");
//         user.setPassword(new BCryptPasswordEncoder().encode("password"));

//         Mockito.when(userRepository.findByUsername("testuser")).thenReturn(user);
//         Mockito.when(jwtTokenUtil.generateToken(Mockito.anyString())).thenReturn("mock-token");

//         TokenModel tokenModel = new TokenModel();
//         tokenModel.setUsername("testuser");
//         tokenModel.setPassword("password");

//         mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(tokenModel)))
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("mock-token"));
//     }

//     @Test
//     public void testValidateToken() throws Exception {
//         TokenModel tokenModel = new TokenModel();
//         tokenModel.setToken("mock-token");

//         Mockito.when(jwtTokenUtil.validateToken("mock-token")).thenReturn("valid");

//         mockMvc.perform(MockMvcRequestBuilders.post("/validate-token")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(tokenModel)))
//                 .andExpect(MockMvcResultMatchers.status().isOk())
//                 .andExpect(MockMvcResultMatchers.content().string("valid"));
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
//         assertEquals("valid", result);
//     }

//     @Test
//     public void testValidateTokenExpiredUtil() throws InterruptedException {
//         String token = jwtTokenUtil.generateToken("testuser");
//         Thread.sleep(60 * 60000); // Wait for the token to expire (60 minutes)
//         boolean result = jwtTokenUtil.validateToken(token);
//         assertEquals("token expired, Please follow refresh mechanism to generate new token", result);
//     }
// }
