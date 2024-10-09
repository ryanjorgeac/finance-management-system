package br.ufpb.dcx.dsc.finance_management.controllers;

import br.ufpb.dcx.dsc.finance_management.DTOs.UserDTO;
import br.ufpb.dcx.dsc.finance_management.DTOs.UserDTOResponse;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Validated
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public  UserController(UserService userService, ModelMapper modelMapper){
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(path = "/users")
    List<UserDTOResponse> listUsers(){
        return userService.listUsers().stream()
                .map(this::convertToDTO)
                .toList();
    }

    private UserDTOResponse convertToDTO(User u) {
        return modelMapper.map(u, UserDTOResponse.class);
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
