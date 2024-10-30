package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderRequestDto;
import mate.academy.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(User user, CreateOrderRequestDto requestDto);

    List<OrderDto> findAllOrders(Long userId, Pageable pageable);

    List<OrderItemDto> findOrderItems(Long orderId, Pageable pageable);

    OrderItemDto findItemById(Long orderId, Long itemId);

    OrderDto updateOrderById(Long orderId, UpdateOrderRequestDto requestDto);
}
