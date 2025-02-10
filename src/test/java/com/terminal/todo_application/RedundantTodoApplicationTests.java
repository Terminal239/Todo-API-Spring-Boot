//package com.terminal.todo_application;
////import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.terminal.todo_application.model.Todo;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class TodoApplicationTests {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@Test
//	public void shouldReturnListOfTodos() throws Exception {
//		Todo todo = new Todo("Sample Task", false);
//		mockMvc.perform(post("/api/todos")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(todo)));
//
//		mockMvc.perform(get("/api/todos"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
//	}
//
//	@Test
//	public void shouldReturnTodoWithGivenId() throws Exception {
//		Todo todo = new Todo("Fetch by ID", false);
//		String response = mockMvc.perform(post("/api/todos")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(todo)))
//				.andReturn().getResponse().getContentAsString();
//
//		Todo createdTodo = objectMapper.readValue(response, Todo.class);
//
//		mockMvc.perform(get("/api/todos/" + createdTodo.getId()))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.title").value("Fetch by ID"));
//	}
//
//	@Test
//	public void shouldCreateTodoForGivenUser() throws Exception {
//		Todo todo = new Todo("Complete the task", false);
//		String requestBody = objectMapper.writeValueAsString(todo);
//
//		mockMvc.perform(post("/api/todos")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(requestBody))
//				.andExpect(status().isCreated())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.title").value("Complete the task"))
//				.andExpect(jsonPath("$.completed").value(false));
//	}
//
//	@Test
//	public void shouldToggleTodoCompletion() throws Exception {
//		Todo todo = new Todo("Toggle completion", false);
//		String response = mockMvc.perform(post("/api/todos")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(todo)))
//				.andReturn().getResponse().getContentAsString();
//
//		Todo createdTodo = objectMapper.readValue(response, Todo.class);
//
//		mockMvc.perform(patch("/api/todos/" + createdTodo.getId() + "/completion"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
//				.andExpect(jsonPath("$.completed").value(true));
//	}
//
//	@Test
//	public void shouldDeleteTodo() throws Exception {
//		Todo todo = new Todo("Delete this task", false);
//		String response = mockMvc.perform(post("/api/todos")
//						.contentType(MediaType.APPLICATION_JSON)
//						.content(objectMapper.writeValueAsString(todo)))
//				.andReturn().getResponse().getContentAsString();
//
//		Todo createdTodo = objectMapper.readValue(response, Todo.class);
//
//		mockMvc.perform(delete("/api/todos/" + createdTodo.getId()))
//				.andExpect(status().isNoContent());
//
//		mockMvc.perform(get("/api/todos/" + createdTodo.getId()))
//				.andExpect(status().isNotFound());
//	}
//
//	@Test
//	public void shouldReturnNotFoundForInvalidId() throws Exception {
//		mockMvc.perform(get("/api/todos/9999"))
//				.andExpect(status().isNotFound());
//	}
//}
