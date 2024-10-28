package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.CreateOrderRequestDto;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.UpdateOrderRequestDto;
import mate.academy.model.Order;
import mate.academy.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class, ShoppingCartMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    Order toEntity(CreateOrderRequestDto requestDto);

    @Mapping(source = "user", target = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "cartItems", target = "orderItems")
    Order toOrder(ShoppingCart shoppingCart);

    void updateOrder(UpdateOrderRequestDto requestDto, @MappingTarget Order order);
}
