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
public class BookExpection extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6820420659993895821L;

	@JsonIgnore
	private HttpStatus httpStatus;
	private String message;
	private String code;
	private String description;

	public BookExpectionBody getOnlyBody() {
		return BookExpectionBody.builder().code(this.code).message(this.message).description(this.description).build();
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class BookExpectionBody {

		private String message;
		private String code;
		private String description;
	}

}