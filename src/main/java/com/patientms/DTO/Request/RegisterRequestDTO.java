package com.patientms.DTO.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
    private String userId;
    private String firstName;
    private String lastName;

    @NotBlank(message = "Email Required")
    @Email(message = "Invalid Email")
    private String email;

    @NotBlank(message = "Password Required")
    @Size(min=2,message = "Invalid Password")
    private String password;
}
