package com.example.dpm.post.model;

import java.time.LocalDate;

import com.example.dpm.member.model.MemberEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "puppy")
public class PuppyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int puppyId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member; // Reference to Member entity

    @Column(nullable = false)
    private String name; // Puppy name

    private LocalDate birth; // Birthdate

    private double weight; // Weight
}
