package br.com.udemy.llibraryapi.expection;

import org.springframework.http.HttpStatus;

import br.com.udemy.llibraryapi.util.CommonConstants;

public class DataNotFoundExpection extends LoanExpection {

	private static final long serialVersionUID = 695948274439163412L;

	public DataNotFoundExpection() {
		this.setHttpStatus(HttpStatus.NOT_FOUND);
		this.setMessage(CommonConstants.NOT_FOUND);
		this.setDescription(CommonConstants.DATA_NOT_FOUND);
	}

	public DataNotFoundExpection(String msg, String description) {
		this.setHttpStatus(HttpStatus.NOT_FOUND);
		this.setMessage(msg);
		this.setDescription(description);
	}

}
