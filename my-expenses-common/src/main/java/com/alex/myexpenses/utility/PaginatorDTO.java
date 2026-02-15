package com.alex.myexpenses.utility;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatorDTO<T> {

	private List<T> content;

	private int totalPages;

	private long totalElements;

	private int currentPage;

	private int pageSize;

	private String search;

	private LocalDate startDate;

	private LocalDate endDate;
	
	public PaginatorDTO(List<T> content, int totalPages, long totalElements) {
	    this.content = content;
	    this.totalPages = totalPages;
	    this.totalElements = totalElements;
	}
}
