package com.e_commerce.Service;

import com.e_commerce.DTO.UserDTO;
import com.e_commerce.Entity.User;
import com.e_commerce.Repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper mapper;

    public UserDTO convertToDTO(User user){
        return mapper.map(user,UserDTO.class);
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public List<UserDTO> getAllUser() {
        List<User> users=userRepo.findAll();
        List<UserDTO> userDTOList=new ArrayList<>();

        for (User user:users){
            UserDTO dto=convertToDTO(user);
            userDTOList.add(dto);
        }
        return userDTOList;
    }

    @Override
    public Boolean updateUserById(Long id, User user) {
        if(userRepo.existsById(id)){
            user.setId(id);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteById(Long id) {
        if(userRepo.existsById(id)){
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<User> login(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password);
    }


}
