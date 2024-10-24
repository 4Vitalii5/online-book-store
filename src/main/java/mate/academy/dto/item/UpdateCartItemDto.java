package mate.academy.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemDto(
        @NotNull
        @Min(1)
        int quantity
) {
}
