package com.challenge.github.popularityscore.external.github.mapper;

import com.challenge.github.popularityscore.external.github.dto.GithubRepositoryItemDto;
import com.challenge.github.popularityscore.external.github.model.GithubRepositoryItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GithubRepositoryItemMapper {
    GithubRepositoryItem toModel(GithubRepositoryItemDto dto);

    GithubRepositoryItemDto toDto(GithubRepositoryItem model);
}

