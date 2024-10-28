package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.dto.item.CreateCartItemRequestDto;
import mate.academy.dto.item.UpdateCartItemDto;
import mate.academy.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management", description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get users shopping cart", description = "Retrieve user's shopping cart")
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(summary = "Add book to the shopping cart",
            description = "Add book to the shopping cart")
    public ShoppingCartDto addBookToShoppingCart(
            @RequestBody @Valid CreateCartItemRequestDto requestDto
    ) {
        return shoppingCartService.addBookToShoppingCart(requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item by id",
            description = "Update quantity of a book in the shopping cart")
    public ShoppingCartDto updateCartItemInShoppingCart(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemDto updateCartItemDto
    ) {
        return shoppingCartService.updateShoppingCart(cartItemId, updateCartItemDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Delete cart item by id",
            description = "Remove a book from the shopping cart")
    public void deleteCartItemFromShoppingCart(@PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);
    }
}
