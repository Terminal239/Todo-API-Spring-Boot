package com.terminal.todo_application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terminal.todo_application.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserModelAPITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldCreateUser() throws Exception {
        User user = new User("Linus Sebastian");
        String response = mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn().getResponse().getContentAsString();

        User created = objectMapper.readValue(response, User.class);
        mockMvc.perform(get("/api/user/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldFindUserByUsername() throws Exception {
        User user = new User("Linus Sebastian");
        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        String searchQuery = "Linus";
        mockMvc.perform(get("/api/user?username=" + searchQuery))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    public void shouldReturnNotWithGivenUsername() throws Exception {
        String searchQuery = "Wick";
        mockMvc.perform(get("/api/user?username=" + searchQuery))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindUserById() throws Exception {
        User user = new User("Linus Sebastian");
        String response = mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andReturn().getResponse().getContentAsString();

        User created = objectMapper.readValue(response, User.class);
        mockMvc.perform(get("/api/user/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(created.getUsername()));
    }

    @Test
    public void shouldReturnNotFoundGivenId() throws Exception {
        int id = 100;
        mockMvc.perform(get("/api/user/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdateUserById() throws Exception {
        User user = new User("Linus Sebastian");
        String response = mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                        .andReturn().getResponse().getContentAsString();

        String updatedUsername = "Linus Smith";
        User created = objectMapper.readValue(response, User.class);
        created.setUsername(updatedUsername);
        mockMvc.perform(put("/api/user/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(updatedUsername));
    }

    @Test
    public void shouldReturnNotFoundForUpdateByGivenId() throws Exception {
        User user = new User("Linus Sebastian");
        mockMvc.perform(put("/api/user/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteUserGivenId() throws Exception {
        String searchQuery = "John";
        String response = mockMvc.perform(get("/api/user?username=" + searchQuery))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User found = objectMapper.readValue(response, User.class);
        mockMvc.perform(delete("/api/user/" + found.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnNotFoundForDeleteUserGivenId() throws Exception {
        mockMvc.perform(delete("/api/user/100"))
                .andExpect(status().isNotFound());
    }
}
