package com.jobportal.users.controller;


import com.jobportal.users.constants.UserConstants;
import com.jobportal.users.dto.ResponseDto;
import com.jobportal.users.dto.UserDto;
import com.jobportal.users.service.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class UserController {

    private final IUserService iUserService;

    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody UserDto userDto) {
        iUserService.createUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(UserConstants.STATUS_201,UserConstants.MESSAGE_201));
    }

    @GetMapping("/fetch")
    public ResponseEntity<UserDto> fetchUserDetails(@RequestParam
                                                        @NotEmpty(message = "Email cannot be empty")
                                                        @Email(message = "Invalid email format")
                                                        String email) {
        UserDto userDto = iUserService.getUserDetails(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateUser(@Valid @RequestBody UserDto userDto) {
        boolean isUpdated = iUserService.updateUser(userDto);

        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
        }
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(UserConstants.STATUS_417, UserConstants.MESSAGE_417_UPDATE));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam
                                                      @NotEmpty(message = "Email cannot be empty")
                                                      @Email(message = "Invalid email format")
                                                      String email) {
        boolean isDeleted = iUserService.deleteUser(email);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(UserConstants.STATUS_200, UserConstants.MESSAGE_200));
        }
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(UserConstants.STATUS_417, UserConstants.MESSAGE_417_DELETE));
    }
}
