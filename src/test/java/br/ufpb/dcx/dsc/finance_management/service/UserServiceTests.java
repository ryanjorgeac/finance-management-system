package br.ufpb.dcx.dsc.finance_management.service;

import br.ufpb.dcx.dsc.finance_management.exceptions.UserNotFoundException;
import br.ufpb.dcx.dsc.finance_management.models.User;
import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
import br.ufpb.dcx.dsc.finance_management.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setName("Casca de bala");
        user.setPassword("password");
    }

    @Test
    void testListUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.listUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("Casca de bala", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals("Casca de bala", foundUser.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.getUserById(1L));

        assertEquals("User with ID 1 not found!", exception.getMessage());
    }

    @Test
    void testGetUserByUsernameSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User foundUser = userService.getUserByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("Casca de bala", foundUser.getName());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("unknownuser")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.getUserByUsername("unknownuser"));

        assertEquals("User unknownuser not found!", exception.getMessage());
    }

    @Test
    void testCreateUserSuccess() {
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(1L);
            return userToSave;
        });
        User newUser = new User();
        newUser.setPassword("password");
        User createdUser = userService.createUser(newUser);
        assertNotNull(createdUser);
        assertEquals("encodedPassword", createdUser.getPassword());
        assertNotNull(createdUser.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void testUpdateUserByUserIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setName("Updated User");

        User result = userService.updateUserByUserId(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserByUserIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User updatedUser = new User();
        updatedUser.setName("Updated User");

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.updateUserByUserId(1L, updatedUser));

        assertEquals("User with ID 1 not found!", exception.getMessage());
    }

    @Test
    void testDeleteUserByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.deleteUserById(1L));

        assertEquals("User with ID 1 not found!", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void testDeleteUserByUsernameSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        doNothing().when(userRepository).delete(user);

        userService.deleteUserByUsername("testuser");

        verify(userRepository, times(1)).findByUsername("testuser");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUserByUsernameNotFound() {
        when(userRepository.findByUsername("unknownuser")).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                userService.deleteUserByUsername("unknownuser"));

        assertEquals("User unknownuser not found!", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }
}
