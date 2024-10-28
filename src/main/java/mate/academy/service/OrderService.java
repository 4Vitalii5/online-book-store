package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto createOrder(CreateOrderRequestDto requestDto);

    List<OrderDto> findAllOrders(Pageable pageable);

    List<OrderItemDto> findOrderItems(Long orderId, Pageable pageable);

    OrderItemDto findItemById(Long orderId, Long itemId);

    OrderDto updateOrderById(Long orderId, UpdateOrderRequestDto requestDto);
}
