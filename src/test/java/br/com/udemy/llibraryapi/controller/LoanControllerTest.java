package br.com.udemy.llibraryapi.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.udemy.llibraryapi.model.LoanDTO;
import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.model.entity.Loan;
import br.com.udemy.llibraryapi.model.response.LoanDTOResponse;
import br.com.udemy.llibraryapi.service.BookService;
import br.com.udemy.llibraryapi.service.LoanService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

	private final static String URL_BASE = "/api/loan";
	private final static String URL_BASE_ID = "/api/loan/%s";

	@Autowired
	private MockMvc mock;

	@MockBean
	private LoanService service;

	@MockBean
	private BookService serviceB;

	@Nested
	@DisplayName("CREATE")
	class whenCreate {

		@Test
		@DisplayName("CREATE - When valid - Expected sucess")
		public void create_whenValid_expectedSucess() throws Exception {

			var json = new ObjectMapper().writeValueAsString(createValidLoanDTO());
			when(serviceB.getByIsbn(anyString())).thenReturn(Optional.of(createValidBookDTO()));
			when(service.insert(Mockito.any(Loan.class))).thenReturn(createValidBookEntity());
			mock.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON).content(json))
					.andExpect(status().isCreated());
		}

		@Test
		@DisplayName("CREATE - When invalid - Expected bad request")
		public void create_whenInvalid_expectedBadRequest() throws Exception {

			var json = new ObjectMapper().writeValueAsString(createValidLoanDTO());
			when(serviceB.getByIsbn(anyString())).thenReturn(Optional.empty());
			when(service.insert(Mockito.any(Loan.class))).thenReturn(createValidBookEntity());
			mock.perform(post(URL_BASE).contentType(MediaType.APPLICATION_JSON).content(json))
					.andExpect(status().isBadRequest());
		}

	}

	@Nested
	@DisplayName("UPDATE")
	class whenUpdate {

		@Test
		@DisplayName("UPDATE - When valid - Expected sucess")
		public void update_whenValid_expectedSucess() throws Exception {
			when(service.getById(Mockito.anyInt())).thenReturn(createValidLoanDTO());
			mock.perform(patch(String.format(URL_BASE_ID, 1)).contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(createValidLoanResponseDTO())))
					.andExpect(status().isOk());
		}

		@Test
		@DisplayName("UPDATE - When invalid - Expected bad request")
		public void update_whenInvalid_expectedBadRequest() throws Exception {
			mock.perform(patch(String.format(URL_BASE_ID, 1899))).andExpect(status().isBadRequest());
		}
	}

	public LoanDTO createValidLoanDTO() {
		return LoanDTO.builder().id(1).isbn("774-78-7777-745-78-7").customer("Yuri Ferreira da Silva")
				.email("ferreirayuri456@gmail.com").build();
	}

	public Loan createValidBookEntity() {
		return Loan.builder().id(1).customer("Yuri Ferreira").book(createValidBookDTO()).loanDate(LocalDate.now())
				.build();
	}

	public Book createValidBookDTO() {
		return Book.builder().id(1).isbn("1597-8547-55-55555-5").build();
	}

	public LoanDTOResponse createValidLoanResponseDTO() {
		return LoanDTOResponse.builder().returned(Boolean.TRUE).build();
	}

}
