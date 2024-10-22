package com.example.dpm.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.todo.model.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Integer>{

}
