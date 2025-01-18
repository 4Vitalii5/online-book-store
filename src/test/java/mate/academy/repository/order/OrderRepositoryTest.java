package mate.academy.repository.order;

import static mate.academy.util.TestConstants.FIRST_RECORD;
import static mate.academy.util.TestConstants.NON_EXISTING_ORDER_ID;
import static mate.academy.util.TestConstants.ORDER_ID;
import static mate.academy.util.TestConstants.USER_ID;
import static mate.academy.util.TestUtil.PAGEABLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
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
        "classpath:database/orders/add-orders.sql",
})
@Sql(scripts = {
        "classpath:database/orders/remove-orders.sql",
        "classpath:database/users/remove-users.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Verify findOrdersByUserId() method works")
    void findOrdersByUserId_validUserId_returnsOrders() {
        // When
        List<Order> orders = orderRepository.findOrdersByUserId(
                USER_ID, PAGEABLE);

        // Then
        assertThat(orders).isNotEmpty();
        assertThat(orders.get(FIRST_RECORD).getUser().getId()).isEqualTo(USER_ID);
    }

    @Test
    @DisplayName("Verify existsById() method works")
    void existsById_validId_returnsTrue() {
        // When
        boolean exists = orderRepository.existsById(ORDER_ID);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Verify existsById() method returns false for non-existing Id")
    void existsById_nonExistingId_returnsFalse() {
        // When
        boolean exists = orderRepository.existsById(NON_EXISTING_ORDER_ID);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Verify findById() method works")
    void findById_validId_returnsOrder() {
        // When
        Optional<Order> order = orderRepository.findById(ORDER_ID);

        // Then
        assertThat(order).isPresent();
        assertThat(order.get().getId()).isEqualTo(ORDER_ID);
    }

    @Test
    @DisplayName("Verify findById() returns empty for non-existing Id")
    void findById_nonExistingId_returnsEmpty() {
        // When
        Optional<Order> order = orderRepository.findById(NON_EXISTING_ORDER_ID);

        // Then
        assertThat(order).isNotPresent();
    }
}
