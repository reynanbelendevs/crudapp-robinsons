package com.nan_spring_boot.spring_boot_crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nan_spring_boot.spring_boot_crud.model.UserProfile;
import com.nan_spring_boot.spring_boot_crud.service.UserProfileService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("api/v1/user")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/create")
    public ResponseEntity<String> postMethodName(@RequestBody @Valid UserProfile userEntity) {
        try {

            String result =  userProfileService.createUserProfileService(userEntity);
            return ResponseEntity.ok(result);
        } catch (InterruptedException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getuserProfile(@PathVariable int userId) {
        UserProfile userProfile = userProfileService.getUserProfileService(userId);
        if (userProfile == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody UserProfile userEntity) {
        String result = userProfileService.updateUserProfileService(userId, userEntity);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId) {
        String result = userProfileService.deleteUserProfileService(userId);
        return ResponseEntity.ok(result);
    }    

    @GetMapping("Hello")
    public ResponseEntity<String> Hello() {
        return ResponseEntity.ok("Hello");
    }    
    
}
