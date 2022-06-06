package com.app.fileManager.service;

import com.app.fileManager.FileManagerApplication;
import com.app.fileManager.domain.Document;
import com.app.fileManager.domain.User;
import com.app.fileManager.repository.DocumentRepository;
import com.app.fileManager.repository.RoleRepository;
import com.app.fileManager.repository.UserRepository;
import com.app.fileManager.service.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LogManager.getLogger(FileManagerApplication.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final BucketService bucketService;
    private final DocumentRepository documentRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BucketService bucketService, DocumentRepository documentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bucketService = bucketService;
        this.documentRepository = documentRepository;
    }

    public User register(UserDto dto){
        logger.debug("Creating new user: " + dto.toString());
        return userRepository.save(new User(
                dto.getLogin(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                roleRepository.getByName("user").get()
        ));
    }

    public Optional<User> updateUserInfo(UserDto dto, Long id){
        return Optional
                .of(userRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    if (dto.getPassword() != null) {
                        user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    }
                    if (dto.getLogin() != null) user.setLogin(dto.getLogin());
                    if (dto.getEmail() != null) user.setEmail(dto.getEmail());
                    if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
                    if (dto.getLastName() != null) user.setLastName(dto.getLastName());
                    if (dto.getRole() != null) {
                        user.addRole(roleRepository.findById(dto.getRole()).get());
                    }
                    return user;
                });
    }

    public Optional<User> putUser(UserDto dto, Long id){
        return Optional
                .of(userRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    user.addRole(roleRepository.getByName(dto.getRole()).get());
                    user.setLogin(dto.getLogin());
                    user.setLastName(dto.getLastName());
                    user.setFirstName(dto.getFirstName());
                    user.setEmail(dto.getEmail());
                    user.setPassword(passwordEncoder.encode(dto.getPassword()));
                    return user;
                });
    }

    public void delete(Long id){
        logger.debug("deleting user id: " + id);
        User user = userRepository.findById(id).get();
        for (Document document : user.getDocuments()){
            documentRepository.delete(document);
        }
        bucketService.deleteBucket(user.getLogin());
        userRepository.deleteById(id);
    }
}
