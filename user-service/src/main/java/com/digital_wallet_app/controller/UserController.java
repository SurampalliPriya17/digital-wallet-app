package com.digital_wallet_app.controller;

import com.digital_wallet_app.dto.UserDto;
import com.digital_wallet_app.dto.UserProfileDto;
import com.digital_wallet_app.exception.UserNotFoundException;
import com.digital_wallet_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user-service")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public Long createUser(@RequestBody @Valid UserDto userDto) throws ExecutionException, InterruptedException {
        return userService.createUser(userDto);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable Long id)throws UserNotFoundException{
        UserProfileDto userProfileDto = userService.getUserProfile(id);
        return ResponseEntity.ok(userProfileDto);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UserProfileDto> updateProfile(@PathVariable Long id, @RequestBody @Valid UserDto userDto) throws UserNotFoundException{
        UserProfileDto userProfileDto = userService.updateUser(id,userDto);
        return ResponseEntity.ok(userProfileDto);
    }


    @DeleteMapping("deleteUser/{id}")
    public String deleteProfile(@PathVariable Long id)throws UserNotFoundException{
        return userService.deleteUser(id);
    }
}


