package com.exe201.pillow.service.mapper;

import com.exe201.pillow.domain.Customer;
import com.exe201.pillow.domain.User;
import com.exe201.pillow.service.dto.CustomerDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(source = "user.id", target = "userId")
    CustomerDTO toDto(Customer customer);

    @Mapping(source = "userId", target = "user")
    Customer toEntity(CustomerDTO customerDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.id", target = "userId")
    CustomerDTO toDtoUserId(Customer customer);

    default User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
