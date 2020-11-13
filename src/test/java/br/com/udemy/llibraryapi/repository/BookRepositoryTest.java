package br.com.udemy.llibraryapi.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.udemy.llibraryapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	TestEntityManager entityManager;

	@Autowired
	BookRepository repository;

	@Test
	@DisplayName("CREATE - When valid - Expected sucess")
	public void create_whenValid_expectedSucess() {

		var id = 1;

		entityManager.persist(createValidBook());

		boolean existsById = repository.existsById(id);

		assertTrue(existsById);
	}

	@Test
	@DisplayName("CREATE - When invalid - Expected Bad Request")
	public void create_whenInvalid_expectedBadRequest() {

		var id = 1;

		boolean existsById = repository.existsById(id);

		assertFalse(existsById);
	}

	@Test
	@DisplayName("FIND BY ID - When valid - Expected sucess")
	public void findById_whenValid_expectedSucess() {
		Book book = entityManager.persist(createValidBook("159-77-7777-777-7"));

		Optional<Book> optional = repository.findById(book.getId());

		assertTrue(optional.isPresent());
	}

	public Book createValidBook() {
		return Book.builder().author("Ryan Benedetti e Ronan Cranley").title("Use a Cabeça! jQuery")
				.isbn("978-85-7608-757-1").build();
	}

	public Book createValidBook(String isbn) {
		return Book.builder().author("Ryan Benedetti e Ronan Cranley").title("Use a Cabeça! jQuery").isbn(isbn).build();
	}

}
