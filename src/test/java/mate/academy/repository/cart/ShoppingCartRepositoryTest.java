package mate.academy.repository.cart;

import static mate.academy.util.TestConstants.INVALID_USER_ID;
import static mate.academy.util.TestConstants.SHOPPING_CART_ID;
import static mate.academy.util.TestConstants.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import mate.academy.model.ShoppingCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/clean-database.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/users/add-users.sql",
        "classpath:database/shopping-carts/add-shopping-carts.sql"
})
@Sql(scripts = {
        "classpath:database/shopping-carts/remove-shopping-carts.sql",
        "classpath:database/users/remove-users.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Verify findByUserId() method works")
    void findByUserId_validUserId_returnsShoppingCart() {
        // When
        ShoppingCart foundCart = shoppingCartRepository.findByUserId(USER_ID);

        // Then
        assertThat(foundCart).isNotNull();
        assertThat(foundCart.getId()).isEqualTo(SHOPPING_CART_ID);
        assertThat(foundCart.getUser().getId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("Verify findByUserId() returns null when no cart is found for user")
    void findByUserId_noCartForUser_returnsNull() {
        // When
        ShoppingCart foundCart = shoppingCartRepository.findByUserId(INVALID_USER_ID);

        // Then
        assertThat(foundCart).isNull();
    }
}
