package mate.academy.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.OrderProcessingException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.cart.ShoppingCartRepository;
import mate.academy.repository.order.OrderItemRepository;
import mate.academy.repository.order.OrderRepository;
import mate.academy.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public OrderDto createOrder(User user, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Can't create order. CartItems is empty");
        }
        Order order = createOrderFromCart(shoppingCart, requestDto);
        orderRepository.save(order);
        clearShoppingCart(shoppingCart);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> findAllOrders(Long userId, Pageable pageable) {
        return orderRepository.findOrdersByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderItemDto> findOrderItems(Long orderId, Pageable pageable) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Can't find order with id:" + orderId);
        }
        return orderItemRepository.findByOrderId(orderId, pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findItemById(Long orderId, Long itemId) {
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Can't find order with id:" + orderId);
        }
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find item with id:" + itemId)
        );
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    @Transactional
    public OrderDto updateOrderById(Long orderId, UpdateOrderRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't find order with id:" + orderId)
        );
        orderMapper.updateOrder(requestDto, order);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    private Order createOrderFromCart(ShoppingCart shoppingCart, CreateOrderRequestDto requestDto) {
        BigDecimal total = calculateTotal(shoppingCart);
        Order order = orderMapper.toOrder(shoppingCart);
        order.setStatus(Order.Status.NEW);
        order.setTotal(total);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.shippingAddress());
        List<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(orderItemMapper::toOrderItem)
                .peek(orderItem -> orderItem.setOrder(order))
                .toList();
        order.setOrderItems(new HashSet<>(orderItems));
        return order;
    }

    private BigDecimal calculateTotal(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
