package com.terminal.todo_application;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTodoIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldAddTodoGivenUser() throws Exception {
        User user = createUser(new User("Jane Smith", "jane.smith@gmail.com"));

        Todo todo = new Todo("Complete the Todo Spring API", false, user);
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.id").value(user.getId()));
    }

    @Test
    public void shouldReturnTodosGivenUser() throws Exception {
        int NO_OF_TODOS = 8;
        User user = createUser(new User("Jane Smith", "jane.smith@gmail.com"));
        addTodoToGivenUser(user, NO_OF_TODOS);

        mockMvc.perform(get("/api/users/" + user.getId() + "/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(8)); // Assert number of todos matches the expected value
    }

    @Test
    public void shouldEditTodoGivenUser() throws Exception {
        User user = createUser(new User("Jane Smith", "jane.smith@gmail.com"));
        Todo todo = createTodo(new Todo("New Todo", false, user));

        String updatedTitle = "This should be the title now!";
        todo.setTitle(updatedTitle);
        mockMvc.perform(put("/api/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(updatedTitle));
    }

    @Test
    public void shouldDeleteTodoGivenUser() throws Exception {
        User user = createUser(new User("Jane Smith", "jane.smith@gmail.com"));
        Todo todo = createTodo(new Todo("New Todo", false, user));

        mockMvc.perform(get("/api/users/" + user.getId() + "/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1));

        mockMvc.perform(delete("/api/todos/" + todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/" + user.getId() + "/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0));
    }

    public void addTodoToGivenUser(User user, int NO_OF_TODOS) throws Exception {
        for (int i = 0; i < NO_OF_TODOS; i++) {
            int index = new Random().nextInt(Utils.MOCK_TODOS.length); // This could be deterministic if needed
            boolean completed = new Random().nextDouble(1) >= 0.5; // This could also be deterministic if needed

            createTodo(new Todo(Utils.MOCK_TODOS[index], completed, user));
        }
    }

    private User createUser(User user) throws Exception {
        String response = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, User.class);
    }

    private Todo createTodo(Todo todo) throws Exception {
        String response = mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo))).andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, Todo.class);
    }
}
