package com.example.dpm.todo.model;

import java.time.LocalDate;

import com.example.dpm.member.model.MemberEntity;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "todo")
public class TodoEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int todoId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member; // Reference to Member entity

    @Column(nullable = false)
    private String content; // Task content

    @Column(nullable = false)
    private LocalDate dueData; // Due date

    private boolean status; // Task status
}
