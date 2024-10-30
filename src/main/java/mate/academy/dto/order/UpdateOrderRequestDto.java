package mate.academy.dto.order;

import jakarta.validation.constraints.NotBlank;
import mate.academy.model.Order;
import mate.academy.validation.ValidEnum;

public record UpdateOrderRequestDto(
        @NotBlank
        @ValidEnum(enumClass = Order.Status.class, message = "Invalid order status")
        String status
) {
}
