package mate.academy.controller;

import static mate.academy.util.TestConstants.NON_EXISTING_ID;
import static mate.academy.util.TestConstants.USER_ID;
import static mate.academy.util.TestConstants.VALID_FIRST_BOOK_ID;
import static mate.academy.util.TestConstants.VALID_SECOND_BOOK_ID;
import static mate.academy.util.TestConstants.VALID_USER_EMAIL;
import static mate.academy.util.TestUtil.CREATE_CART_ITEM_REQUEST_DTO;
import static mate.academy.util.TestUtil.INVALID_CREATE_CART_ITEM_REQUEST_DTO;
import static mate.academy.util.TestUtil.INVALID_UPDATE_CART_ITEM_DTO;
import static mate.academy.util.TestUtil.UPDATE_CART_ITEM_DTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.dto.cart.ShoppingCartDto;
import mate.academy.dto.item.CartItemResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "classpath:database/clean-database.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/roles/add-roles.sql",
        "classpath:database/users/add-users.sql",
        "classpath:database/users-roles/set-users-roles.sql",
        "classpath:database/books/add-books.sql",
        "classpath:database/shopping-carts/add-shopping-carts.sql",
        "classpath:database/shopping-carts/add-cart-items.sql",
        "classpath:database/categories/add-categories.sql",
        "classpath:database/books-categories/set-books-to-categories.sql",
        "classpath:database/orders/add-orders.sql",
        "classpath:database/orders/add-order-items.sql"
})
@Sql(scripts = {
        "classpath:database/orders/remove-order-items.sql",
        "classpath:database/shopping-carts/remove-cart-items.sql",
        "classpath:database/books-categories/remove-books-categories.sql",
        "classpath:database/shopping-carts/remove-shopping-carts.sql",
        "classpath:database/orders/remove-orders.sql",
        "classpath:database/books/remove-books.sql",
        "classpath:database/categories/remove-categories.sql",
        "classpath:database/users/remove-users.sql",
        "classpath:database/roles/remove-roles.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingCartControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get shopping cart")
    @WithUserDetails(value = VALID_USER_EMAIL)
    void getShoppingCart_returnsShoppingCart() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ShoppingCartDto responseDto = objectMapper.readValue(jsonResponse, ShoppingCartDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("Add book to shopping cart")
    @WithUserDetails(value = VALID_USER_EMAIL)
    void addBookToShoppingCart_withValidInput_returnsUpdatedCart() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(post("/cart")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(CREATE_CART_ITEM_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ShoppingCartDto responseDto = objectMapper.readValue(jsonResponse, ShoppingCartDto.class);

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.cartItems()).hasSize(2);
    }

    @Test
    @DisplayName("Add book to shopping cart with invalid input")
    @WithMockUser(roles = "USER")
    void addBookToShoppingCart_withInvalidInput_returnsBadRequest() throws Exception {
        // When
        mockMvc.perform(post("/cart")
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                INVALID_CREATE_CART_ITEM_REQUEST_DTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update cart item")
    @WithUserDetails(value = VALID_USER_EMAIL)
    void updateCartItem_withValidId_returnsUpdatedCart() throws Exception {
        // When
        MvcResult mvcResult = mockMvc.perform(put("/cart/items/{cartItemId}", VALID_FIRST_BOOK_ID)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(UPDATE_CART_ITEM_DTO)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ShoppingCartDto responseDto = objectMapper.readValue(jsonResponse, ShoppingCartDto.class);
        int quantity = responseDto.cartItems().stream()
                        .filter(item -> item.id().equals(VALID_FIRST_BOOK_ID))
                                .map(CartItemResponseDto::quantity)
                                        .findFirst()
                                                .get();

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.cartItems()).hasSize(2);
        assertThat(quantity)
                .isEqualTo(UPDATE_CART_ITEM_DTO.quantity());
    }

    @Test
    @DisplayName("Update cart item with invalid id")
    @WithUserDetails(value = VALID_USER_EMAIL)
    void updateCartItem_withInvalidId_returnsNotFound() throws Exception {
        // When
        mockMvc.perform(put("/cart/items/{cartItemId}", NON_EXISTING_ID)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(UPDATE_CART_ITEM_DTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update cart item with invalid input")
    @WithMockUser(roles = "USER")
    void updateCartItem_withInvalidInput_returnsBadRequest() throws Exception {
        // When
        mockMvc.perform(put("/cart/items/{cartItemId}", VALID_FIRST_BOOK_ID)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(INVALID_UPDATE_CART_ITEM_DTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete cart item")
    @WithUserDetails(value = VALID_USER_EMAIL)
    void deleteCartItem_withValidId_deletesItem() throws Exception {
        // When
        mockMvc.perform(delete("/cart/items/{cartItemId}", VALID_SECOND_BOOK_ID)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Then
        MvcResult mvcResult = mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        ShoppingCartDto responseDto = objectMapper.readValue(jsonResponse, ShoppingCartDto.class);
        assertThat(responseDto.cartItems())
                .noneMatch(item -> item.id().equals(VALID_SECOND_BOOK_ID));
    }
}
