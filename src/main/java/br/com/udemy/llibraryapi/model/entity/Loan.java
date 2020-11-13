package br.com.udemy.llibraryapi.model.entity;
 
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.udemy.llibraryapi.model.LoanDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Loan {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 100)
	private String customer;

	@Column(name = "customer_email")
	private String customerEmail;

	@JoinColumn(name = "id_book")
	@ManyToOne
	private Book book;

	@Column
	private LocalDate loanDate;

	@Column
	private Boolean returned;

	public static Loan toEntity(LoanDTO loan) {
		return Loan.builder()
				.id(loan.getId())
				.customer(loan.getCustomer())
				.customerEmail(loan.getEmail())
				.loanDate(LocalDate.now())
				.returned(loan.getReturned()).build();
	}
}