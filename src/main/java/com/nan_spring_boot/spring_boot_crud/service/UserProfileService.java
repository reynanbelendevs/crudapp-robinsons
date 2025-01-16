package com.nan_spring_boot.spring_boot_crud.service;
import com.nan_spring_boot.spring_boot_crud.model.UserProfile;


public interface UserProfileService {

    public String createUserProfileService(UserProfile userProfile) throws InterruptedException;

    public UserProfile getUserProfileService(Integer param);

    public String updateUserProfileService(Integer param , UserProfile userProfile);

    public String deleteUserProfileService(Integer param);

}
