package com.exe201.pillow.service.mapper;

import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.domain.DefaultSize;
import com.exe201.pillow.domain.OrderItem;
import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.service.dto.OrderItemDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(source = "pillow.id", target = "pillowId")
    @Mapping(source = "pillow.name", target = "pillowName")
    @Mapping(source = "defaultSize.id", target = "defaultSizeId")
    @Mapping(source = "defaultSize.name", target = "defaultSizeName")
    @Mapping(source = "order.id", target = "orderId")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(source = "pillowId", target = "pillow")
    @Mapping(source = "defaultSizeId", target = "defaultSize")
    @Mapping(source = "orderId", target = "order")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "order.id", target = "orderId")
    OrderItemDTO toDtoOrderId(OrderItem orderItem);

    default Pillow pillowFromId(Long id) {
        if (id == null) {
            return null;
        }
        Pillow pillow = new Pillow();
        pillow.setId(id);
        return pillow;
    }

    default DefaultSize defaultSizeFromId(Long id) {
        if (id == null) {
            return null;
        }
        DefaultSize defaultSize = new DefaultSize();
        defaultSize.setId(id);
        return defaultSize;
    }

    default CustomerOrder orderFromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerOrder order = new CustomerOrder();
        order.setId(id);
        return order;
    }
}
