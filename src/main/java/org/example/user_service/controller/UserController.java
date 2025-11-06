package org.example.user_service.controller;

import org.example.user_service.service.UserService;
import org.example.user_service.userdto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }



    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.findById(id).orElseThrow());
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        service.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser( @RequestBody UserDTO userDTO ) {
        service.updateUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
