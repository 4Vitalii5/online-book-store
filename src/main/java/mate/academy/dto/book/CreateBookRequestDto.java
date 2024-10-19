package mate.academy.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;
import mate.academy.validation.Isbn;

public record CreateBookRequestDto(
        @NotEmpty
        String title,
        @NotEmpty
        String author,
        @Isbn
        String isbn,
        @NotEmpty
        @Min(0)
        BigDecimal price,
        String description,
        String coverImage,
        @NotEmpty
        List<Long> categoryIds
) {
}
