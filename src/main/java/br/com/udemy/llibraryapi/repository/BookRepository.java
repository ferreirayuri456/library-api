package br.com.udemy.llibraryapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.udemy.llibraryapi.model.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	Optional<Book> getBookByIsbn(String isbn);
	

}
