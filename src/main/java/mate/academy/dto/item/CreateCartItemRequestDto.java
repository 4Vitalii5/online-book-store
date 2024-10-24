package mate.academy.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateCartItemRequestDto(
        @NotNull
        @Min(0)
        Long bookId,
        @NotNull
        @Min(0)
        int quantity
) {
}
