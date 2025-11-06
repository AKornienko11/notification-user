package org.example.user_service.controller;

import org.example.user_service.service.UserService;
import org.example.user_service.userdto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserDTO userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDTO();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john@example.com");

    }


    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.findAll()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.ofNullable(userDto));

        mockMvc.perform(get("/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }


    @Test
    void testAddUser() throws Exception {
        UserDTO requestDto = new UserDTO();
        requestDto.setName("Dave");
        requestDto.setEmail("dave@example.com");


        UserDTO savedDto = new UserDTO();
        savedDto.setId(2L);
        savedDto.setName("Dave");
        savedDto.setEmail("dave@example.com");


        Mockito.when(userService.createUser(any(UserDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dave"))
                .andExpect(jsonPath("$.email").value("dave@example.com"));

        Mockito.verify(userService, Mockito.times(1))
                .createUser(argThat(dto ->
                        dto.getName().equals("Dave") && dto.getEmail().equals("dave@example.com")
                ));
    }


    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDTO update = new UserDTO();
        update.setName("Johnny");
        update.setEmail("johnny.mnemonic@mail.com");
        update.setId(1L);


        Mockito.when(userService.updateUser(any(UserDTO.class))).thenReturn(update);

        mockMvc.perform(put("/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Johnny"));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        mockMvc.perform(delete("/1"))
                .andExpect(status().isNoContent());

        Mockito.doNothing().when(userService).deleteUser(any());
    }

}