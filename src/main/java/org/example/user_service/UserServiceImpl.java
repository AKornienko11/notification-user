package org.example.user_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final MessageProducer messageProducer;

    @Autowired
    public UserServiceImpl(UserRepository repo, MessageProducer messageProducer) {
        this.repository = repo;
        this.messageProducer = messageProducer;

    }

    @Override
    public List<UserDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        return repository.findById(id).map(this::convertToDTO);
    }

    @Override
    public void createUser(UserDTO userDTO) {
        User user = convertFromDTO(userDTO);
        User savedUser = repository.save(user);
        // Отправляем уведомление через Kafka
        messageProducer.sendMessage("user-topic", "Created: " + savedUser.getEmail());
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        if (!repository.existsById(userDTO.getId())) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        User userUpdate = convertFromDTO(userDTO);
        repository.save(userUpdate);

    }

    @Override
    public void deleteUser(Long id) {
        User deletedUser = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        // Уведомляем через Kafka
        messageProducer.sendMessage("user-topic", "Deleted: " + id);
        repository.deleteById(id);
    }

    public UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;

    }

    public User convertFromDTO(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;

    }
}
