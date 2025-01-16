package com.nan_spring_boot.spring_boot_crud.model;

import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserProfile {

        @NotBlank
        @Size(max = 50)
        @NotNull
        private String name;

        @NotBlank
        @NotNull
        @Email
        private String emailAddress;

        private Gender gender;

        @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2}", message = "Date must be in the format yyyy/mm/dd")
        private String birthDate;

        private String role;

        @Nullable 
        private int age;

        public UserProfile(@NotBlank @Size(max = 50) @NotNull String name,
                @NotBlank @NotNull @Email String emailAddress, @NotBlank @NotNull Gender gender,
                @NotNull @Past @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2}", message = "Date must be in the format yyyy/mm/dd") String birthDate,
                String role, int age) {
            this.name = name;
            this.emailAddress = emailAddress;
            this.gender = gender;
            this.birthDate = birthDate;
            this.role = role;
            this.age = age;
        }


        

    }

