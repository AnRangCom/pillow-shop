package com.exe201.pillow.service.mapper;

import com.exe201.pillow.domain.Customer;
import com.exe201.pillow.domain.CustomerOrder;
import com.exe201.pillow.service.dto.CustomerOrderDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstName", target = "customerName")
    CustomerOrderDTO toDto(CustomerOrder customerOrder);

    @Mapping(source = "customerId", target = "customer")
    CustomerOrder toEntity(CustomerOrderDTO customerOrderDTO);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer.id", target = "customerId")
    CustomerOrderDTO toDtoCustomerId(CustomerOrder customerOrder);

    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
