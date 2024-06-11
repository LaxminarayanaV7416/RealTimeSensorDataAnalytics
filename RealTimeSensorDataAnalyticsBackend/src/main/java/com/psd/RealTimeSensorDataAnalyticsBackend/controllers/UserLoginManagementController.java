package com.psd.RealTimeSensorDataAnalyticsBackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.psd.RealTimeSensorDataAnalyticsBackend.models.UsersModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.TokenModel;
import com.psd.RealTimeSensorDataAnalyticsBackend.models.Users;
import com.psd.RealTimeSensorDataAnalyticsBackend.repository.UserRepository;
import com.psd.RealTimeSensorDataAnalyticsBackend.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserLoginManagementController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UsersModel user){
        UsersModel searchedUser = userRepository.findByUsername(user.getUsername());
        Map<String, String> resultResponse = new HashMap<>();
        if (searchedUser == null){
            String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setUserType(UserEnum.IS_USER.toString());
            if (userRepository.save(user).getId()>0){
                resultResponse.put("message", "User Registered Succesfully");
                return ResponseEntity.ok(resultResponse);
            }
            resultResponse.put("message", "User Not Saved, Internal Server Error. Please Try Again");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultResponse);
        }
        resultResponse.put("message", "User Not Saved, User already exsists");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(resultResponse);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<Object> registerAdminUser(@RequestBody UsersModel user){
        UsersModel searchedUser = userRepository.findByUsername(user.getUsername());
        Map<String, String> resultResponse = new HashMap<>();
        if (searchedUser == null){
            String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);
            user.setUserType(UserEnum.IS_ADMIN.toString());
            if (userRepository.save(user).getId()>0){
                resultResponse.put("message", "User Registered Succesfully");
                return ResponseEntity.ok(resultResponse);
            }
            resultResponse.put("message", "User Not Saved, Internal Server Error. Please Try Again");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultResponse);
        }
        resultResponse.put("message", "User Not Saved, User already exsists");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(resultResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> generateToken(@RequestBody TokenModel tokenReqRes){
        UsersModel databaseUser = userRepository.findByUsername(tokenReqRes.getUsername());
        Map<String, String> resultResponse = new HashMap<>();
        if (databaseUser == null){
            resultResponse.put("message", "Sorry, User Does Not Exist");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultResponse);
        }
        if (new BCryptPasswordEncoder().matches(tokenReqRes.getPassword(), databaseUser.getPassword())){
            String tokenString = tokenReqRes.getUsername() + " <> " + databaseUser.getId() + " <> " + databaseUser.getUserType();
            String token = jwtTokenUtil.generateToken(tokenString);
            tokenReqRes.setToken(token);
            tokenReqRes.setExpirationTime("60 Min");
            resultResponse.put("username", tokenReqRes.getUsername());
            resultResponse.put("expiryTime", tokenReqRes.getExpirationTime());
            resultResponse.put("email", databaseUser.getEmail());
            resultResponse.put("token", tokenReqRes.getToken());
            resultResponse.put("userType", databaseUser.getUserType());
            return ResponseEntity.ok(resultResponse);
        }else {
            resultResponse.put("message", "Password Doesn't Match. Verify");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resultResponse);
        }
    }

    // ADMIN ROUTE ONLY
    @GetMapping("/get-all-users")
    public ResponseEntity<Object> getAllUsers(@RequestHeader(value = "Authorization", required = false) String token){
        Map<String, Object> result = new HashMap<>();
        if(Objects.nonNull(token)){
            boolean authorizationValidation = jwtTokenUtil.validateToken(token.substring(7));
            if(authorizationValidation){
                String userName = jwtTokenUtil.getUsernameFromToken(token.substring(7));
                UsersModel user = userRepository.findByUsername(userName);
                if(user.getUserType().equals(UserEnum.IS_ADMIN.toString())){
                    List<UsersModel> allUsers = userRepository.findAll();
                    List<Map<String, String>> filteredUsers = new ArrayList<>();
                    for(UsersModel eachUser : allUsers){
                        Map<String, String> tempData = new HashMap<>();
                        tempData.put("username", eachUser.getUsername());
                        tempData.put("email", eachUser.getEmail());
                        tempData.put("name", eachUser.getName());
                        filteredUsers.add(tempData);
                    }
                    result.put("message", "success");
                    result.put("results", filteredUsers);
                    return ResponseEntity.status(HttpStatus.OK).body(result);
                } else {
                    result.put("message", "User is not Admin to get all users");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
                }
            } else {
                result.put("message", "authorizaiton token is invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }
        } else {
            result.put("message", "Provide the Authorization token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }
}
