package br.ufpb.dcx.dsc.finance_management.controllers;

import br.ufpb.dcx.dsc.finance_management.DTOs.user.UserCreationDTO;
import br.ufpb.dcx.dsc.finance_management.DTOs.user.UserDTO;
import br.ufpb.dcx.dsc.finance_management.DTOs.user.UserDTOResponse;
import br.ufpb.dcx.dsc.finance_management.DTOs.user.UserDTOUpdate;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.services.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Validated
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private UserDTOResponse convertToDTO(User user){
        return modelMapper.map(user, UserDTOResponse.class);
    }

    private User convertToEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private User convertToEntityUpdate(UserDTOUpdate userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private User convertToEntityCreation(UserCreationDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    @GetMapping("/users")
    List<UserDTOResponse> listUsers(){
        return userService
                .listUsers()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/users/{userId}")
    UserDTOResponse getUserById(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        return convertToDTO(user);
    }

//    @GetMapping("/users/{username}")
//    UserDTOResponse getUserById(@PathVariable String username){
//        User user = userService.getUserByUsername(username);
//        return convertToDTO(user);
//    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTOResponse createUser(@Valid @RequestBody UserCreationDTO userDTO){
        User user = convertToEntityCreation(userDTO);
        System.out.println("saldo é " + (user.getBalance().toString()));
        User savedUser = userService.createUser(user);
        return convertToDTO(savedUser);
    }

    @PutMapping("/users/{userId}")
    public UserDTOResponse updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTOUpdate userDTO){
        User user = convertToEntityUpdate(userDTO);
        User updated = userService.updateUserByUserId(userId, user);
        return convertToDTO(updated);
    }

//    @PutMapping("/users/{username}")
//    public UserDTOResponse updateUser(@PathVariable String username, @RequestBody UserDTO userDTO){
//        User user = convertToEntity(userDTO);
//        User updated = userService.updateUserByUsername(username, user);
//        return convertToDTO(updated);
//    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUserById(userId);
    }

//    @DeleteMapping("/users/{username}")
//    public void deleteUser(@PathVariable String username){
//        userService.deleteUserByUsername(username);
//    }

}
