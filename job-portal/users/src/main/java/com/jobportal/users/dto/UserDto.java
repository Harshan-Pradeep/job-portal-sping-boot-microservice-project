package com.jobportal.users.dto;

import com.jobportal.users.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Users",
        description = "Schema to hold User Account information"
)
public class UserDto {

    @NotEmpty(message = "user name can not be a null or empty")
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    @Schema(
            description = "UserName of user account", example = "userzi"
    )
    private String username;

    @NotEmpty(message = "Email address can not be a null or empty")
    @Email(message = "Email address should be a valid value")
    @Schema(
            description = "Email of user account", example = "user@gmail.com"
    )
    private String email;

    @NotEmpty(message = "password can not be a null or empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    @Schema(
            description = "Password of user account", example = "User12@"
    )
    private String passwordHash;

    @Pattern(regexp = "^[A-Za-z]+$", message = "First name can only contain letters")
    @Schema(
            description = "First Name of user account", example = "John"
    )
    private String firstName;

    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name can only contain letters")
    @Schema(
            description = "Last Name of user account", example = "Doe"
    )
    private String lastName;

    @Schema(
            description = "Role of user account", example = "JOB_SEEKER"
    )
    private UserRole userRole;
}
