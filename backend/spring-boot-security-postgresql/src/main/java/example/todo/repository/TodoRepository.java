package example.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import example.todo.models.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
  List<Todo> findByUser_username(@Param("username") String username);
}
