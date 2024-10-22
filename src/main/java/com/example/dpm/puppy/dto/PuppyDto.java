package com.example.dpm.puppy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PuppyDto {
    private int puppyId;
    private Long memberId; // Decoupled member reference by storing only memberId
    private String name;
    private LocalDate birth;
    private double weight;
}
