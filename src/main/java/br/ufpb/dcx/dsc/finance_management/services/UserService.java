package br.ufpb.dcx.dsc.finance_management.services;

import br.ufpb.dcx.dsc.finance_management.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufpb.dcx.dsc.finance_management.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> listUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ItemNotFoundException("User " + userId + " not found!"));
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ItemNotFoundException("User " + username + " not found!"));
    }

    public User createUser(User user){
        String encondedPw = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encondedPw);
        return userRepository.save(user);
    }

    public User updateUserByUserId(Long userId, User user){
        User toUpdate = userRepository
                .findById(userId)
                .orElseThrow(() -> new ItemNotFoundException("User " + userId + " not found!"));
        toUpdate.setName(user.getName());
        return userRepository.save(toUpdate);
    }

    public User updateUserByUsername(String username, User user){
        User toUpdate = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ItemNotFoundException("User " + username + " not found!"));
        toUpdate.setName(user.getName());
        return userRepository.save(toUpdate);
    }

    public void deleteUserById(Long userId){
        User toDelete = userRepository
                .findById(userId)
                .orElseThrow(() -> new ItemNotFoundException("User " + userId + " not found!"));
        return userRepository.delete(toDelete);
    }

    public void deleteUserByUsername(String username){
        User toDelete = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ItemNotFoundException("User " + username + " not found!"));
        return userRepository.delete(toDelete);
    }

}
