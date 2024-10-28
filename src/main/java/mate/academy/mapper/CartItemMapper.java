package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.item.CartItemResponseDto;
import mate.academy.dto.item.CreateCartItemRequestDto;
import mate.academy.dto.item.UpdateCartItemDto;
import mate.academy.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "bookId", target = "book.id")
    CartItem toEntity(CreateCartItemRequestDto requestDto);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    void updateCartItemFromDto(UpdateCartItemDto requestDto,
                               @MappingTarget CartItem cartItem);
}
