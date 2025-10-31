package org.example.user_service.service;

import org.example.user_service.userdto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAll();
    Optional<UserDTO> findById(Long id);
    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(Long id);

}
