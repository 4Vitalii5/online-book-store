package mate.academy.dto.item;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemDto(
        @Positive
        int quantity
) {
}
