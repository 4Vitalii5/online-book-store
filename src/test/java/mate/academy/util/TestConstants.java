package mate.academy.util;

import static mate.academy.model.Role.RoleName.ROLE_SUPER_ADMIN;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import mate.academy.model.Role;

public class TestConstants {
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
    public static final String UPDATED_BOOK_COVER_IMAGE =
            "https://shop.lonelyplanet.com9781838690434.jpg?v=1666176637&width=1500";
    public static final List<Long> UPDATED_BOOK_CATEGORY_IDS = List.of(1L);

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

    // Exception messages for book tests
    public static final String DUPLICATE_ISBN_MESSAGE = "Book with ISBN: %s already exists";
    public static final String BOOK_NOT_FOUND_MESSAGE = "Can't find book by id: %d";

    // Sample book
    public static final Long SAMPLE_BOOK_ID = 1L;
    public static final String SAMPLE_BOOK_TITLE = "Harry Potter";
    public static final String SAMPLE_BOOK_AUTHOR = "J.K.Rowling";
    public static final String SAMPLE_BOOK_ISBN = "9780596520681";
    public static final BigDecimal SAMPLE_BOOK_PRICE = BigDecimal.valueOf(20.50);
    public static final String SAMPLE_BOOK_DESCRIPTION = "Sample description";
    public static final String SAMPLE_BOOK_COVER_IMAGE = "http://example.com/cover.jpg";

    // Data for category creation
    public static final String CATEGORY_NAME = "Sci-Fi";
    public static final String CATEGORY_DESCRIPTION = "Books about science fiction";

    // Data for category update
    public static final Long CATEGORY_ID = 1L;
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String NEW_CATEGORY_NAME = "Horror";
    public static final String NEW_CATEGORY_DESCRIPTION = "Books of horror genre";

    // Exception messages for category tests
    public static final String DUPLICATE_NAME_MESSAGE = "Category with name: %s already exists";
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Can't find category by id: %d";
    public static final String INVALID_CATEGORY_NAME = "Darkness";

    // Data for second category
    public static final long SECOND_CATEGORY_ID = 3L;
    public static final String SECOND_CATEGORY_NAME = "Non-fiction";
    public static final String SECOND_CATEGORY_DESCRIPTION = "Documentary books";

    //User
    public static final Long USER_ID = 1L;
    public static final Long INVALID_USER_ID = 23L;
    public static final String USER_EMAIL = "sashko45@example.com";
    public static final String VALID_USER_EMAIL = "testuser@example.com";
    public static final String NON_EXISTING_USER_EMAIL = "maks43@ukr.com";
    public static final String USER_PASSWORD = "password123";
    public static final String USER_FIRST_NAME = "John";
    public static final String USER_LAST_NAME = "Doe";
    public static final String USER_SHIPPING_ADDRESS = "123 Main St, Springfield";
    public static final String ENCODED_PASSWORD = "$2a$10$7d7q7q6/8Vh2Q4H1OpXJNe";
    public static final Role.RoleName DEFAULT_ROLE = Role.RoleName.ROLE_USER;
    public static final Role.RoleName INVALID_ROLE = ROLE_SUPER_ADMIN;
    public static final String DUPLICATE_EMAIL_MESSAGE = "User with email: sashko45@example.com "
            + "already exists";
    public static final String ROLE_NOT_FOUND_MESSAGE = "Role: ROLE_USER not found";

    //Order
    public static final Long ORDER_ID = 1L;
    public static final Long ITEM_ID = 1L;
    public static final Long SECOND_ITEM_ID = 2L;
    public static final Long BOOK_ID = 1L;
    public static final Long SECOND_BOOK_ID = 1L;
    public static final String SHIPPING_ADDRESS = "123 Main St, Springfield";
    public static final String NEW_ORDER_STATUS = "NEW";
    public static final BigDecimal ITEM_PRICE = BigDecimal.valueOf(10.00);
    public static final int ITEM_QUANTITY = 2;
    public static final int SECOND_ITEM_QUANTITY = 10;
    public static final BigDecimal TOTAL_PRICE = ITEM_PRICE.multiply(
            BigDecimal.valueOf(ITEM_QUANTITY));
    public static final LocalDateTime ORDER_DATE = LocalDateTime.now();
    public static final String ORDER_NOT_FOUND_MESSAGE = "Can't find order with id:" + ORDER_ID;
    public static final String ORDER_PROCESSING_EXCEPTION_MESSAGE = "Can't create order. "
            + "CartItems is empty";

    //Shopping Cart
    public static final Long SHOPPING_CART_ID = 1L;
    public static final Long CART_ITEM_ID = 1L;
    public static final String CART_NOT_FOUND_MESSAGE = "Can't find cart item with id:"
            + CART_ITEM_ID;

    //Invalid data
    public static final Long INVALID_ORDER_ID = 99L;
    public static final Long NON_EXISTING_ID = 999L;
    public static final Long NON_EXISTING_ORDER_ID = 999L;

    private TestConstants() {
    }
}
