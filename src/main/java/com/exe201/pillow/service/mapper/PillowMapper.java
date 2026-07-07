package com.exe201.pillow.service.mapper;

import com.exe201.pillow.domain.Pillow;
import com.exe201.pillow.service.dto.PillowDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PillowMapper extends EntityMapper<PillowDTO, Pillow> {
    PillowDTO toDto(Pillow pillow);

    Pillow toEntity(PillowDTO pillowDTO);
}
