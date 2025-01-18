package mate.academy.service.impl;

import static mate.academy.util.TestConstants.ITEM_ID;
import static mate.academy.util.TestConstants.ORDER_ID;
import static mate.academy.util.TestConstants.ORDER_NOT_FOUND_MESSAGE;
import static mate.academy.util.TestConstants.ORDER_PROCESSING_EXCEPTION_MESSAGE;
import static mate.academy.util.TestConstants.USER_ID;
import static mate.academy.util.TestUtil.CART_ITEM;
import static mate.academy.util.TestUtil.CREATE_ORDER_REQUEST_DTO;
import static mate.academy.util.TestUtil.ORDER;
import static mate.academy.util.TestUtil.ORDER_DTO;
import static mate.academy.util.TestUtil.ORDER_ITEM;
import static mate.academy.util.TestUtil.ORDER_ITEM_DTO;
import static mate.academy.util.TestUtil.PAGEABLE;
import static mate.academy.util.TestUtil.SECOND_CART_ITEM;
import static mate.academy.util.TestUtil.SHOPPING_CART;
import static mate.academy.util.TestUtil.UPDATE_ORDER_REQUEST_DTO;
import static mate.academy.util.TestUtil.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.OrderProcessingException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.repository.cart.ShoppingCartRepository;
import mate.academy.repository.order.OrderItemRepository;
import mate.academy.repository.order.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;

@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    @DisplayName("Create order from shopping cart")
    void createOrder_validShoppingCart_createsOrder() {
        // Given
        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(CART_ITEM);
        cartItems.add(SECOND_CART_ITEM);
        SHOPPING_CART.setCartItems(cartItems);
        when(orderItemMapper.toOrderItem(any())).thenReturn(ORDER_ITEM);
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(SHOPPING_CART);
        when(orderMapper.toOrder(SHOPPING_CART)).thenReturn(ORDER);
        when(orderMapper.toDto(ORDER)).thenReturn(ORDER_DTO);
        when(orderRepository.save(ORDER)).thenReturn(ORDER);

        // When
        OrderDto actualOrderDto = orderService.createOrder(USER, CREATE_ORDER_REQUEST_DTO);

        // Then
        assertThat(actualOrderDto).isEqualTo(ORDER_DTO);
        verify(shoppingCartRepository, times(1)).findByUserId(USER_ID);
        verify(orderRepository, times(1)).save(ORDER);
        verify(orderMapper, times(1)).toDto(ORDER);
    }

    @Test
    @DisplayName("Throw OrderProcessingException if cart is empty")
    void createOrder_emptyCart_throwsOrderProcessingException() {
        // Given
        ShoppingCart emptyCart = new ShoppingCart();
        emptyCart.setUser(USER);
        emptyCart.setCartItems(Set.of());
        when(shoppingCartRepository.findByUserId(USER_ID)).thenReturn(emptyCart);

        // When
        OrderProcessingException exception = assertThrows(OrderProcessingException.class,
                () -> orderService.createOrder(USER, CREATE_ORDER_REQUEST_DTO));

        // Then
        assertThat(exception.getMessage()).isEqualTo(ORDER_PROCESSING_EXCEPTION_MESSAGE);
        verify(shoppingCartRepository, times(1)).findByUserId(USER_ID);
        verifyNoMoreInteractions(shoppingCartRepository, orderRepository,
                orderMapper, orderItemMapper);
    }

    @Test
    @DisplayName("Find all orders by user ID")
    void findAllOrders_validUserId_returnsOrderList() {
        // Given
        when(orderRepository.findOrdersByUserId(USER_ID, PAGEABLE)).thenReturn(List.of(ORDER));
        when(orderMapper.toDto(ORDER)).thenReturn(ORDER_DTO);

        // When
        List<OrderDto> actualOrderDtos = orderService.findAllOrders(USER_ID, PAGEABLE);

        // Then
        assertThat(actualOrderDtos).hasSize(1);
        assertThat(actualOrderDtos.get(0)).isEqualTo(ORDER_DTO);
        verify(orderRepository, times(1)).findOrdersByUserId(USER_ID, PAGEABLE);
        verify(orderMapper, times(1)).toDto(ORDER);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    @DisplayName("Find order items by order ID")
    void findOrderItems_validOrderId_returnsOrderItemList() {
        // Given
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        when(orderItemRepository.findByOrderId(ORDER_ID, PAGEABLE)).thenReturn(List.of(ORDER_ITEM));
        when(orderItemMapper.toDto(ORDER_ITEM)).thenReturn(ORDER_ITEM_DTO);

        // When
        List<OrderItemDto> actualOrderItemDtos = orderService.findOrderItems(ORDER_ID, PAGEABLE);

        // Then
        assertThat(actualOrderItemDtos).hasSize(1);
        assertThat(actualOrderItemDtos.get(0)).isEqualTo(ORDER_ITEM_DTO);
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderItemRepository, times(1)).findByOrderId(ORDER_ID, PAGEABLE);
        verify(orderItemMapper, times(1)).toDto(ORDER_ITEM);
        verifyNoMoreInteractions(orderRepository, orderItemRepository, orderItemMapper);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException if order not found")
    void findOrderItems_nonExistentOrder_throwsEntityNotFoundException() {
        // Given
        when(orderRepository.existsById(ORDER_ID)).thenReturn(false);

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.findOrderItems(ORDER_ID, PAGEABLE));

        // Then
        assertThat(exception.getMessage()).isEqualTo(ORDER_NOT_FOUND_MESSAGE);
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verifyNoMoreInteractions(orderRepository, orderItemRepository, orderItemMapper);
    }

    @Test
    @DisplayName("Find order item by ID")
    void findItemById_validIds_returnsOrderItemDto() {
        // Given
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        when(orderItemRepository.findByIdAndOrderId(ITEM_ID, ORDER_ID))
                .thenReturn(Optional.of(ORDER_ITEM));
        when(orderItemMapper.toDto(ORDER_ITEM)).thenReturn(ORDER_ITEM_DTO);

        // When
        OrderItemDto actualOrderItemDto = orderService.findItemById(ORDER_ID, ITEM_ID);

        // Then
        assertThat(actualOrderItemDto).isEqualTo(ORDER_ITEM_DTO);
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderItemRepository, times(1))
                .findByIdAndOrderId(ITEM_ID, ORDER_ID);
        verify(orderItemMapper, times(1)).toDto(ORDER_ITEM);
        verifyNoMoreInteractions(orderRepository, orderItemRepository, orderItemMapper);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException if order item not found")
    void findItemById_nonExistentOrderItem_throwsEntityNotFoundException() {
        // Given
        when(orderRepository.existsById(ORDER_ID)).thenReturn(true);
        when(orderItemRepository.findByIdAndOrderId(ITEM_ID, ORDER_ID))
                .thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.findItemById(ORDER_ID, ITEM_ID));

        // Then
        assertThat(exception.getMessage()).isEqualTo("Can't find item with id:" + ITEM_ID);
        verify(orderRepository, times(1)).existsById(ORDER_ID);
        verify(orderItemRepository, times(1))
                .findByIdAndOrderId(ITEM_ID, ORDER_ID);
        verifyNoMoreInteractions(orderRepository, orderItemRepository, orderItemMapper);
    }

    @Test
    @DisplayName("Update order by ID")
    void updateOrderById_validOrderId_returnsUpdatedOrderDto() {
        // Given
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.of(ORDER));
        when(orderMapper.toDto(ORDER)).thenReturn(ORDER_DTO);
        when(orderRepository.save(ORDER)).thenReturn(ORDER);

        // When
        OrderDto actualOrderDto = orderService.updateOrderById(ORDER_ID, UPDATE_ORDER_REQUEST_DTO);

        // Then
        assertThat(actualOrderDto).isEqualTo(ORDER_DTO);
        verify(orderRepository, times(1)).findById(ORDER_ID);
        verify(orderMapper, times(1))
                .updateOrder(UPDATE_ORDER_REQUEST_DTO, ORDER);
        verify(orderRepository, times(1)).save(ORDER);
        verify(orderMapper, times(1)).toDto(ORDER);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }

    @Test
    @DisplayName("Throw EntityNotFoundException if updating order not found")
    void updateOrderById_nonExistentOrder_throwsEntityNotFoundException() {
        // Given
        when(orderRepository.findById(ORDER_ID)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> orderService.updateOrderById(ORDER_ID, UPDATE_ORDER_REQUEST_DTO));

        // Then
        assertThat(exception.getMessage()).isEqualTo(ORDER_NOT_FOUND_MESSAGE);
        verify(orderRepository, times(1)).findById(ORDER_ID);
        verifyNoMoreInteractions(orderRepository, orderMapper);
    }
}
