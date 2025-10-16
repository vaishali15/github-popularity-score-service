package com.challenge.github.popularityscore.external.github.mapper;

import com.challenge.github.popularityscore.external.github.dto.GithubSearchResponseDto;
import com.challenge.github.popularityscore.external.github.model.GithubSearchResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = GithubRepositoryItemMapper.class)
public interface GithubSearchResponseMapper {
    GithubSearchResponse toModel(GithubSearchResponseDto dto);

    GithubSearchResponseDto toDto(GithubSearchResponse model);
}
