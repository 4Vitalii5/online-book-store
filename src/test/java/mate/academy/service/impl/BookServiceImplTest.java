package mate.academy.service.impl;

import static mate.academy.constant.BookTestConstants.DUPLICATE_ISBN_MESSAGE;
import static mate.academy.constant.BookTestConstants.ENTITY_NOT_FOUND_MESSAGE;
import static mate.academy.constant.BookTestConstants.FIRST_RECORD;
import static mate.academy.constant.BookTestConstants.SAMPLE_BOOK_AUTHOR;
import static mate.academy.constant.BookTestConstants.SAMPLE_BOOK_COVER_IMAGE;
import static mate.academy.constant.BookTestConstants.SAMPLE_BOOK_DESCRIPTION;
import static mate.academy.constant.BookTestConstants.SAMPLE_BOOK_ID;
import static mate.academy.constant.BookTestConstants.SAMPLE_BOOK_ISBN;
import static mate.academy.constant.BookTestConstants.SAMPLE_BOOK_PRICE;
import static mate.academy.constant.BookTestConstants.SAMPLE_BOOK_TITLE;
import static mate.academy.constant.BookTestConstants.SAMPLE_CATEGORY_IDS;
import static mate.academy.constant.BookTestConstants.SEARCH_PAGE_NUMBER;
import static mate.academy.constant.BookTestConstants.SEARCH_PAGE_SIZE;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_AUTHOR;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_COVER_IMAGE;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_DESCRIPTION;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_ISBN;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_PRICE;
import static mate.academy.constant.BookTestConstants.UPDATED_BOOK_TITLE;
import static mate.academy.constant.CategoryTestConstants.CATEGORY_DESCRIPTION;
import static mate.academy.constant.CategoryTestConstants.CATEGORY_ID;
import static mate.academy.constant.CategoryTestConstants.CATEGORY_NAME;
import static mate.academy.constant.CategoryTestConstants.SECOND_CATEGORY_DESCRIPTION;
import static mate.academy.constant.CategoryTestConstants.SECOND_CATEGORY_ID;
import static mate.academy.constant.CategoryTestConstants.SECOND_CATEGORY_NAME;
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
import java.util.Set;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.BookSearchParameters;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.CategoryDto;
import mate.academy.exception.DuplicateResourceException;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Book;
import mate.academy.model.Category;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.book.BookSpecificationBuilder;
import mate.academy.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private Category firstCategory;
    private Book sampleBook;
    private BookDto sampleBookDto;
    private CategoryDto categoryDto;
    private BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds;

    @BeforeEach
    void setUp() {
        firstCategory = new Category(CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION);

        sampleBook = new Book(
                SAMPLE_BOOK_ID, SAMPLE_BOOK_TITLE, SAMPLE_BOOK_AUTHOR,
                SAMPLE_BOOK_ISBN, SAMPLE_BOOK_PRICE, SAMPLE_BOOK_DESCRIPTION,
                SAMPLE_BOOK_COVER_IMAGE, Set.of(firstCategory)
        );

        sampleBookDto = new BookDto(
                sampleBook.getId(), sampleBook.getTitle(), sampleBook.getAuthor(),
                sampleBook.getIsbn(), sampleBook.getPrice(), sampleBook.getDescription(),
                sampleBook.getCoverImage(), List.of(firstCategory.getId())
        );

        categoryDto = new CategoryDto(
                firstCategory.getId(), firstCategory.getName(),
                firstCategory.getDescription()
        );

        bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(
                sampleBook.getId(), sampleBook.getTitle(),
                sampleBook.getAuthor(), sampleBook.getIsbn(), sampleBook.getPrice(),
                sampleBook.getDescription(), sampleBook.getCoverImage()
        );
    }

    @Test
    @DisplayName("Verify creation of new book")
    void save_validCreateBookRequestDto_returnsBookDto() {
        //Given
        CreateBookRequestDto requestDto = createBookRequestDto();

        when(bookMapper.toEntity(requestDto)).thenReturn(sampleBook);
        when(bookRepository.save(sampleBook)).thenReturn(sampleBook);
        when(bookRepository.existsByIsbn(requestDto.isbn())).thenReturn(false);
        when(bookMapper.toDto(sampleBook)).thenReturn(sampleBookDto);
        //When
        BookDto savedBookDto = bookService.save(requestDto);
        //Then
        assertThat(savedBookDto).isEqualTo(sampleBookDto);
        verify(bookRepository, times(1)).existsByIsbn(requestDto.isbn());
        verify(bookRepository, times(1)).save(sampleBook);
        verify(bookMapper, times(1)).toDto(sampleBook);
    }

    @Test
    @DisplayName("Verify save() method when isbn already exists")
    void save_duplicateIsbn_throwsDuplicateResourceException() {
        //Given
        CreateBookRequestDto requestDto = createBookRequestDto();
        String expected = String.format(DUPLICATE_ISBN_MESSAGE, requestDto.isbn());

        when(bookMapper.toEntity(requestDto)).thenReturn(sampleBook);
        when(bookRepository.existsByIsbn(requestDto.isbn())).thenReturn(true);
        //When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> bookService.save(requestDto)
        );
        String actual = exception.getMessage();
        //Then
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).existsByIsbn(requestDto.isbn());
    }

    @Test
    @DisplayName("Find all books for page")
    void findAll_validPageable_returnsAllBooks() {
        //Given
        Pageable pageable = PageRequest.of(SEARCH_PAGE_NUMBER, SEARCH_PAGE_SIZE);
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook), pageable, 1);

        when(bookMapper.toDto(sampleBook)).thenReturn(sampleBookDto);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        //When
        List<BookDto> bookDtos = bookService.findAll(pageable);
        //Then
        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos).containsExactly(sampleBookDto);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(sampleBook);
    }

    @Test
    @DisplayName("Find book by id")
    void findById_validBookId_returnsBookDto() {
        //Given
        when(bookMapper.toDto(sampleBook)).thenReturn(sampleBookDto);
        when(bookRepository.findById(sampleBook.getId())).thenReturn(Optional.of(sampleBook));
        //When
        BookDto foundBookDto = bookService.findById(sampleBook.getId());
        //Then
        assertThat(foundBookDto).isEqualTo(sampleBookDto);
        verify(bookRepository, times(1)).findById(sampleBook.getId());
        verify(bookMapper, times(1)).toDto(sampleBook);
    }

    @Test
    @DisplayName("Verify findById method with invalid book id")
    void findById_invalidBookId_throwsEntityNotFoundException() {
        //Given
        when(bookRepository.findById(sampleBook.getId())).thenReturn(Optional.empty());
        String expected = String.format(ENTITY_NOT_FOUND_MESSAGE, sampleBook.getId());
        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(sampleBook.getId()));
        String actual = exception.getMessage();
        //Then
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(sampleBook.getId());
    }

    @Test
    @DisplayName("Update book by valid id")
    void updateBookById_validBookIdAndCreateBookRequestDto_returnsUpdatedBookDto() {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                UPDATED_BOOK_TITLE,
                UPDATED_BOOK_AUTHOR,
                UPDATED_BOOK_ISBN,
                UPDATED_BOOK_PRICE,
                UPDATED_BOOK_DESCRIPTION,
                UPDATED_BOOK_COVER_IMAGE,
                List.of(SECOND_CATEGORY_ID)
        );
        Category secondCategory = new Category(
                SECOND_CATEGORY_ID,
                SECOND_CATEGORY_NAME,
                SECOND_CATEGORY_DESCRIPTION
        );
        Book updatedBook = new Book(
                SAMPLE_BOOK_ID, requestDto.title(),
                requestDto.author(), requestDto.isbn(),
                requestDto.price(), requestDto.description(),
                requestDto.coverImage(), Set.of(secondCategory)
        );
        BookDto expected = new BookDto(
                updatedBook.getId(), updatedBook.getTitle(),
                updatedBook.getAuthor(), updatedBook.getIsbn(),
                updatedBook.getPrice(), updatedBook.getDescription(),
                updatedBook.getCoverImage(), List.of(secondCategory.getId()));

        when(bookRepository.findById(SAMPLE_BOOK_ID)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.save(sampleBook)).thenReturn(updatedBook);
        when(bookRepository.existsByIsbn(requestDto.isbn())).thenReturn(false);
        when(categoryService.getById(secondCategory.getId())).thenReturn(categoryDto);
        when(categoryMapper.toEntity(categoryDto)).thenReturn(secondCategory);
        doNothing().when(bookMapper).updateBookFromDto(requestDto, sampleBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expected);
        //When
        BookDto actual = bookService.updateBookById(SAMPLE_BOOK_ID, requestDto);
        //Then
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(SAMPLE_BOOK_ID);
        verify(bookRepository, times(1)).existsByIsbn(requestDto.isbn());
        verify(categoryService, times(1)).getById(secondCategory.getId());
        verify(categoryMapper, times(1)).toEntity(categoryDto);
        verify(bookMapper, times(1)).updateBookFromDto(requestDto, sampleBook);
        verify(bookMapper, times(1)).toDto(updatedBook);
    }

    @Test
    @DisplayName("Verify updateBookById method when isbn already exists")
    void updateBookById_duplicatedIsbn_throwsDuplicatedSourceException() {
        //Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                UPDATED_BOOK_TITLE,
                UPDATED_BOOK_AUTHOR,
                UPDATED_BOOK_ISBN,
                UPDATED_BOOK_PRICE,
                UPDATED_BOOK_DESCRIPTION,
                UPDATED_BOOK_COVER_IMAGE,
                List.of(SECOND_CATEGORY_ID)
        );
        String expected = String.format(DUPLICATE_ISBN_MESSAGE, requestDto.isbn());

        when(bookRepository.findById(SAMPLE_BOOK_ID)).thenReturn(Optional.of(sampleBook));
        when(bookRepository.existsByIsbn(requestDto.isbn())).thenReturn(true);
        //When
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> bookService.updateBookById(SAMPLE_BOOK_ID, requestDto));
        String actual = exception.getMessage();
        //Then
        assertThat(actual).isEqualTo(expected);
        verify(bookRepository, times(1)).findById(SAMPLE_BOOK_ID);
        verify(bookRepository, times(1)).existsByIsbn(requestDto.isbn());
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
        String[] titles = new String[]{SAMPLE_BOOK_TITLE};
        String[] prices = new String[]{"20.50"};
        BookSearchParameters searchParameters = new BookSearchParameters(
                titles, null, prices
        );
        Pageable pageable = PageRequest.of(SEARCH_PAGE_NUMBER, SEARCH_PAGE_SIZE);
        List<Book> expected = List.of(sampleBook);
        Page<Book> bookPage = new PageImpl<>(expected, pageable, expected.size());

        Specification<Book> specification = bookSpecificationBuilder.build(searchParameters);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(sampleBook)).thenReturn(sampleBookDto);
        //When
        List<BookDto> actual = bookService.search(searchParameters, pageable);
        //Then
        assertThat(actual).hasSize(1);
        assertThat(actual.get(FIRST_RECORD)).isEqualTo(sampleBookDto);

        verify(bookSpecificationBuilder, atLeastOnce()).build(searchParameters);
        verify(bookRepository, times(1)).findAll(specification, pageable);
        verify(bookMapper, times(1)).toDto(sampleBook);
        verifyNoMoreInteractions(bookSpecificationBuilder, bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Find books in certain category id")
    void getBooksByCategoryId_validCategoryIdAndPageable_returnsAllFromCategory() {
        //Given
        Long categoryId = firstCategory.getId();
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(sampleBook);

        when(categoryService.getById(categoryId)).thenReturn(categoryDto);
        when(bookMapper.toDtoWithoutCategories(sampleBook)).thenReturn(bookDtoWithoutCategoryIds);
        when(bookRepository.findAllByCategories_Id(categoryId, pageable)).thenReturn(books);
        //When
        List<BookDtoWithoutCategoryIds> bookDtos =
                bookService.getBooksByCategoryId(categoryId, pageable);
        //Then
        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDtoWithoutCategoryIds);

        verify(bookRepository, times(1))
                .findAllByCategories_Id(categoryId, pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategories(sampleBook);
        verify(categoryService, times(1)).getById(categoryId);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryService);
    }

    @Test
    @DisplayName("Verify getBooksByCategoryId method with invalid category id")
    void getBooksByCategoryId_invalidCategory_throwsEntityNotFoundException() {
        //Given
        Long categoryId = firstCategory.getId();
        Pageable pageable = PageRequest.of(0, 10);
        String expected = "Can't find category by id: " + categoryId;

        when(categoryService.getById(categoryId)).thenReturn(null);
        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBooksByCategoryId(categoryId, pageable));
        String actual = exception.getMessage();
        //Then
        assertThat(actual).isEqualTo(expected);

        verify(categoryService, times(1)).getById(categoryId);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryService);
    }

    private CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto(
                SAMPLE_BOOK_TITLE, SAMPLE_BOOK_AUTHOR, SAMPLE_BOOK_ISBN,
                SAMPLE_BOOK_PRICE, SAMPLE_BOOK_DESCRIPTION,
                SAMPLE_BOOK_COVER_IMAGE, SAMPLE_CATEGORY_IDS
        );
    }
}
