package br.com.udemy.llibraryapi.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.udemy.llibraryapi.expection.DataNotFoundExpection;
import br.com.udemy.llibraryapi.expection.LoanExpection;
import br.com.udemy.llibraryapi.model.LoanDTO;
import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.model.entity.Loan;
import br.com.udemy.llibraryapi.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoanService {

	@Autowired
	LoanRepository repository;

	@Autowired
	BookService service;
	
	private final Integer LOAN_DAYS = 4;

	@Transactional
	public Loan insert(Loan dto) throws Exception {

		if (repository.existsByBookAndNotReturned(dto.getBook())) {
			log.info("Emprestando livro={}", dto);
			throw LoanExpection.builder().message("Livro já emprestado").build();
		}
		return repository.save(dto);
	}

	public LoanDTO getById(Integer id) throws DataNotFoundExpection {
		var loan = repository.findById(id).orElseThrow(DataNotFoundExpection::new);
		log.info("Empréstimo encontrado, livro={}", loan);
		return LoanDTO.entityToDto(loan);
	}

	public LoanDTO update(Integer id, LoanDTO loan) throws LoanExpection {
		if (repository.existsById(id)) {
			var bookLoand = Loan.toEntity(loan);
			repository.save(bookLoand);
			log.info("Empréstimo de id={} ", id + " atualizado com sucesso para, empréstimo={}", loan);
			return LoanDTO.entityToDto(bookLoand);
		}
		throw new LoanExpection();
	}

	public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
		return repository.findByBook(book, pageable);
		
	}
	
	public List<Loan> getAllLateLoans(){
		var daysAgo = LocalDate.now().minusDays(LOAN_DAYS);
		return repository.findByLoanDateLessThanAndNotReturned(daysAgo);
	}
}
