package com.dev.todo3.repository;

import com.dev.todo3.entity.TodoList;
import com.dev.todo3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<TodoList, Long> {

    List<TodoList> findByUser(User user); // Obtém as listas associadas ao usuário

}