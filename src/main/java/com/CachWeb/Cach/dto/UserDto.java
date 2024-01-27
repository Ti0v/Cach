package com.CachWeb.Cach.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
{

    private Long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @NotEmpty(message = "Phone number should not be empty")
    private String phoneNumber;

    private String roleName;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    @NotEmpty(message = "Password confirmation should not be empty")
    private String passwordConfirmation;

    private String fullName;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
