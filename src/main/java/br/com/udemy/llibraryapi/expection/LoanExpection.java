package br.com.udemy.llibraryapi.expection;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = false)
public class LoanExpection extends Exception {
	private static final long serialVersionUID = 7900134017631099504L;

	@JsonIgnore
	private HttpStatus httpStatus;
	private String message;
	private String code;
	private String description;

	public LoanExpectionBody getOnlyBody() {
		return LoanExpectionBody.builder().code(this.code).message(this.message).description(this.description).build();
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class LoanExpectionBody {

		private String message;
		private String code;
		private String description;
	}
}
