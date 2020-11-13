package br.com.udemy.llibraryapi.model;

import br.com.udemy.llibraryapi.model.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {

	private Integer id;
	private String title;
	private String author;
	private String isbn;

	public static Book dtoToEntity(BookDTO dto) {
		return Book.builder().id(dto.getId()).title(dto.getTitle()).author(dto.getAuthor()).isbn(dto.getIsbn()).build();
	}

	public static BookDTO entityToDto(Book book) {
		return BookDTO.builder().id(book.getId()).title(book.getTitle()).author(book.getAuthor()).isbn(book.getIsbn())
				.build();
	}
}
