package com.example.dpm.todo.service;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.todo.dto.TodoDto;
import com.example.dpm.todo.model.TodoEntity;

@Service
public class TodoService {
	 // Entity to DTO
    public static TodoDto toDto(TodoEntity todoEntity) {
        return TodoDto.builder()
                .todoId(todoEntity.getTodoId())
                .memberId(todoEntity.getMember().getMember_id())
                .content(todoEntity.getContent())
                .dueData(todoEntity.getDueData())
                .status(todoEntity.isStatus())
                .build();
    }

    // DTO to Entity
    public static TodoEntity toEntity(TodoDto todoDTO, MemberEntity member) {
        return TodoEntity.builder()
                .todoId(todoDTO.getTodoId())
                .member(member)
                .content(todoDTO.getContent())
                .dueData(todoDTO.getDueData())
                .status(todoDTO.isStatus())
                .build();
    }
}
