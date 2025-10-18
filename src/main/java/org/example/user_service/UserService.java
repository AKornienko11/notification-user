package org.example.user_service;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDTO> findAll();
    Optional<UserDTO> findById(Long id);
    void createUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);
    void deleteUser(Long id);
}
