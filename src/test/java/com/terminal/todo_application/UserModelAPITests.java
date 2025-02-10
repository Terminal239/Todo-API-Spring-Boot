package com.terminal.todo_application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terminal.todo_application.model.Todo;
import com.terminal.todo_application.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
public class UserModelAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateUser() throws Exception {
        User user = createUser(new User("Linus Sebastian", "jane.smith@gmail.com"));

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldFindUserByUsername() throws Exception {
        User user = createUser(new User("Linus Sebastian", "jane.smith@gmail.com"));

        String searchQuery = "Linus";
        mockMvc.perform(get("/api/users?username=" + searchQuery))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldReturnNotWithGivenUsername() throws Exception {
        String searchQuery = "Wick";
        mockMvc.perform(get("/api/users?username=" + searchQuery))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindUserById() throws Exception {
        User user = createUser(new User("Linus Sebastian", "jane.smith@gmail.com"));

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void shouldReturnNotFoundGivenId() throws Exception {
        int id = 100;
        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateUserById() throws Exception {
        User user = createUser(new User("Linus Sebastian", "jane.smith@gmail.com"));

        String updatedUsername = "Linus Smith";
        user.setUsername(updatedUsername);
        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(updatedUsername));
    }

    @Test
    public void shouldReturnNotFoundForUpdateByGivenId() throws Exception {
        User user = new User("Linus Sebastian", "jane.smith@gmail.com");
        mockMvc.perform(put("/api/users/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteUserGivenId() throws Exception {
        String searchQuery = "John";
        String response = mockMvc.perform(get("/api/users?username=" + searchQuery))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User found = objectMapper.readValue(response, User.class);
        mockMvc.perform(delete("/api/users/" + found.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundForDeleteUserGivenId() throws Exception {
        mockMvc.perform(delete("/api/users/100"))
                .andExpect(status().isNotFound());
    }

    private User createUser(User user) throws Exception {
        String response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, User.class);
    }

    private void createTodo(Todo todo) throws Exception {
        String response = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo))).andReturn().getResponse().getContentAsString();

        objectMapper.readValue(response, Todo.class);
    }
}
