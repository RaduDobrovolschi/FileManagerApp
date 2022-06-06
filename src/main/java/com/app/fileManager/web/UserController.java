package com.app.fileManager.web;

import com.app.fileManager.FileManagerApplication;
import com.app.fileManager.domain.User;
import com.app.fileManager.repository.RoleRepository;
import com.app.fileManager.repository.UserRepository;
import com.app.fileManager.service.UserService;
import com.app.fileManager.service.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private static final Logger logger = LogManager.getLogger(FileManagerApplication.class);
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public UserController(UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @PostMapping("/users/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto dto){
        if (dto.getLogin() == null
            || (dto.getEmail() == null)
            || (dto.getPassword() == null)
            || (userRepository.existsByEmail(dto.getEmail())
            || (userRepository.existsByLogin(dto.getLogin())))){
            logger.error("Invalid input data {}" + dto.toString());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.register(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody UserDto dto, @PathVariable Long id){
        if (!userRepository.existsById(id)
            || (dto.getRole() != null && !roleRepository.existsByName(dto.getRole()))){
            logger.error("not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.updateUserInfo(dto, id).get(), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<List<User>>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        logger.debug("getting user no " + id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            logger.debug("returning user " + user.get().toString());
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            logger.debug("user " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> putUser(@RequestBody UserDto dto, @PathVariable Long id){
        if (!dto.noEmptyFields()){
            logger.error("dto shouldnt have empty fields");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!userRepository.existsById(id)){
            logger.error("not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.putUser(dto, id).get(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        if (!userRepository.existsById(id)){
            logger.error("user " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}