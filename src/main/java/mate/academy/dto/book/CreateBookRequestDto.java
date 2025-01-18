package mate.academy.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.validation.Isbn;

public record CreateBookRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @Isbn
        String isbn,
        @NotNull
        @Positive
        BigDecimal price,
        String description,
        String coverImage,
        @NotEmpty
        List<Long> categoryIds
) {
}
