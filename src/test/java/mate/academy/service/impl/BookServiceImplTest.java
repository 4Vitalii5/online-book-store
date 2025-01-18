package mate.academy.service.impl;

import static mate.academy.util.TestConstants.BOOK_NOT_FOUND_MESSAGE;
import static mate.academy.util.TestConstants.CATEGORY_NOT_FOUND_MESSAGE;
import static mate.academy.util.TestConstants.DUPLICATE_ISBN_MESSAGE;
import static mate.academy.util.TestConstants.FIRST_RECORD;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_ID;
import static mate.academy.util.TestUtil.BOOK_DTO_WITHOUT_CATEGORY_IDS;
import static mate.academy.util.TestUtil.BOOK_PAGE;
import static mate.academy.util.TestUtil.CATEGORY_DTO;
import static mate.academy.util.TestUtil.CREATE_BOOK_REQUEST_DTO;
import static mate.academy.util.TestUtil.FIRST_CATEGORY;
import static mate.academy.util.TestUtil.NEW_BOOK;
import static mate.academy.util.TestUtil.PAGEABLE;
import static mate.academy.util.TestUtil.SAMPLE_BOOK;
import static mate.academy.util.TestUtil.SAMPLE_BOOK_DTO;
import static mate.academy.util.TestUtil.SEARCH_PARAMETERS;
import static mate.academy.util.TestUtil.SECOND_CATEGORY;
import static mate.academy.util.TestUtil.UPDATED_BOOK;
import static mate.academy.util.TestUtil.UPDATED_BOOK_DTO;
import static mate.academy.util.TestUtil.UPDATE_BOOK_REQUEST_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.exception.DuplicateResourceException;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.book.BookSpecificationBuilder;
import mate.academy.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify creation of new book")
    void save_validCreateBookRequestDto_returnsBookDto() {
        //Given
        when(bookMapper.toEntity(CREATE_BOOK_REQUEST_DTO)).thenReturn(SAMPLE_BOOK);
        when(bookRepository.save(SAMPLE_BOOK)).thenReturn(SAMPLE_BOOK);
        when(bookRepository.existsByIsbn(SAMPLE_BOOK.getIsbn())).thenReturn(false);
        when(bookMapper.toDto(SAMPLE_BOOK)).thenReturn(SAMPLE_BOOK_DTO);

        //When
        BookDto savedBookDto = bookService.save(CREATE_BOOK_REQUEST_DTO);

        //Then
        assertThat(savedBookDto).isEqualTo(SAMPLE_BOOK_DTO);
        verify(bookRepository, times(1))
                .existsByIsbn(SAMPLE_BOOK.getIsbn());
        verify(bookRepository, times(1)).save(SAMPLE_BOOK);
        verify(bookMapper, times(1)).toDto(SAMPLE_BOOK);
    }

    @Test
    @DisplayName("Verify save() method when isbn already exists")
    void save_duplicateIsbn_throwsDuplicateResourceException() {
        //Given
        String expected = String.format(DUPLICATE_ISBN_MESSAGE, CREATE_BOOK_REQUEST_DTO.isbn());
        when(bookMapper.toEntity(CREATE_BOOK_REQUEST_DTO)).thenReturn(NEW_BOOK);
        when(bookRepository.existsByIsbn(NEW_BOOK.getIsbn())).thenReturn(true);

        //When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> bookService.save(CREATE_BOOK_REQUEST_DTO));
        String actual = exception.getMessage();

        //Then
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1))
                .existsByIsbn(NEW_BOOK.getIsbn());
    }

    @Test
    @DisplayName("Find all books for page")
    void findAll_validPageable_returnsAllBooks() {
        //Given
        when(bookMapper.toDto(SAMPLE_BOOK)).thenReturn(SAMPLE_BOOK_DTO);
        when(bookRepository.findAll(PAGEABLE)).thenReturn(BOOK_PAGE);

        //When
        List<BookDto> bookDtos = bookService.findAll(PAGEABLE);

        //Then
        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos).containsExactly(SAMPLE_BOOK_DTO);
        verify(bookRepository, times(1)).findAll(PAGEABLE);
        verify(bookMapper, times(1)).toDto(SAMPLE_BOOK);
    }

    @Test
    @DisplayName("Find book by id")
    void findById_validBookId_returnsBookDto() {
        //Given
        when(bookMapper.toDto(SAMPLE_BOOK)).thenReturn(SAMPLE_BOOK_DTO);
        when(bookRepository.findById(SAMPLE_BOOK.getId())).thenReturn(Optional.of(SAMPLE_BOOK));

        //When
        BookDto foundBookDto = bookService.findById(SAMPLE_BOOK.getId());

        //Then
        assertThat(foundBookDto).isEqualTo(SAMPLE_BOOK_DTO);
        verify(bookRepository, times(1)).findById(SAMPLE_BOOK.getId());
        verify(bookMapper, times(1)).toDto(SAMPLE_BOOK);
    }

    @Test
    @DisplayName("Verify findById method with invalid book id")
    void findById_invalidBookId_throwsEntityNotFoundException() {
        //Given
        when(bookRepository.findById(SAMPLE_BOOK.getId())).thenReturn(Optional.empty());
        String expected = String.format(BOOK_NOT_FOUND_MESSAGE, SAMPLE_BOOK.getId());

        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(SAMPLE_BOOK.getId()));
        String actual = exception.getMessage();

        //Then
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(SAMPLE_BOOK.getId());
    }

    @Test
    @DisplayName("Update book by valid id")
    void updateBookById_validBookIdAndCreateBookRequestDto_returnsUpdatedBookDto() {
        //Given
        when(bookRepository.findById(SAMPLE_BOOK_ID)).thenReturn(Optional.of(SAMPLE_BOOK));
        when(bookRepository.save(SAMPLE_BOOK)).thenReturn(UPDATED_BOOK);
        when(bookRepository.existsByIsbn(UPDATE_BOOK_REQUEST_DTO.isbn())).thenReturn(false);
        when(categoryService.getById(FIRST_CATEGORY.getId())).thenReturn(CATEGORY_DTO);
        when(categoryMapper.toEntity(CATEGORY_DTO)).thenReturn(SECOND_CATEGORY);
        doNothing().when(bookMapper).updateBookFromDto(UPDATE_BOOK_REQUEST_DTO, SAMPLE_BOOK);
        when(bookMapper.toDto(UPDATED_BOOK)).thenReturn(UPDATED_BOOK_DTO);

        //When
        BookDto actual = bookService.updateBookById(SAMPLE_BOOK_ID, UPDATE_BOOK_REQUEST_DTO);

        //Then
        assertThat(actual).isEqualTo(UPDATED_BOOK_DTO);
        verify(bookRepository, times(1)).findById(SAMPLE_BOOK_ID);
        verify(bookRepository, times(1))
                .existsByIsbn(UPDATE_BOOK_REQUEST_DTO.isbn());
        verify(categoryService, times(1)).getById(FIRST_CATEGORY.getId());
        verify(categoryMapper, times(1)).toEntity(CATEGORY_DTO);
        verify(bookMapper, times(1))
                .updateBookFromDto(UPDATE_BOOK_REQUEST_DTO, SAMPLE_BOOK);
        verify(bookMapper, times(1)).toDto(UPDATED_BOOK);
    }

    @Test
    @DisplayName("Verify updateBookById method when isbn already exists")
    void updateBookById_duplicatedIsbn_throwsDuplicatedSourceException() {
        //Given
        String expected = String.format(DUPLICATE_ISBN_MESSAGE, UPDATE_BOOK_REQUEST_DTO.isbn());
        when(bookRepository.findById(SAMPLE_BOOK_ID)).thenReturn(Optional.of(SAMPLE_BOOK));
        when(bookRepository.existsByIsbn(UPDATE_BOOK_REQUEST_DTO.isbn())).thenReturn(true);

        //When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> bookService.updateBookById(SAMPLE_BOOK_ID, UPDATE_BOOK_REQUEST_DTO));
        String actual = exception.getMessage();

        //Then
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(SAMPLE_BOOK_ID);
        verify(bookRepository, times(1))
                .existsByIsbn(UPDATE_BOOK_REQUEST_DTO.isbn());
    }

    @Test
    @DisplayName("Delete book by id")
    void deleteById_validBookId_isOk() {
        //Given
        doNothing().when(bookRepository).deleteById(SAMPLE_BOOK_ID);

        // When
        bookService.deleteById(SAMPLE_BOOK_ID);

        // Then
        verify(bookRepository, times(1)).deleteById(SAMPLE_BOOK_ID);
    }

    @Test
    @DisplayName("Verify search method works with given search parameters and pageable")
    void search_validSearchParametersAndPageable_returnsMatchingBooks() {
        //Given
        Specification<Book> specification = bookSpecificationBuilder.build(SEARCH_PARAMETERS);
        when(bookRepository.findAll(specification, PAGEABLE)).thenReturn(BOOK_PAGE);
        when(bookMapper.toDto(SAMPLE_BOOK)).thenReturn(SAMPLE_BOOK_DTO);

        //When
        List<BookDto> actual = bookService.search(SEARCH_PARAMETERS, PAGEABLE);

        //Then
        assertThat(actual).hasSize(1);
        assertThat(actual.get(FIRST_RECORD)).isEqualTo(SAMPLE_BOOK_DTO);
        verify(bookSpecificationBuilder, atLeastOnce()).build(SEARCH_PARAMETERS);
        verify(bookRepository, times(1)).findAll(specification, PAGEABLE);
        verify(bookMapper, times(1)).toDto(SAMPLE_BOOK);
        verifyNoMoreInteractions(bookSpecificationBuilder, bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Find books in certain category id")
    void getBooksByCategoryId_validCategoryIdAndPageable_returnsAllFromCategory() {
        //Given
        when(categoryService.getById(FIRST_CATEGORY.getId())).thenReturn(CATEGORY_DTO);
        when(bookMapper.toDtoWithoutCategories(SAMPLE_BOOK))
                .thenReturn(BOOK_DTO_WITHOUT_CATEGORY_IDS);
        when(bookRepository.findAllByCategories_Id(FIRST_CATEGORY.getId(), PAGEABLE))
                .thenReturn(List.of(SAMPLE_BOOK));

        //When
        List<BookDtoWithoutCategoryIds> bookDtos = bookService.getBooksByCategoryId(
                FIRST_CATEGORY.getId(), PAGEABLE);

        //Then
        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(BOOK_DTO_WITHOUT_CATEGORY_IDS);
        verify(bookRepository, times(1))
                .findAllByCategories_Id(FIRST_CATEGORY.getId(), PAGEABLE);
        verify(bookMapper, times(1)).toDtoWithoutCategories(SAMPLE_BOOK);
        verify(categoryService, times(1)).getById(FIRST_CATEGORY.getId());
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryService);
    }

    @Test
    @DisplayName("Verify getBooksByCategoryId method with invalid category id")
    void getBooksByCategoryId_invalidCategory_throwsEntityNotFoundException() {
        //Given
        String expected = String.format(CATEGORY_NOT_FOUND_MESSAGE, FIRST_CATEGORY.getId());
        when(categoryService.getById(FIRST_CATEGORY.getId())).thenReturn(null);

        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBooksByCategoryId(FIRST_CATEGORY.getId(), PAGEABLE));
        String actual = exception.getMessage();

        //Then
        assertThat(actual).isEqualTo(expected);
        verify(categoryService, times(1)).getById(FIRST_CATEGORY.getId());
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryService);
    }
}
