package mate.academy.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.dto.item.CreateCartItemRequestDto;
import mate.academy.dto.item.UpdateCartItemDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.BookMapper;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.cart.ShoppingCartRepository;
import mate.academy.repository.item.CartItemRepository;
import mate.academy.service.ShoppingCartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto getShoppingCart() {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(getCurrentUserId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addBookToShoppingCart(CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(getCurrentUserId());
        addOrUpdateCartItem(requestDto, shoppingCart);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateShoppingCart(Long cartItemId,
                                              UpdateCartItemDto updateCartItemDto) {
        ShoppingCart shoppingCart = getShoppingCartByCurrentUser();
        CartItem existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item with id:" + cartItemId));
        cartItemMapper.updateCartItemFromDto(updateCartItemDto, existingCartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartByCurrentUser();
        shoppingCart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        shoppingCartRepository.save(shoppingCart);
    }

    private Long getCurrentUserId() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return ((User) principal).getId();
    }

    private ShoppingCart getShoppingCartByCurrentUser() {
        return shoppingCartRepository.findByUserId(getCurrentUserId());
    }

    private void addOrUpdateCartItem(CreateCartItemRequestDto requestDto,
                                     ShoppingCart shoppingCart) {
        Book book = bookRepository.findById(requestDto.bookId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find book by id: " + requestDto.bookId())
        );
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();
        if (existingCartItem.isPresent()) {
            int updatedQuantity = existingCartItem.get().getQuantity() + requestDto.quantity();
            existingCartItem.get().setQuantity(updatedQuantity);
        } else {
            CartItem newCartItem = cartItemMapper.toEntity(requestDto);
            newCartItem.setBook(book);
            newCartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(newCartItem);
            cartItemRepository.save(newCartItem);
        }
    }
}
