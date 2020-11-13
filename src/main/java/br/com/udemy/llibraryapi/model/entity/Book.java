package br.com.udemy.llibraryapi.model.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.com.udemy.llibraryapi.model.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String title;

	@Column
	private String author;

	@Column
	private String isbn;

	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private List<Loan> loans;

	public static Book toEntity(BookDTO dto) {
		return Book.builder()
				.id(dto.getId())
				.author(dto.getAuthor())
				.title(dto.getTitle())
				.isbn(dto.getIsbn()).build();
	}
}
