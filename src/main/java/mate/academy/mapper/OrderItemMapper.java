package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItem.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(source = "book.price", target = "price")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", expression = "java(order)")
    OrderItem toOrderItem(CartItem cartItem, @Context Order order);
}