package com.jobportal.users.service;

import com.jobportal.users.dto.UserDto;

public interface IUserService {

    void createUser(UserDto userDto);

    UserDto getUserDetails(String email);

    boolean updateUser(UserDto userDto);

    boolean deleteUser(String email);
}
