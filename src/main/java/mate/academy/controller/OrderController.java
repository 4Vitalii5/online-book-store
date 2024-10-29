package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.dto.order.UpdateOrderRequestDto;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing order")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Place an order", description = "Make an order")
    public OrderDto addOrder(Authentication authentication,
                             @RequestBody @Valid CreateOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user, requestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve user's order history")
    public List<OrderDto> getOrders(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrders(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all items from order",
            description = "Retrieve all OrderItems for a specific order")
    public List<OrderItemDto> getOrderItems(@PathVariable("orderId") Long orderId,
                                            Pageable pageable) {
        return orderService.findOrderItems(orderId, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get item by order id",
            description = "Retrieve a specific OrderItem within an order")
    public OrderItemDto getOrderItem(@PathVariable("orderId") Long orderId,
                                     @PathVariable("id") Long itemId) {
        return orderService.findItemById(orderId, itemId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderDto updateOrderStatus(@PathVariable("id") Long orderId,
                                      @RequestBody @Valid UpdateOrderRequestDto requestDto) {
        return orderService.updateOrderById(orderId, requestDto);
    }
}
