package mate.academy.service.impl;

import static mate.academy.util.TestConstants.BOOK_ID;
import static mate.academy.util.TestConstants.BOOK_NOT_FOUND_MESSAGE;
import static mate.academy.util.TestConstants.CART_NOT_FOUND_MESSAGE;
import static mate.academy.util.TestConstants.ITEM_ID;
import static mate.academy.util.TestConstants.USER_ID;
import static mate.academy.util.TestUtil.BOOK;
import static mate.academy.util.TestUtil.CART_ITEM;
import static mate.academy.util.TestUtil.CREATE_CART_ITEM_REQUEST_DTO;
import static mate.academy.util.TestUtil.NEW_USER;
import static mate.academy.util.TestUtil.SECOND_CART_ITEM;
import static mate.academy.util.TestUtil.SHOPPING_CART;
import static mate.academy.util.TestUtil.SHOPPING_CART_DTO;
import static mate.academy.util.TestUtil.UPDATE_CART_ITEM_DTO;
import static mate.academy.util.TestUtil.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.dto.item.CartItemResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.cart.ShoppingCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ShoppingCartServiceImplTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(CART_ITEM);
        cartItems.add(SECOND_CART_ITEM);
        SHOPPING_CART.setCartItems(cartItems);
    }

    @Test
    @DisplayName("Create shopping cart")
    void createShoppingCart_validUser_createsShoppingCart() {
        // When
        shoppingCartService.createShoppingCart(NEW_USER);

        // Then
        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Get shopping cart")
    void getShoppingCart_validUser_returnsShoppingCart() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER);
        SecurityContextHolder.setContext(securityContext);
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(SHOPPING_CART);
        when(shoppingCartMapper.toDto(SHOPPING_CART)).thenReturn(SHOPPING_CART_DTO);

        // When
        ShoppingCartDto actualShoppingCartDto = shoppingCartService.getShoppingCart();

        // Then
        assertThat(actualShoppingCartDto).isEqualTo(SHOPPING_CART_DTO);
        verify(shoppingCartRepository, times(1)).findByUserId(USER_ID);
        verify(shoppingCartMapper, times(1)).toDto(SHOPPING_CART);
    }

    @Test
    @DisplayName("Add book to shopping cart")
    void addBookToShoppingCart_validBook_addsBookToCart() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER);
        SecurityContextHolder.setContext(securityContext);
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(SHOPPING_CART);
        when(bookRepository.findById(CREATE_CART_ITEM_REQUEST_DTO.bookId()))
                .thenReturn(Optional.of(BOOK));
        when(shoppingCartMapper.toDto(SHOPPING_CART)).thenReturn(SHOPPING_CART_DTO);

        // When
        ShoppingCartDto actualShoppingCartDto = shoppingCartService.addBookToShoppingCart(
                CREATE_CART_ITEM_REQUEST_DTO);

        // Then
        assertThat(actualShoppingCartDto).isEqualTo(SHOPPING_CART_DTO);
        verify(shoppingCartRepository, times(1)).findByUserId(USER_ID);
        verify(shoppingCartRepository, times(1)).save(SHOPPING_CART);
        verify(shoppingCartMapper, times(1)).toDto(SHOPPING_CART);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException if book not found")
    void addBookToShoppingCart_bookNotFound_throwsEntityNotFoundException() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER);
        SecurityContextHolder.setContext(securityContext);
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.empty());
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(SHOPPING_CART);

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addBookToShoppingCart(CREATE_CART_ITEM_REQUEST_DTO));

        // Then
        assertThat(exception.getMessage()).isEqualTo(
                String.format(BOOK_NOT_FOUND_MESSAGE, BOOK_ID));
        verify(bookRepository, times(1)).findById(BOOK_ID);
    }

    @Test
    @DisplayName("Update shopping cart item quantity")
    void updateShoppingCart_validItem_updatesQuantity() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER);
        SecurityContextHolder.setContext(securityContext);
        when(shoppingCartMapper.toDto(SHOPPING_CART)).thenReturn(SHOPPING_CART_DTO);
        doNothing().when(cartItemMapper).updateCartItemFromDto(UPDATE_CART_ITEM_DTO, CART_ITEM);
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(SHOPPING_CART);

        // When
        ShoppingCartDto actualShoppingCartDto = shoppingCartService.updateShoppingCart(ITEM_ID,
                UPDATE_CART_ITEM_DTO);
        int actual = actualShoppingCartDto.cartItems().stream()
                .filter(cart -> cart.id().equals(ITEM_ID))
                .map(CartItemResponseDto::quantity)
                .findFirst().get();

        // Then
        assertThat(actual).isEqualTo(UPDATE_CART_ITEM_DTO.quantity());
        verify(cartItemMapper, times(1)).updateCartItemFromDto(UPDATE_CART_ITEM_DTO, CART_ITEM);
        verify(shoppingCartMapper, times(1)).toDto(SHOPPING_CART);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException if cart item not found")
    void updateShoppingCart_itemNotFound_throwsEntityNotFoundException() {
        ShoppingCart emptyCart = new ShoppingCart();
        emptyCart.setUser(USER);
        emptyCart.setCartItems(Set.of());
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(emptyCart);

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateShoppingCart(ITEM_ID, UPDATE_CART_ITEM_DTO));

        // Then
        assertThat(exception.getMessage()).isEqualTo(
                String.format(CART_NOT_FOUND_MESSAGE, ITEM_ID));
        verify(shoppingCartRepository, times(1)).findByUserId(USER_ID);
    }

    @Test
    @DisplayName("Remove cart item")
    void removeCartItem_validItem_removesCartItem() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(USER);
        SecurityContextHolder.setContext(securityContext);
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(SHOPPING_CART);

        // When
        shoppingCartService.removeCartItem(SECOND_CART_ITEM.getId());

        // Then
        assertThat(SHOPPING_CART.getCartItems()).doesNotContainSequence(SECOND_CART_ITEM);
        verify(shoppingCartRepository, times(1)).findByUserId(USER_ID);
        verify(shoppingCartRepository, times(1)).save(SHOPPING_CART);
    }
}
