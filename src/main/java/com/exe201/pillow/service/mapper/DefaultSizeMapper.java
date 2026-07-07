package com.exe201.pillow.service.mapper;

import com.exe201.pillow.domain.DefaultSize;
import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.service.dto.DefaultSizeDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DefaultSizeMapper extends EntityMapper<DefaultSizeDTO, DefaultSize> {
    @Mapping(source = "pillow.id", target = "pillowId")
    DefaultSizeDTO toDto(DefaultSize defaultSize);

    @Mapping(source = "pillowId", target = "pillow")
    DefaultSize toEntity(DefaultSizeDTO defaultSizeDTO);

    @Named("pillowId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "pillow.id", target = "pillowId")
    DefaultSizeDTO toDtoPillowId(DefaultSize defaultSize);

    default Pillow pillowFromId(Long id) {
        if (id == null) {
            return null;
        }
        Pillow pillow = new Pillow();
        pillow.setId(id);
        return pillow;
    }
}
