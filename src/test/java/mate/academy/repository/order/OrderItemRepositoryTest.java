package mate.academy.repository.order;

import static mate.academy.util.TestConstants.FIRST_RECORD;
import static mate.academy.util.TestConstants.INVALID_ORDER_ID;
import static mate.academy.util.TestConstants.ITEM_ID;
import static mate.academy.util.TestConstants.NON_EXISTING_ID;
import static mate.academy.util.TestConstants.NON_EXISTING_ORDER_ID;
import static mate.academy.util.TestConstants.ORDER_ID;
import static mate.academy.util.TestConstants.SEARCH_PAGE_NUMBER;
import static mate.academy.util.TestConstants.SEARCH_PAGE_SIZE;
import static mate.academy.util.TestUtil.PAGEABLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import mate.academy.model.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/clean-database.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {
        "classpath:database/users/add-users.sql",
        "classpath:database/books/add-books.sql",
        "classpath:database/orders/add-orders.sql",
        "classpath:database/orders/add-order-items.sql",
})
@Sql(scripts = {
        "classpath:database/orders/remove-order-items.sql",
        "classpath:database/orders/remove-orders.sql",
        "classpath:database/users/remove-users.sql",
        "classpath:database/books/remove-books.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderItemRepositoryTest {
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("Verify findByOrderId() method works")
    void findByOrderId_validOrderId_returnsOrderItems() {
        // When
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(
                ORDER_ID, PAGEABLE);

        // Then
        assertThat(orderItems).isNotEmpty();
        assertThat(orderItems.get(FIRST_RECORD).getOrder().getId()).isEqualTo(ORDER_ID);
    }

    @Test
    @DisplayName("Verify findByIdAndOrderId() method works")
    void findByIdAndOrderId_validIds_returnsOrderItem() {
        // When
        Optional<OrderItem> orderItem = orderItemRepository.findByIdAndOrderId(
                ITEM_ID, ORDER_ID);

        // Then
        assertThat(orderItem).isPresent();
        assertThat(orderItem.get().getId()).isEqualTo(ITEM_ID);
    }

    @Test
    @DisplayName("Verify findByIdAndOrderId() returns empty for invalid orderId")
    void findByIdAndOrderId_invalidOrderId_returnsEmpty() {
        // When
        Optional<OrderItem> orderItem = orderItemRepository.findByIdAndOrderId(
                ITEM_ID, INVALID_ORDER_ID);

        // Then
        assertThat(orderItem).isNotPresent();
    }

    @Test
    @DisplayName("Verify findById() returns empty for non-existing Id")
    void findById_nonExistingId_returnsEmpty() {
        // When
        Optional<OrderItem> orderItem = orderItemRepository.findById(
                NON_EXISTING_ID);

        // Then
        assertThat(orderItem).isNotPresent();
    }

    @Test
    @DisplayName("Verify findByOrderId() returns empty list for non-existing orderId")
    void findByOrderId_nonExistingOrderId_returnsEmptyList() {
        // When
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(
                NON_EXISTING_ORDER_ID, PageRequest.of(SEARCH_PAGE_NUMBER, SEARCH_PAGE_SIZE));

        // Then
        assertThat(orderItems).isEmpty();
    }
}
