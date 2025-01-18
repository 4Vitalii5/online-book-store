package mate.academy.util;

import static mate.academy.util.TestConstants.BOOK_ID;
import static mate.academy.util.TestConstants.CART_ITEM_ID;
import static mate.academy.util.TestConstants.CATEGORY_DESCRIPTION;
import static mate.academy.util.TestConstants.CATEGORY_ID;
import static mate.academy.util.TestConstants.CATEGORY_NAME;
import static mate.academy.util.TestConstants.DEFAULT_ROLE;
import static mate.academy.util.TestConstants.ENCODED_PASSWORD;
import static mate.academy.util.TestConstants.ITEM_ID;
import static mate.academy.util.TestConstants.ITEM_PRICE;
import static mate.academy.util.TestConstants.ITEM_QUANTITY;
import static mate.academy.util.TestConstants.NEW_BOOK_AUTHOR;
import static mate.academy.util.TestConstants.NEW_BOOK_CATEGORY_IDS;
import static mate.academy.util.TestConstants.NEW_BOOK_COVER_IMAGE;
import static mate.academy.util.TestConstants.NEW_BOOK_DESCRIPTION;
import static mate.academy.util.TestConstants.NEW_BOOK_ISBN;
import static mate.academy.util.TestConstants.NEW_BOOK_PRICE;
import static mate.academy.util.TestConstants.NEW_BOOK_TITLE;
import static mate.academy.util.TestConstants.NEW_CATEGORY_DESCRIPTION;
import static mate.academy.util.TestConstants.NEW_CATEGORY_NAME;
import static mate.academy.util.TestConstants.NON_EXISTING_ID;
import static mate.academy.util.TestConstants.ORDER_DATE;
import static mate.academy.util.TestConstants.ORDER_ID;
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
import static mate.academy.util.TestConstants.SECOND_BOOK_ID;
import static mate.academy.util.TestConstants.SECOND_CATEGORY_DESCRIPTION;
import static mate.academy.util.TestConstants.SECOND_CATEGORY_ID;
import static mate.academy.util.TestConstants.SECOND_CATEGORY_NAME;
import static mate.academy.util.TestConstants.SECOND_ITEM_ID;
import static mate.academy.util.TestConstants.SECOND_ITEM_QUANTITY;
import static mate.academy.util.TestConstants.SHIPPING_ADDRESS;
import static mate.academy.util.TestConstants.TOTAL_PRICE;
import static mate.academy.util.TestConstants.UPDATED_BOOK_AUTHOR;
import static mate.academy.util.TestConstants.UPDATED_BOOK_CATEGORY_IDS;
import static mate.academy.util.TestConstants.UPDATED_BOOK_COVER_IMAGE;
import static mate.academy.util.TestConstants.UPDATED_BOOK_DESCRIPTION;
import static mate.academy.util.TestConstants.UPDATED_BOOK_ISBN;
import static mate.academy.util.TestConstants.UPDATED_BOOK_PRICE;
import static mate.academy.util.TestConstants.UPDATED_BOOK_TITLE;
import static mate.academy.util.TestConstants.USER_EMAIL;
import static mate.academy.util.TestConstants.USER_FIRST_NAME;
import static mate.academy.util.TestConstants.USER_ID;
import static mate.academy.util.TestConstants.USER_LAST_NAME;
import static mate.academy.util.TestConstants.USER_PASSWORD;
import static mate.academy.util.TestConstants.USER_SHIPPING_ADDRESS;
import static mate.academy.util.TestConstants.VALID_USER_EMAIL;

import java.util.List;
import java.util.Set;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.book.BookSearchParameters;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CreateCategoryRequestDto;
import mate.academy.dto.item.CartItemResponseDto;
import mate.academy.dto.item.CreateCartItemRequestDto;
import mate.academy.dto.item.UpdateCartItemDto;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderRequestDto;
import mate.academy.dto.user.UserLoginRequestDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.Category;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.Role;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
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
    public static final Book BOOK = new Book();

    static {
        BOOK.setId(BOOK_ID);
        BOOK.setPrice(ITEM_PRICE);
    }

    public static final Book SECOND_BOOK = new Book();

    static {
        SECOND_BOOK.setId(SECOND_BOOK_ID);
        SECOND_BOOK.setPrice(ITEM_PRICE);
    }

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

    //Roles
    public static final Role ROLE = new Role();

    static {
        ROLE.setId(1L);
        ROLE.setRole(DEFAULT_ROLE);
    }

    //User
    public static final UserRegistrationRequestDto USER_REGISTRATION_REQUEST_DTO =
            new UserRegistrationRequestDto(
                    USER_EMAIL,
                    USER_PASSWORD,
                    USER_PASSWORD,
                    USER_FIRST_NAME,
                    USER_LAST_NAME,
                    USER_SHIPPING_ADDRESS
            );
    public static final User USER = new User();

    static {
        USER.setId(1L);
        USER.setEmail(USER_EMAIL);
        USER.setPassword(ENCODED_PASSWORD);
        USER.setFirstName(USER_FIRST_NAME);
        USER.setLastName(USER_LAST_NAME);
        USER.setShippingAddress(USER_SHIPPING_ADDRESS);
        USER.setRoles(Set.of(ROLE));
    }

    public static final User NEW_USER = new User();

    static {
        NEW_USER.setEmail(USER_EMAIL);
        NEW_USER.setPassword(USER_PASSWORD);
        NEW_USER.setFirstName(USER_FIRST_NAME);
        NEW_USER.setLastName(USER_LAST_NAME);
        NEW_USER.setShippingAddress(USER_SHIPPING_ADDRESS);
    }

    public static final UserResponseDto USER_RESPONSE_DTO = new UserResponseDto(
            1L,
            USER_EMAIL,
            USER_FIRST_NAME,
            USER_LAST_NAME,
            USER_SHIPPING_ADDRESS
    );

    //Order
    public static final CreateOrderRequestDto CREATE_ORDER_REQUEST_DTO = new CreateOrderRequestDto(
            SHIPPING_ADDRESS
    );

    public static final UpdateOrderRequestDto UPDATE_ORDER_REQUEST_DTO = new UpdateOrderRequestDto(
            Order.Status.COMPLETED.name()
    );

    public static final OrderItem ORDER_ITEM = new OrderItem();

    static {
        ORDER_ITEM.setId(ITEM_ID);
        ORDER_ITEM.setQuantity(ITEM_QUANTITY);
        ORDER_ITEM.setBook(BOOK);
        ORDER_ITEM.setPrice(ITEM_PRICE);
    }

    //Shopping cart
    public static final ShoppingCart SHOPPING_CART = new ShoppingCart();

    static {
        SHOPPING_CART.setUser(USER);
    }

    public static final CartItem CART_ITEM = new CartItem();

    static {
        CART_ITEM.setId(ITEM_ID);
        CART_ITEM.setShoppingCart(SHOPPING_CART);
        CART_ITEM.setBook(BOOK);
        CART_ITEM.setQuantity(ITEM_QUANTITY);
    }

    public static final CartItem SECOND_CART_ITEM = new CartItem();

    static {
        SECOND_CART_ITEM.setId(SECOND_ITEM_ID);
        SECOND_CART_ITEM.setShoppingCart(SHOPPING_CART);
        SECOND_CART_ITEM.setBook(SECOND_BOOK);
        SECOND_CART_ITEM.setQuantity(SECOND_ITEM_QUANTITY);
    }

    static {
        Set<CartItem> cartItems = Set.of(CART_ITEM, SECOND_CART_ITEM);
        SHOPPING_CART.setCartItems(cartItems);
    }

    public static final CreateCartItemRequestDto CREATE_CART_ITEM_REQUEST_DTO =
            new CreateCartItemRequestDto(BOOK_ID, 2);

    public static final CreateCartItemRequestDto INVALID_CREATE_CART_ITEM_REQUEST_DTO =
            new CreateCartItemRequestDto(NON_EXISTING_ID, -1);

    public static final UpdateCartItemDto INVALID_UPDATE_CART_ITEM_DTO = new UpdateCartItemDto(-1);

    public static final UpdateCartItemDto UPDATE_CART_ITEM_DTO = new UpdateCartItemDto(
            5
    );

    public static final CartItemResponseDto CART_ITEM_RESPONSE_DTO = new CartItemResponseDto(
            CART_ITEM_ID,
            CART_ITEM.getBook().getId(),
            CART_ITEM.getBook().getTitle(),
            UPDATE_CART_ITEM_DTO.quantity()
    );

    public static final ShoppingCartDto SHOPPING_CART_DTO = new ShoppingCartDto(
            SHOPPING_CART.getId(),
            USER_ID,
            List.of(CART_ITEM_RESPONSE_DTO)
    );

    //AuthenticationController
    public static final UserLoginRequestDto USER_LOGIN_REQUEST_DTO =
            new UserLoginRequestDto(VALID_USER_EMAIL, USER_PASSWORD);

    //Order
    public static final Order ORDER = new Order();

    static {
        ORDER.setId(ORDER_ID);
        ORDER.setUser(USER);
        ORDER.setOrderItems(Set.of(ORDER_ITEM));
        ORDER.setTotal(TOTAL_PRICE);
        ORDER.setOrderDate(ORDER_DATE);
        ORDER.setShippingAddress(SHIPPING_ADDRESS);
        ORDER.setStatus(Order.Status.NEW);
    }

    public static final OrderItemDto ORDER_ITEM_DTO = new OrderItemDto(
            ITEM_ID,
            BOOK_ID,
            ITEM_QUANTITY
    );

    public static final OrderDto ORDER_DTO = new OrderDto(
            ORDER_ID,
            USER_ID,
            List.of(ORDER_ITEM_DTO),
            ORDER_DATE,
            TOTAL_PRICE,
            Order.Status.NEW.name()
    );

    private TestUtil() {
    }
}
