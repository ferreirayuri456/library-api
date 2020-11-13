package br.com.udemy.llibraryapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.udemy.llibraryapi.expection.DataNotFoundExpection;
import br.com.udemy.llibraryapi.expection.LoanExpection;
import br.com.udemy.llibraryapi.model.LoanDTO;
import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.model.entity.Loan;
import br.com.udemy.llibraryapi.model.response.LoanDTOResponse;
import br.com.udemy.llibraryapi.repository.LoanRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

	@InjectMocks
	private LoanService service;

	@Mock
	private LoanRepository repository;

	@Nested
	@DisplayName("CREATE")
	class whenCreate {

		@Test
		@DisplayName("CREATE - When valid - Expected sucess")
		public void create_whenValid_expectedSucess() throws Exception {
			when(repository.existsByBookAndNotReturned(createValidEntityBook())).thenReturn(Boolean.FALSE);
			when(repository.save(createValidEntityLoan())).thenReturn(createValidEntityLoan());
			var result = service.insert(createValidEntityLoan());
			assertEquals(createValidEntityLoan(), result);
		}

		@Test
		@DisplayName("CREATE - When valid - Expected bad request")
		public void create_whenValid_expectedBadRequest() throws Exception {
			Assertions.assertThrows(LoanExpection.class, () -> {
				when(repository.existsByBookAndNotReturned(createValidEntityBook())).thenReturn(Boolean.TRUE);
				service.insert(createValidEntityLoan());
			});

		}

	}

	@Nested
	@DisplayName("UPDATE")
	class whenUpdate {
		@Test
		@DisplayName("UPDATE - When valid - Expected sucess")
		public void update_whenValid_expectedSucess() throws LoanExpection {
			when(repository.save(Mockito.any())).thenReturn(createValidEntityLoan());
			when(repository.existsById(1)).thenReturn(Boolean.TRUE);
			var result = service.update(1, createValidLoanDTO());
			assertEquals(LoanDTO.entityToDto(createValidEntityLoan()), result);
		}

		@Test
		@DisplayName("UPDATE - When invalid - Expected bad request")
		public void update_whenInvalid_expectedBadRequest() {
			Assertions.assertThrows(LoanExpection.class, () -> {
				when(repository.existsById(1)).thenReturn(Boolean.FALSE);
				service.update(1, createValidLoanDTO());
			});
		}

	}

	@Nested
	@DisplayName("GET BY ID")
	class whenGetById {

		@Test
		@DisplayName("GET BY ID - When valid - Expected sucess")
		public void getById_whenValid_expectedSucess() throws DataNotFoundExpection {
			when(repository.findById(anyInt())).thenReturn(Optional.of(createValidEntityLoan()));
			var result = service.getById(1);
			assertEquals(LoanDTO.entityToDto(createValidEntityLoan()), result);
		}

		@Test
		@DisplayName("GET BY ID - When invalid - Expected bad request")
		public void getById_whenInvalid_expectedBadRequest() {
			Assertions.assertThrows(LoanExpection.class, () -> {
				when(repository.findById(1)).thenReturn(Optional.empty());
				service.getById(1);
			});

		}

	}

	public Loan createValidEntityLoan() {
		return Loan.builder().id(1).customer("Yuri Ferreira").loanDate(LocalDate.now()).book(createValidEntityBook())
				.returned(Boolean.TRUE).build();
	}

	public LoanDTO createValidLoanDTO() {
		return LoanDTO.builder().id(1).isbn("774-78-7777-745-78-7").customer("Yuri Ferreira").returned(Boolean.TRUE)
				.build();
	}

	public Book createValidEntityBook() {
		return Book.builder().id(1).author("Ryan Benedetti e Ronan Cranley").isbn("978-85-7608-757-1")
				.title("Use a Cabeça! jQuery").build();
	}

	public LoanDTOResponse createValidLoanDTOResponse() {
		return LoanDTOResponse.builder().returned(Boolean.TRUE).build();
	}
}
