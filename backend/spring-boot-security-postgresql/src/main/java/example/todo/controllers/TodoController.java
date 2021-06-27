package example.todo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.todo.models.Todo;
import example.todo.models.User;
import example.todo.payload.request.TodoDelRequest;
import example.todo.payload.request.TodoRequest;
import example.todo.repository.TodoRepository;
import example.todo.repository.UserRepository;
import example.todo.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/todo")
public class TodoController {
	@Autowired
	TodoRepository todoRepository;
	@Autowired
	UserRepository userRepository;

	@GetMapping("/list")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public List<Todo> getTodoList() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return todoRepository.findByUser_username(userDetails.getUsername());
	}

	@PostMapping("/post")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void post(@RequestBody TodoRequest message) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		User user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
		Todo todo = new Todo();
		todo.setMessage(message.getMessage().getSubject());
		todo.setUser(user);
		todoRepository.save(todo);
	}

	@PostMapping("/delete")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public void delete(@RequestBody TodoDelRequest id) {
		for (TodoDelRequest.Message message: id.getMessage()) {
			Todo todo = new Todo();
			todo.setId(Integer.valueOf(message.getId()));
			todoRepository.delete(todo);
		}
	}
}
