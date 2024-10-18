package mate.academy.repository.book;

import java.util.List;
import java.util.Optional;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(attributePaths = "categories")
    BookDto save(CreateBookRequestDto createBookRequestDto);

    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(Specification<Book> bookSpecification, Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    Optional<Book> findById(Long id);

    boolean existsByIsbn(String isbn);

    List<Book> findAllByCategories_Id(Long categoryId, Pageable pageable);
}
