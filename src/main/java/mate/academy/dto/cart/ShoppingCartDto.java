package mate.academy.dto.cart;

import java.util.List;
import mate.academy.dto.item.CartItemResponseDto;

public record ShoppingCartDto(
        Long id,
        Long userId,
        List<CartItemResponseDto> cartItems
) {
}
