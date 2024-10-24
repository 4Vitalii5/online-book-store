package mate.academy.dto.item;

import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @Positive
        Long bookId,
        @Positive
        int quantity
) {
}
