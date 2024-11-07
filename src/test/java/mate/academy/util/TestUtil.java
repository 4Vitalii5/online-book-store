package mate.academy.util;

import static mate.academy.util.TestConstants.CATEGORY_DESCRIPTION;
import static mate.academy.util.TestConstants.CATEGORY_ID;
import static mate.academy.util.TestConstants.CATEGORY_NAME;
import static mate.academy.util.TestConstants.NEW_BOOK_AUTHOR;
import static mate.academy.util.TestConstants.NEW_BOOK_CATEGORY_IDS;
import static mate.academy.util.TestConstants.NEW_BOOK_COVER_IMAGE;
import static mate.academy.util.TestConstants.NEW_BOOK_DESCRIPTION;
import static mate.academy.util.TestConstants.NEW_BOOK_ISBN;
import static mate.academy.util.TestConstants.NEW_BOOK_PRICE;
import static mate.academy.util.TestConstants.NEW_BOOK_TITLE;
import static mate.academy.util.TestConstants.NEW_CATEGORY_DESCRIPTION;
import static mate.academy.util.TestConstants.NEW_CATEGORY_NAME;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_AUTHOR;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_COVER_IMAGE;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_DESCRIPTION;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_ID;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_ISBN;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_PRICE;
import static mate.academy.util.TestConstants.SAMPLE_BOOK_TITLE;
import static mate.academy.util.TestConstants.SEARCH_AUTHOR;
import static mate.academy.util.TestConstants.SEARCH_FIELD_AUTHOR;
import static mate.academy.util.TestConstants.SEARCH_PAGE_NUMBER;
import static mate.academy.util.TestConstants.SEARCH_PAGE_SIZE;
import static mate.academy.util.TestConstants.SEARCH_PRICE_MAX;
import static mate.academy.util.TestConstants.SEARCH_PRICE_MIN;
import static mate.academy.util.TestConstants.SEARCH_TITLE;
import static mate.academy.util.TestConstants.SECOND_CATEGORY_DESCRIPTION;
import static mate.academy.util.TestConstants.SECOND_CATEGORY_ID;
import static mate.academy.util.TestConstants.SECOND_CATEGORY_NAME;
import static mate.academy.util.TestConstants.UPDATED_BOOK_AUTHOR;
import static mate.academy.util.TestConstants.UPDATED_BOOK_CATEGORY_IDS;
import static mate.academy.util.TestConstants.UPDATED_BOOK_COVER_IMAGE;
import static mate.academy.util.TestConstants.UPDATED_BOOK_DESCRIPTION;
import static mate.academy.util.TestConstants.UPDATED_BOOK_ISBN;
import static mate.academy.util.TestConstants.UPDATED_BOOK_PRICE;
import static mate.academy.util.TestConstants.UPDATED_BOOK_TITLE;

import java.util.List;
import java.util.Set;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.BookSearchParameters;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.model.Book;
import mate.academy.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class TestUtil {
    //Category
    public static final Category FIRST_CATEGORY = new Category(
            CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION, false
    );
    public static final Category SECOND_CATEGORY = new Category(
            SECOND_CATEGORY_ID, SECOND_CATEGORY_NAME, SECOND_CATEGORY_DESCRIPTION, false
    );
    //Book
    public static final Book SAMPLE_BOOK = new Book(
            SAMPLE_BOOK_ID, SAMPLE_BOOK_TITLE, SAMPLE_BOOK_AUTHOR,
            SAMPLE_BOOK_ISBN, SAMPLE_BOOK_PRICE, SAMPLE_BOOK_DESCRIPTION,
            SAMPLE_BOOK_COVER_IMAGE, false, Set.of(FIRST_CATEGORY)
    );
    public static final Book NEW_BOOK = new Book(
            null, NEW_BOOK_TITLE, NEW_BOOK_AUTHOR,
            NEW_BOOK_ISBN, NEW_BOOK_PRICE, NEW_BOOK_DESCRIPTION,
            NEW_BOOK_COVER_IMAGE, false, null
    );
    public static final Book UPDATED_BOOK = new Book(
            SAMPLE_BOOK_ID, UPDATED_BOOK_TITLE, UPDATED_BOOK_AUTHOR,
            UPDATED_BOOK_ISBN, UPDATED_BOOK_PRICE, UPDATED_BOOK_DESCRIPTION,
            UPDATED_BOOK_COVER_IMAGE, false, Set.of(SECOND_CATEGORY)
    );
    //BookDto
    public static final BookDto SAMPLE_BOOK_DTO = new BookDto(
            SAMPLE_BOOK.getId(), SAMPLE_BOOK.getTitle(), SAMPLE_BOOK.getAuthor(),
            SAMPLE_BOOK.getIsbn(), SAMPLE_BOOK.getPrice(), SAMPLE_BOOK.getDescription(),
            SAMPLE_BOOK.getCoverImage(), List.of(FIRST_CATEGORY.getId())
    );
    public static final BookDto UPDATED_BOOK_DTO = new BookDto(
            SAMPLE_BOOK_ID, UPDATED_BOOK_TITLE, UPDATED_BOOK_AUTHOR,
            UPDATED_BOOK_ISBN, UPDATED_BOOK_PRICE, UPDATED_BOOK_DESCRIPTION,
            UPDATED_BOOK_COVER_IMAGE, List.of(SECOND_CATEGORY.getId())
    );
    public static final BookDtoWithoutCategoryIds BOOK_DTO_WITHOUT_CATEGORY_IDS =
            new BookDtoWithoutCategoryIds(
                    SAMPLE_BOOK.getId(), SAMPLE_BOOK.getTitle(), SAMPLE_BOOK.getAuthor(),
                    SAMPLE_BOOK.getIsbn(), SAMPLE_BOOK.getPrice(), SAMPLE_BOOK.getDescription(),
                    SAMPLE_BOOK.getCoverImage()
            );
    //CategoryDto
    public static final CategoryDto CATEGORY_DTO = new CategoryDto(
            FIRST_CATEGORY.getId(), FIRST_CATEGORY.getName(), FIRST_CATEGORY.getDescription()
    );
    public static final CategoryDto UPDATED_CATEGORY_DTO = new CategoryDto(
            SECOND_CATEGORY.getId(), SECOND_CATEGORY.getName(), SECOND_CATEGORY.getDescription()
    );
    //Pageable
    public static final Pageable PAGEABLE = PageRequest.of(SEARCH_PAGE_NUMBER, SEARCH_PAGE_SIZE);
    public static final Page<Book> BOOK_PAGE = new PageImpl<>(
            List.of(SAMPLE_BOOK), PAGEABLE, 1
    );
    public static final Page<Category> CATEGORY_PAGE = new PageImpl<>(
            List.of(FIRST_CATEGORY), PAGEABLE, 1
    );
    //Book request dto
    public static final CreateBookRequestDto CREATE_BOOK_REQUEST_DTO = new CreateBookRequestDto(
            NEW_BOOK_TITLE, NEW_BOOK_AUTHOR, NEW_BOOK_ISBN, NEW_BOOK_PRICE,
            NEW_BOOK_DESCRIPTION, NEW_BOOK_COVER_IMAGE, NEW_BOOK_CATEGORY_IDS
    );
    public static final CreateBookRequestDto UPDATE_BOOK_REQUEST_DTO = new CreateBookRequestDto(
            UPDATED_BOOK_TITLE, UPDATED_BOOK_AUTHOR, UPDATED_BOOK_ISBN, UPDATED_BOOK_PRICE,
            UPDATED_BOOK_DESCRIPTION, UPDATED_BOOK_COVER_IMAGE, UPDATED_BOOK_CATEGORY_IDS
    );
    //Category request dto
    public static final CreateCategoryRequestDto CREATE_CATEGORY_REQUEST_DTO =
            new CreateCategoryRequestDto(NEW_CATEGORY_NAME, NEW_CATEGORY_DESCRIPTION);

    public static final CreateCategoryRequestDto UPDATE_CATEGORY_REQUEST_DTO =
            new CreateCategoryRequestDto(SECOND_CATEGORY_NAME, SECOND_CATEGORY_DESCRIPTION);

    public static final BookSearchParameters SEARCH_PARAMETERS = new BookSearchParameters(
            new String[]{SEARCH_TITLE}, new String[]{SEARCH_AUTHOR},
            new String[]{SEARCH_PRICE_MIN, SEARCH_PRICE_MAX}
    );
    //Specification
    public static final Specification<Book> SPECIFICATION = (root, query, cb) ->
            cb.equal(root.get(SEARCH_FIELD_AUTHOR), SEARCH_AUTHOR);

    private TestUtil() {
    }
}
