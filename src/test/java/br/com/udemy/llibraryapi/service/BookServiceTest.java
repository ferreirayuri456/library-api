package br.com.udemy.llibraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.udemy.llibraryapi.expection.BookExpection;
import br.com.udemy.llibraryapi.model.BookDTO;
import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.model.entity.Loan;
import br.com.udemy.llibraryapi.repository.BookRepository;
import br.com.udemy.llibraryapi.repository.LoanRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

	@InjectMocks
	private BookService service;

	@Mock
	private LoanRepository repositoryL;

	@Mock
	private BookRepository repository;

	@Nested
	@DisplayName("CREATE")
	class whenCreate {

		@Test
		@DisplayName("CREATE - When valid - Expected sucess")
		public void create_whenValid_expectedSucess() throws BookExpection {
			when(repository.existsById(1)).thenReturn(Boolean.FALSE);
			when(repository.save(Mockito.any())).thenReturn(createValidBookEntity());
			var result = service.insert(createValidBook());
			assertEquals(BookDTO.entityToDto(createValidBookEntity()), result);
		}

		@Test
		@DisplayName("CREATE - When invalid - Expected Bad Request")
		public void create_whenInvalid_expectedBadRequest() {
			Assertions.assertThrows(BookExpection.class, () -> {
				when(repository.existsById(1)).thenReturn(Boolean.TRUE);
				service.insert(createValidBook());
			});
		}
	}

	@Nested
	@DisplayName("FIND BY ID")
	class whenFindById {

		@Test
		@DisplayName("FIND BY ID - When valid - Expected sucess")
		public void findById_whenValid_expectedSucess() throws BookExpection {
			when(repository.findById(1)).thenReturn(Optional.of(createValidBookEntity()));
			var result = service.findById(1);
			assertEquals(BookDTO.entityToDto(createValidBookEntity()), result);
		}

		@Test
		@DisplayName("FIND BY ID - When invalid - Expected Bad Request")
		public void findById_whenInvalid_expectedBadRequest() {
			Assertions.assertThrows(BookExpection.class, () -> {
				when(repository.findById(1)).thenReturn(Optional.empty());
				service.findById(1);
			});
		}

		@Test
		@DisplayName("FIND LOAN BY BOOK - When valid - Expected sucess")
		public void findLoanByBook_whenValid_expectedSucess() throws Exception {

			List<Book> list = Arrays.asList(createValidBookEntity());
			Page<Book> page = new PageImpl<Book>(list, createPageRequest(), 1);
			when(repository.findAll(any(Example.class), any(PageRequest.class))).thenReturn(page);
			var result = service.findLoanByBook(createValidBookEntity(), createPageRequest());
			assertThat(result.getContent()).isEqualTo(list);
			assertThat(result.getPageable().getPageNumber()).isEqualTo(1);
			assertThat(result.getPageable().getPageSize()).isEqualTo(1);
		}
	}

	@Nested
	@DisplayName("UPDATE")
	class whenUpdate {

		@Test
		@DisplayName("UPDATE - When valid - Expected sucess")
		public void update_whenValid_expectedSucess() throws BookExpection {
			when(repository.save(Mockito.any())).thenReturn(createValidBookEntity());
			when(repository.existsById(1)).thenReturn(Boolean.TRUE);
			var result = service.update(1, createValidBook());
			assertEquals(BookDTO.entityToDto(createValidBookEntity()), result);
		}

		@Test
		@DisplayName("UPDATE - When invalid - Expected bad request")
		public void update_whenInvalid_expectedBadRequest() {
			Assertions.assertThrows(BookExpection.class, () -> {
				when(repository.existsById(1)).thenReturn(Boolean.FALSE);
				service.update(1, createValidBook());
			});
		}
	}

	@Nested
	@DisplayName("DELETE")
	class whenDelete {

		@Test
		@DisplayName("DELETE - When valid - Expected sucess")
		public void delete_whenValid_expectedSucess() throws BookExpection {
			when(repository.findById(1)).thenReturn(Optional.of(createValidBookEntity()));
			doNothing().when(repository).deleteById(1);
			service.delete(1);
			verify(repository).deleteById(1);

		}

		@Test
		@DisplayName("DELETE - When invalid - Expected bad request")
		public void delete_whenInvalid_expectedBadRequest() {
			doThrow(new EmptyResultDataAccessException(1)).when(repository).findById(1);
			Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
				service.delete(1);
			});
		}

	}

	public BookDTO createValidBook() {
		return BookDTO.builder().id(1).author("Ryan Benedetti e Ronan Cranley").title("Use a Cabeça! jQuery")
				.isbn("978-85-7608-757-1").build();
	}

	public Book createValidBookEntity() {
		return Book.builder().id(1).author("Ryan Benedetti e Ronan Cranley").title("Use a Cabeça! jQuery")
				.isbn("978-85-7608-757-1").build();
	}

	public PageRequest createPageRequest() {
		return PageRequest.of(1, 1);
	}

	public Page<Loan> createPageResponse() {
		return new PageImpl<>(List.of(Loan.builder().build()));
	}

}
