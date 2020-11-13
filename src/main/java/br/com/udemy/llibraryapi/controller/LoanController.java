package br.com.udemy.llibraryapi.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.udemy.llibraryapi.model.BookDTO;
import br.com.udemy.llibraryapi.model.LoanDTO;
import br.com.udemy.llibraryapi.model.entity.Loan;
import br.com.udemy.llibraryapi.service.BookService;
import br.com.udemy.llibraryapi.service.LoanService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/loan")
@Slf4j
public class LoanController {

	@Autowired
	private LoanService service;

	@Autowired
	private BookService serviceB;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adiciona empréstimos", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Empréstimo cadastrado com sucesso", response = BookDTO.class),
			@ApiResponse(code = 400, message = "Má solicitação para criação de empréstimo"),
			@ApiResponse(code = 401, message = "Ausência de autorização"),
			@ApiResponse(code = 403, message = "Usuário não autorizado a realizar criação de livro"),
			@ApiResponse(code = 404, message = "Empréstimo não localizado com os parâmetros informados"),
			@ApiResponse(code = 405, message = "Método não permitido"),
			@ApiResponse(code = 500, message = "Sistema indisponível") })
	public Integer create(
			@ApiParam(value = "Informações dos livros", required = true) @RequestBody LoanDTO dto)
			throws Exception {
		var book = serviceB.getByIsbn(dto.getIsbn()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não encontrado pelo ISBN passado"));
		var entity = Loan.builder().book(book).customer(dto.getCustomer()).loanDate(LocalDate.now()).build();
		log.info("Cadastrando empréstimo, resp={}", dto);
		entity = service.insert(entity);
		return entity.getId();
	}

	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Atualiza empréstimos", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Empréstimo cadastrado com sucesso", response = BookDTO.class),
			@ApiResponse(code = 400, message = "Má solicitação para criação de empréstimo"),
			@ApiResponse(code = 401, message = "Ausência de autorização"),
			@ApiResponse(code = 403, message = "Usuário não autorizado a realizar criação de livro"),
			@ApiResponse(code = 404, message = "Empréstimo não localizado com os parâmetros informados"),
			@ApiResponse(code = 405, message = "Método não permitido"),
			@ApiResponse(code = 500, message = "Sistema indisponível") })
	public void update(
			@ApiParam(value = "Id do empréstimo", required = true) @PathVariable Integer id,
			@ApiParam(value = "Informações do empréstimo") @RequestBody LoanDTO dto) throws Exception {
		service.update(id, dto);
	}

}
