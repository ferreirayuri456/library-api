package br.com.udemy.llibraryapi.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.udemy.llibraryapi.model.BookDTO;
import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.service.BookService;
import br.com.udemy.llibraryapi.service.LoanService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

	@Autowired
	private MockMvc mock;

	@MockBean
	private BookService service;

	@MockBean
	private LoanService serviceL;

	private final static String URL_BASE = "/api/books";
	private final static String URL_BASE_ID = "/api/books/%s";
	private final static String URL_BASE_INVALID = "/api/book";
	private final static String URL_BASE_LOAN = "/api/books/%s/loans";

	@Nested
	@DisplayName("CREATE")
	class whenCreate {

		@Test
		@DisplayName("Create - When valid - Expected sucess")
		public void create_whenValid_expectedSucess() throws Exception {
			when(service.insert(createValidBookDTO())).thenReturn(createValidBookDTO());
			var json = new ObjectMapper().writeValueAsString(createValidBookDTO());
			mock.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON).content(json))
					.andExpect(status().isCreated());
		}

		@Test
		@DisplayName("Create - When invalid - Expected bad request")
		public void create_whenInvalid_expectedBadRequest() throws Exception {
			mock.perform(post(String.format(URL_BASE))).andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Create - When invalid - Expected not found")
		public void create_whenInvalid_expectedNotFound() throws Exception {
			mock.perform(post(String.format(URL_BASE_INVALID))).andExpect(status().isNotFound());

		}

	}

	@Nested
	@DisplayName("UPDATE")
	class whenUpdate {

		@Test
		@DisplayName("Update - When valid - Expected sucess")
		public void update_whenValid_expectedSucess() throws Exception {
			when(service.update(1, createValidBookDTOPut())).thenReturn(createValidBookDTO());
			mock.perform(put(String.format(URL_BASE_ID, 1)).contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(createValidBookDTO())))
					.andExpect(status().isNoContent());
		}

		@Test
		@DisplayName("Update - When invalid - Expected bad request")
		public void update_whenInvalid_expectedBadRequest() throws Exception {
			mock.perform(put(String.format(URL_BASE_ID, 156))).andExpect(status().isBadRequest());
		}

	}

	@Nested
	@DisplayName("DELETE")
	class whenDelete {

		@Test
		@DisplayName("Delete - When valid - Expected sucess")
		public void delete_whenValid_expectedSucess() throws Exception {
			when(service.findById(anyInt())).thenReturn(createValidBookDTO());
			mock.perform(delete(String.format(URL_BASE_ID, anyInt()))).andExpect(status().isNoContent());
		}

		@Test
		@DisplayName("Delete - When invalid - Expected bad request")
		public void delete_whenInvalid_expectedBadRequest() throws Exception {
			mock.perform(delete(String.format(URL_BASE_ID, "AA"))).andExpect(status().isBadRequest());
		}

	}

	@Nested
	@DisplayName("FIND BY ID")
	class whenFindById {

		@Test
		@DisplayName("FIND BY ID - When valid - Expected sucess")
		public void findById_whenValid_expectedSucess() throws Exception {

			when(service.findById(1)).thenReturn(createValidBookDTO());
			mock.perform(get(String.format(URL_BASE_ID, 1))).andExpect(status().isOk());
		}

		@Test
		@DisplayName("FIND BY ID - When invalid - Expected bad request")
		public void findById_whenValid_expectedBadRequest() throws Exception {

			when(service.findById(anyInt())).thenReturn(createInvalidBook());
			mock.perform(get(String.format(URL_BASE_ID, anyInt()))).andExpect(status().isOk());
		}

//		@Test
//		@DisplayName("FIND BY ID - When valid - Expected sucess")
//		public void findByBorrowedBook_whenValid_expectedSucess() throws Exception {
//			when(service.findLoanByBook(createValidBook(), createPageRequest())).thenReturn(createPageResponse());
//			mock.perform(get(String.format(URL_BASE_LOAN, 1))).andExpect(status().isOk());
//		}

		@Test
		@DisplayName("FIND BY ID - When invalid - Expected bad request")
		public void findByBorrowdBook_whenInvalid_expectedBadRequest() throws Exception {
			mock.perform(get(URL_BASE_LOAN, 1)).andExpect(status().isBadRequest());
		}
	}

	public BookDTO createValidBookDTO() {
		return BookDTO.builder().id(1).author("Ryan Benedetti e Ronan Cranley").title("Use a Cabeça! jQuery")
				.isbn("978-85-7608-757-1").build();
	}

	public BookDTO createValidBookDTOPut() {
		return BookDTO.builder().author("Casa Publicadora Brasileira").title("Neemias: princípios de liderança bíblica")
				.isbn("159-76-7898-965-2").build();
	}

	public Book createValidBook() {
		return Book.builder().id(1).author("Ryan Benedetti e Ronan Cranley").title("Use a Cabeça! jQuery")
				.isbn("978-85-7608-757-1").build();
	}

	public BookDTO createInvalidBook() {
		return null;
	}

	public PageRequest createPageRequest() {
		return PageRequest.of(1, 1);
	}

	public Page<Book> createPageResponse() {
		return new PageImpl<>(List.of(Book.builder().id(1).build()));
	}
}
