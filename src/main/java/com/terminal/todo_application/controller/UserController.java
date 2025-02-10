package com.terminal.todo_application.controller;

import com.terminal.todo_application.model.Todo;
import com.terminal.todo_application.model.User;
import com.terminal.todo_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Validated @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("")
    public ResponseEntity<User> findUsers(@RequestParam String username) {
        if (username == null)
            return ResponseEntity.badRequest().build();

        User user = userService.findByUsernameStartingWith(username);
        if (user == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/todos")
    public ResponseEntity<List<Todo>> findTodosByUserId(@PathVariable Long id) {
        List<Todo> todos = userService.findTodosByUserId(id);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
