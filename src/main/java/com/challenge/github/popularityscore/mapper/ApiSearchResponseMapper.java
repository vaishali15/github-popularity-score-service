package com.challenge.github.popularityscore.mapper;

import com.challenge.github.popularityscore.dto.ApiSearchResponseDto;
import com.challenge.github.popularityscore.model.ApiSearchResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = RepositoryMapper.class)
public interface ApiSearchResponseMapper {

    ApiSearchResponse toModel(ApiSearchResponseDto dto);

    ApiSearchResponseDto toDto(ApiSearchResponse model);
}
