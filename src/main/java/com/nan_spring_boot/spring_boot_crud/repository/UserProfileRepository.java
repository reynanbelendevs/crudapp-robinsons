package com.nan_spring_boot.spring_boot_crud.repository;
import org.apache.el.parser.ParseException;
import com.nan_spring_boot.spring_boot_crud.model.UserProfile;

public interface UserProfileRepository {
 
    public Integer createUserProfile(UserProfile userProfile) throws ParseException, java.text.ParseException;

    public UserProfile getUserProfile(Integer param);

    public int updateUserProfile(Integer param, UserProfile userProfile);   

    public int deleteUserProfile(Integer param);


}
