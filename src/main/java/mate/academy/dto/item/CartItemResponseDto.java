package mate.academy.dto.item;

public record CartItemResponseDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {
}
