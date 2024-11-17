package com.jobportal.users.service;

import com.jobportal.users.dto.UserDto;
import com.jobportal.users.entity.User;
import com.jobportal.users.exception.ResourceNotFoundException;
import com.jobportal.users.exception.UserAlreadyExistsException;
import com.jobportal.users.mapper.UserMapper;
import com.jobportal.users.repository.UserRepository;
import lombok.AllArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
//    private BCryptPasswordEncoder bCryptPasswordEncoder;


    /**
     * @param userDto
     */
    @Override
    public void createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto, new User());
        Optional <User> optionalUser = userRepository.findByEmail(userDto.getEmail());
//        user.setPasswordHash(bCryptPasswordEncoder.encode(userDto.getPasswordHash()));
        if(optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with given email"+userDto.getEmail());
        }
        userRepository.save(user);

    }

    /**
     * @param email
     * @return
     */
    @Override
    public UserDto getUserDetails(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "Email", email)
        );
        UserDto userDto = UserMapper.mapToUserDto(user, new UserDto());
        return userDto;
    }

    /**
     * @param userDto
     * @return
     */
    @Override
    public boolean updateUser(UserDto userDto) {
        boolean isUpdated = false;

        User existingUser = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", userDto.getEmail()));

        User updatedUser = UserMapper.mapToUser(userDto, existingUser);

        userRepository.save(updatedUser);
        isUpdated = true;

        return isUpdated;
    }

    /**
     * @param email
     * @return
     */
    @Override
    public boolean deleteUser(String email) {
        boolean isDeleted = false;

        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Email", email));

        userRepository.deleteByEmail(email);
        isDeleted = true;

        return true;
    }
}
