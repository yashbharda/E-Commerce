package com.e_commerce.Controller;

import com.e_commerce.DTO.UserDTO;
import com.e_commerce.Entity.User;
import com.e_commerce.Service.UserService;
import com.e_commerce.Service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Tag(name = "User Apis", description = "Add, Read, Update, Delete User")
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private ModelMapper mapper;

    @PostMapping("/add")
    @Operation(summary = "Add a new User")
    public ResponseEntity<User> addUser(@RequestBody User user){
        System.out.println(user.getPassword());
        return new ResponseEntity<>(service.saveUser(user),HttpStatus.CREATED);
    }

    @GetMapping("/get")
    @Operation(summary = "Display All User")
    public ResponseEntity<List<UserDTO>> getAllUser(){
        return new ResponseEntity<>(service.getAllUser(),HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a User")
    public ResponseEntity<?> updateById(@PathVariable Long id, @RequestBody User user){
        if(service.updateUserById(id,user)){
            return new ResponseEntity<>("User Updated",HttpStatus.OK);
        }
        return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a User")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        if(service.deleteById(id)){
            return new ResponseEntity<>("User Deleted",HttpStatus.OK);
        }
        return new ResponseEntity<>("User Not Found",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get a User By Id")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> optionalUser=service.getUserById(id);
       if(optionalUser.isPresent()){
           return ResponseEntity.ok(optionalUser.get());
       }
       return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    @Operation(summary = "User SignUp")
    public ResponseEntity<?> login(@RequestBody Map<String, String> logindata, HttpSession session){
        String email=logindata.get("email");
        String password=logindata.get("password");

        Optional<User> userOptional=service.login(email,password);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setLoggedIn(true); // update status
            service.saveUser(user);  // save updated user

            session.setAttribute("loggedUser",userOptional.get());
            return ResponseEntity.ok("Login SuccessFul");
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 💥 This logs out the user
        return ResponseEntity.ok("Logout successful");
    }


}
