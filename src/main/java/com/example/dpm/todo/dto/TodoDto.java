package com.example.dpm.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDto {
    private int todoId;
    private Long memberId; // Member reference by memberId
    private String content;
    private LocalDate dueData;
    private boolean status;
}
