package mate.academy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequestDto(
        @NotBlank
        @Size(min = 3, max = 100)
        String name,
        @Size(min = 8, max = 1000)
        String description
) {
}
