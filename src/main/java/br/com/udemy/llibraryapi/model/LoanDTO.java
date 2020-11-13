package br.com.udemy.llibraryapi.model;

import java.time.LocalDate;

import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.model.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDTO {

	private Integer id;
	private String isbn;
	private String customer;
	private String email;
	private BookDTO book;
	private Boolean returned;

	public static Loan dtoToEntity(LoanDTO dto, Book book) {
		return Loan.builder().id(dto.getId()).customer(dto.getCustomer()).book(book).loanDate(LocalDate.now()).build();
	}

	public static LoanDTO entityToDto(Loan loan) {
		return LoanDTO.builder().id(loan.getId()).customer(loan.getCustomer()).email(loan.getCustomerEmail()).build();
	}

}
