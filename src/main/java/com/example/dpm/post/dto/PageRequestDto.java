package com.example.dpm.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDto {
	@Builder.Default //이렇게 하면 data값?! 찾아보기
	private int page = 1;
	
	@Builder.Default
	private int size = 10;
}
