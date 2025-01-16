package com.nan_spring_boot.spring_boot_crud.service;

import org.springframework.stereotype.Service;
import com.nan_spring_boot.spring_boot_crud.model.UserProfile;
import com.nan_spring_boot.spring_boot_crud.repository.UserProfileRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class UserProfileServiceImpl implements UserProfileService {
    @Autowired
    UserProfileRepositoryImpl userProfileRepository;

    @Override
    public String createUserProfileService(UserProfile userProfile) throws InterruptedException {
        try {
            int id = userProfileRepository.createUserProfile(userProfile);
            return id  > 0 ? "User created successfully, ID: " + String.valueOf(id) : "User not created";
            
        } catch (Exception e) {
           return e.getMessage();
        }
    }
    @Override
    public UserProfile getUserProfileService(Integer param) {
        try {
            UserProfile userProfile = userProfileRepository.getUserProfile(param);
            return userProfile;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String updateUserProfileService(Integer param, UserProfile userProfile) {
        try{
            int rowsAffected = userProfileRepository.updateUserProfile(param, userProfile);
            if (rowsAffected > 0) {
                return "User Profile updated successfully";
            } else {
                return "User Profile not found";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String deleteUserProfileService(Integer param) {
        try {
            int rowsAffected = userProfileRepository.deleteUserProfile(param);
            if (rowsAffected > 0) {
                return "User deleted successfully";
            }else{
                return "User not found";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    
}
