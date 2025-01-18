package mate.academy.repository.book;

import static mate.academy.util.TestConstants.FIRST_RECORD;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_ISBN;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_TITLE;
import static mate.academy.util.TestConstants.SEARCH_AUTHOR;
import static mate.academy.util.TestConstants.SEARCH_PAGE_NUMBER;
import static mate.academy.util.TestConstants.SEARCH_PAGE_SIZE;
import static mate.academy.util.TestConstants.SEARCH_TITLE;
import static mate.academy.util.TestConstants.VALID_FIRST_BOOK_ID;
import static mate.academy.util.TestConstants.VALID_SECOND_BOOK_ID;
import static mate.academy.util.TestUtil.NEW_BOOK;
import static mate.academy.util.TestUtil.PAGEABLE;
import static mate.academy.util.TestUtil.SPECIFICATION;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/clean-database.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/books/add-books.sql",
        "classpath:database/categories/add-categories.sql",
        "classpath:database/books-categories/set-books-to-categories.sql"
})
@Sql(scripts = {
        "classpath:database/books/remove-books.sql",
        "classpath:database/categories/remove-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Verify save() method works")
    void save_validBook_returnsBook() {
        // When
        Book savedBook = bookRepository.save(NEW_BOOK);

        // Then
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo(NEW_BOOK.getTitle());
    }

    @Test
    @DisplayName("Verify findById() method works")
    void findById_validId_returnsBook() {
        // When
        Optional<Book> foundBook = bookRepository.findById(VALID_FIRST_BOOK_ID);

        // Then
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getId()).isEqualTo(VALID_FIRST_BOOK_ID);
    }

    @Test
    @DisplayName("Verify existsByIsbn() method works")
    void existsByIsbn_validIsbn_returnsTrue() {
        // When
        boolean exists = bookRepository.existsByIsbn(SAMPLE_BOOK_ISBN);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Verify findAll() method works with pageable")
    void findAll_pageable_returnsBooksPage() {
        // When
        Page<Book> bookPage = bookRepository.findAll(PAGEABLE);

        // Then
        assertThat(bookPage.getContent()).isNotEmpty();
        assertThat(bookPage.getContent().get(FIRST_RECORD).getTitle()).isEqualTo(SAMPLE_BOOK_TITLE);
    }

    @Test
    @DisplayName("Verify findAll(Specification, Pageable) method works")
    void findAll_withSpecificationAndPageable_returnsBooksPage() {
        // When
        Page<Book> bookPage = bookRepository.findAll(SPECIFICATION, PAGEABLE);

        // Then
        assertThat(bookPage.getContent()).isNotEmpty();
        assertThat(bookPage.getContent().get(FIRST_RECORD).getAuthor()).isEqualTo(SEARCH_AUTHOR);
    }

    @Test
    @DisplayName("Verify findAllByCategories_Id() method works")
    void findAllByCategories_Id_validCategoryId_returnsBooks() {
        // When
        List<Book> books = bookRepository.findAllByCategories_Id(
                VALID_SECOND_BOOK_ID, PageRequest.of(SEARCH_PAGE_NUMBER, SEARCH_PAGE_SIZE));

        // Then
        assertThat(books).isNotEmpty();
        assertThat(books.get(FIRST_RECORD).getTitle()).isEqualTo(SEARCH_TITLE);
    }
}
