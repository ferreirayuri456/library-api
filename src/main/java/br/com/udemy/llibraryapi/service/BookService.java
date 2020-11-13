package br.com.udemy.llibraryapi.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.udemy.llibraryapi.expection.BookExpection;
import br.com.udemy.llibraryapi.model.BookDTO;
import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookService {

	@Autowired
	BookRepository repository;

	@Autowired
	LoanService service;

	@Autowired
	ModelMapper modelMapper;

	@Transactional
	public BookDTO insert(BookDTO dto) throws BookExpection {
		if (!repository.existsById(dto.getId())) {
			var resp = repository.save(BookDTO.dtoToEntity(dto));
			log.info("Livro adicionado com sucesso, livro={})", dto);
			return BookDTO.entityToDto(resp);
		}
		throw BookExpection.builder().httpStatus(HttpStatus.BAD_REQUEST).description("Livro já existente")
				.message("Não foi possível inserir o livro").build();
	}

	@Transactional
	public BookDTO findById(int id) throws BookExpection {
		var book = repository.findById(id).orElseThrow(BookExpection::new);
		log.info("Livro encontrado={}", book);
		return BookDTO.entityToDto(book);
	}

	@Transactional
	public Optional<Book> getByIsbn(String isbn) throws BookExpection {
		log.info("ISBN encontrado={}", isbn);
		return repository.getBookByIsbn(isbn);
	}

	@Transactional
	public void delete(int id) throws BookExpection {
		var response = repository.findById(id).orElseThrow(() -> new BookExpection());
		repository.deleteById(id);
		log.info("Livro deletado com sucesso, id={}, response={}", id, response);
	}

	@Transactional
	public BookDTO update(Integer id, BookDTO dto) throws BookExpection {
		if (repository.existsById(id)) {
			var book = Book.toEntity(dto);
			repository.save(book);
			log.info("Livro de id={} ", id + " atualidado com sucesso para, livro={}", book);
			return BookDTO.entityToDto(book);
		}
		throw new BookExpection();
	}

	@Transactional
	public Page<Book> findLoanByBook(Book filter, Pageable pageable) throws Exception {
		Example<Book> example = Example.of(filter, ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
		return repository.findAll(example, pageable);
	}

	public Optional<Book> getById(Integer id) {
		return repository.findById(id);
	}

}
