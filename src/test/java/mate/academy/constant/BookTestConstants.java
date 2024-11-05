package mate.academy.constant;

import static mate.academy.constant.CategoryTestConstants.CATEGORY_ID;

import java.math.BigDecimal;
import java.util.List;

public class BookTestConstants {
    // Data for book creation
    public static final String NEW_BOOK_TITLE = "New Book";
    public static final String NEW_BOOK_AUTHOR = "Jane Doe";
    public static final String NEW_BOOK_ISBN = "978-3-16-148410-0";
    public static final BigDecimal NEW_BOOK_PRICE = BigDecimal.valueOf(19.99);
    public static final String NEW_BOOK_DESCRIPTION = "A fascinating book";
    public static final String NEW_BOOK_COVER_IMAGE = "coverImage.jpg";
    public static final List<Long> NEW_BOOK_CATEGORY_IDS = List.of(1L, 2L);
    // Data for book update
    public static final String UPDATED_BOOK_TITLE = "The Perfect Shot";
    public static final String UPDATED_BOOK_AUTHOR = "Planet, Lonely";
    public static final String UPDATED_BOOK_ISBN = "9781838690434";
    public static final BigDecimal UPDATED_BOOK_PRICE = BigDecimal.valueOf(30.99);
    public static final String UPDATED_BOOK_DESCRIPTION = "Book description #2";
    public static final String UPDATED_BOOK_COVER_IMAGE = "https://shop.lonelyplanet.com9781838690434.jpg?v=1666176637&width=1500";
    public static final List<Long> UPDATED_BOOK_CATEGORY_IDS = List.of(2L);
    // Data for book search
    public static final String SEARCH_TITLE = "Another Great Book";
    public static final String SEARCH_AUTHOR = "Jane Doe";
    public static final String SEARCH_PRICE_MIN = "10.00";
    public static final String SEARCH_PRICE_MAX = "50.00";
    // Parameter name
    public static final String SEARCH_PARAM_TITLES = "titles";
    public static final String SEARCH_PARAM_AUTHORS = "authors";
    public static final String SEARCH_PARAM_PRICES = "prices";
    public static final String SEARCH_FIELD_AUTHOR = "author";
    public static final int SEARCH_PAGE_NUMBER = 0;
    public static final int SEARCH_PAGE_SIZE = 10;
    public static final int FIRST_RECORD = 0;
    // Valid data
    public static final Long VALID_FIRST_BOOK_ID = 1L;
    public static final Long VALID_SECOND_BOOK_ID = 2L;
    // Exception messages
    public static final String DUPLICATE_ISBN_MESSAGE = "Book with ISBN: %s already exists";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "Can't find book by id: %d";
    // Sample book
    public static final Long SAMPLE_BOOK_ID = 1L;
    public static final String SAMPLE_BOOK_TITLE = "Harry Potter";
    public static final String SAMPLE_BOOK_AUTHOR = "J.K.Rowling";
    public static final String SAMPLE_BOOK_ISBN = "9780596520681";
    public static final BigDecimal SAMPLE_BOOK_PRICE = BigDecimal.valueOf(20.50);
    public static final String SAMPLE_BOOK_DESCRIPTION = "Sample description";
    public static final String SAMPLE_BOOK_COVER_IMAGE = "http://example.com/cover.jpg";
    public static final List<Long> SAMPLE_CATEGORY_IDS = List.of(CATEGORY_ID);

    private BookTestConstants() {
    }
}
