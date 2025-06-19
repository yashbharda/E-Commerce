package com.e_commerce.Service;

import com.e_commerce.DTO.UserDTO;
import com.e_commerce.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public User saveUser(User user);

    public List<UserDTO> getAllUser();

    public Boolean updateUserById(Long id, User user);

    public Boolean deleteById(Long id);

    public Optional<User> getUserById(Long id);

    public Optional<User> login(String email, String password);

}
