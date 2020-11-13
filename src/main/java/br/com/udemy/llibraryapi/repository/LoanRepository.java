package br.com.udemy.llibraryapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.udemy.llibraryapi.model.entity.Book;
import br.com.udemy.llibraryapi.model.entity.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

	@Query(value = " select case when ( count(l.id) > 0 ) then true else false end "
			+ " from Loan l where l.book = :book and ( l.returned is null or l.returned is false ) ")
	boolean existsByBookAndNotReturned(@Param("book") Book book);

	@Query(value = " select l from Loan as l join l.book as b where b.isbn = :isbn or l.customer = :customer ")
	Page<Loan> findByBookIsbnOrCustomer(@Param("isbn") String isbn, @Param("customer") String customer,
			Pageable pageable);

	Page<Loan> findByBook(Book book, Pageable pageable);

	@Query(value = " select l from Loan l where l.loanDate <= :daysAgo and ( l.returned is null or l.returned is false ) ")
	List<Loan> findByLoanDateLessThanAndNotReturned(@Param("daysAgo") LocalDate daysAgo);

}
