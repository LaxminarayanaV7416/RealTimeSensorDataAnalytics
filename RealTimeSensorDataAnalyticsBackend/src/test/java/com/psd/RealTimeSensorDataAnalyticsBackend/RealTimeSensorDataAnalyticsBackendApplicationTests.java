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
