package br.com.udemy.llibraryapi.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.udemy.llibraryapi.expection.BookExpection;
import br.com.udemy.llibraryapi.model.BookDTO;
import br.com.udemy.llibraryapi.model.LoanDTO;
import br.com.udemy.llibraryapi.model.entity.Book;
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
@RequestMapping("/api/books")
@Slf4j
public class BookController {

	@Autowired
	private BookService service;

	@Autowired
	private LoanService LoanService;

	@Autowired
	private ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Adiciona livros", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Livro cadastrado com sucesso", response = BookDTO.class),
			@ApiResponse(code = 400, message = "Má solicitação para criação de livro"),
			@ApiResponse(code = 401, message = "Ausência de autorização"),
			@ApiResponse(code = 403, message = "Usuário não autorizado a realizar criação de livro"),
			@ApiResponse(code = 404, message = "Livro não localizado com os parâmetros informados"),
			@ApiResponse(code = 405, message = "Método não permitido"),
			@ApiResponse(code = 500, message = "Sistema indisponível") })
	public BookDTO create(
			@ApiParam(value = "Informações dos livros", required = true) @RequestBody BookDTO dto)
			throws BookExpection {
		log.info("Cadastrando livro, resp={}", dto);
		return service.insert(dto);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Recupera livros", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Livro recuperado com sucesso", response = BookDTO.class),
			@ApiResponse(code = 400, message = "Má solicitação para criação de livro"),
			@ApiResponse(code = 401, message = "Ausência de autorização"),
			@ApiResponse(code = 403, message = "Usuário não autorizado a realizar criação de livro"),
			@ApiResponse(code = 404, message = "Livro não localizado com os parâmetros informados"),
			@ApiResponse(code = 405, message = "Método não permitido"),
			@ApiResponse(code = 500, message = "Sistema indisponível") })
	public BookDTO findById(
			@ApiParam(value = "Código do livro", example = "1", required = true) @PathVariable Integer id)
			throws BookExpection {
		log.info("Recuperando o livro de id={}", id);
		return service.findById(id);

	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remove livros", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = { 
			@ApiResponse(code = 204, message = "Livro removido com sucesso", response = BookDTO.class),
			@ApiResponse(code = 400, message = "Má solicitação para criação de livro"),
			@ApiResponse(code = 401, message = "Ausência de autorização"),
			@ApiResponse(code = 403, message = "Usuário não autorizado a realizar criação de livro"),
			@ApiResponse(code = 404, message = "Livro não localizado com os parâmetros informados"),
			@ApiResponse(code = 405, message = "Método não permitido"),
			@ApiResponse(code = 500, message = "Sistema indisponível") })
	public void delete(
			@ApiParam(value = "Código do livro", example = "1", required = true) @PathVariable Integer id)
			throws BookExpection {
		log.info("Removendo livro={}", id);
		service.delete(id);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Atualiza livros", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Livro atualizado com sucesso", response = BookDTO.class),
			@ApiResponse(code = 400, message = "Má solicitação para criação de livro"),
			@ApiResponse(code = 401, message = "Ausência de autorização"),
			@ApiResponse(code = 403, message = "Usuário não autorizado a realizar criação de livro"),
			@ApiResponse(code = 404, message = "Livro não localizado com os parâmetros informados"),
			@ApiResponse(code = 405, message = "Método não permitido"),
			@ApiResponse(code = 500, message = "Sistema indisponível") })
	public BookDTO update(
			@ApiParam(value = "Código do livro", example = "1", required = true) @PathVariable Integer id,
			@ApiParam(value = "Informações do livro", required = true) @RequestBody BookDTO dto) throws BookExpection {
		log.info("Atualizando livro id={}, novo livro={}", id, dto);
		return service.update(id, dto);
	}

	@GetMapping("/{id}/loans")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Recupera livros", authorizations = { @Authorization(value = "OAuth2") })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Livro recuperado com sucesso", response = BookDTO.class),
			@ApiResponse(code = 400, message = "Má solicitação para criação de livro"),
			@ApiResponse(code = 401, message = "Ausência de autorização"),
			@ApiResponse(code = 403, message = "Usuário não autorizado a realizar criação de livro"),
			@ApiResponse(code = 404, message = "Livro não localizado com os parâmetros informados"),
			@ApiResponse(code = 405, message = "Método não permitido"),
			@ApiResponse(code = 500, message = "Sistema indisponível") })
	public Page<LoanDTO> loansByBook(
			@ApiParam(value = "Código do livro", example = "1", required = true) @PathVariable Integer id,
			@ApiParam(value = "Quantidade de número de páginas", required = true) Pageable pageable) {
		Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Page<Loan> result = LoanService.getLoansByBook(book, pageable);
		List<LoanDTO> list = result.getContent().stream().map(loan -> {
			Book loanBook = loan.getBook();
			BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
			LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
			loanDTO.setBook(bookDTO);
			return loanDTO;
		}).collect(Collectors.toList());
		return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
	}

}
