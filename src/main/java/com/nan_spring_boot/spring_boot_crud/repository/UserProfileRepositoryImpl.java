package com.nan_spring_boot.spring_boot_crud.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import com.nan_spring_boot.spring_boot_crud.model.Gender;
import com.nan_spring_boot.spring_boot_crud.model.UserProfile;
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Repository
public class UserProfileRepositoryImpl implements UserProfileRepository {

    private Date transformStringToDate(String string_date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate recordDate = LocalDate.parse(string_date, formatter);
        return Date.valueOf(recordDate);
    }

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
 
    @Override
    public Integer createUserProfile(UserProfile userProfile) {
        final String sql = """
            INSERT INTO users_profile (
            full_name, email_address, gender, birth_date, role_base, age
            ) VALUES (
            :name, :email_address, :gender, :birth_date, :role_base, :age
            )
            RETURNING id
        """;
        MapSqlParameterSource parameterSqlParameterSource = new MapSqlParameterSource()
            .addValue("name", userProfile.getName())
            .addValue("email_address", userProfile.getEmailAddress())
            .addValue("gender", userProfile.getGender().toString())
            .addValue("birth_date", transformStringToDate(userProfile.getBirthDate()))
            .addValue("role_base", userProfile.getRole())
            .addValue("age", userProfile.getAge());
        Integer returningId = jdbcTemplate.queryForObject(sql, parameterSqlParameterSource, Integer.class);
        return returningId;
    }

    @Override
    public UserProfile getUserProfile(Integer param) {
        final String sql = """
            SELECT * FROM users_profile 
             WHERE id = :user_id::integer 
        """;
        MapSqlParameterSource parameterSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", param);
        
        UserProfile userProfile = jdbcTemplate.queryForObject(
            sql, 
            parameterSqlParameterSource, 
            (rs, rowNum) -> {
            return new UserProfile(
                rs.getString("full_name"),
                rs.getString("email_address"),
                Gender.valueOf(rs.getString("gender").toUpperCase()),
                rs.getDate("birth_date").toString(),
                rs.getString("role_base"),
                rs.getInt("age")
            );
            }
        );
        return userProfile;
    }

    @Override
    public int updateUserProfile(Integer param, UserProfile userProfile) {
        final String sql = """
            UPDATE users_profile SET 
             full_name = :name, 
             email_address = :email_address, 
             gender = :gender, 
             birth_date = :birth_date, 
             role_base = :role, 
             age = :age 
             WHERE id = :user_id
            """;
        MapSqlParameterSource parameterSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", param)
        .addValue("name", userProfile.getName())
        .addValue("email_address", userProfile.getEmailAddress())
        .addValue("gender", userProfile.getGender().toString())
        .addValue("birth_date", transformStringToDate(userProfile.getBirthDate()))
        .addValue("role", userProfile.getRole())
        .addValue("age", userProfile.getAge());
        int rowsAffected = jdbcTemplate.update(sql, parameterSqlParameterSource);
        return rowsAffected;
    }
    @Override
    public int deleteUserProfile(Integer param) {
        final String sql = """
                DELETE FROM users_profile 
                 WHERE 
                 id = :user_id::integer
                RETURNING id
                """;
        MapSqlParameterSource parameterSqlParameterSource = new MapSqlParameterSource()
        .addValue("user_id", param);
        List<String> deletedUser = jdbcTemplate.query(sql, parameterSqlParameterSource, (rs, rowNum) -> rs.getString("id"));
        int rowsAffected = deletedUser.size();
        return rowsAffected;
    }
}
