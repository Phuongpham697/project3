package jmaster.io.project3.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDTO<T> {
	private int totalPages;
	private long totalElements;
	private List<T> contents;
}
