package mate.academy.service;

import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.dto.item.CreateCartItemRequestDto;
import mate.academy.dto.item.UpdateCartItemDto;
import mate.academy.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartDto getShoppingCart();

    ShoppingCartDto addBookToShoppingCart(CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateShoppingCart(Long cartItemId, UpdateCartItemDto updateCartItemDto);

    void removeCartItem(Long cartItemId);
}
