package mate.academy.dto.order;

public record OrderItemDto(
        Long id,
        Long bookId,
        int quantity
) {
}
