package com.example.dpm.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dpm.post.model.TodoEntity;

public interface TodoRepository extends JpaRepository<TodoEntity, Integer>{

}
