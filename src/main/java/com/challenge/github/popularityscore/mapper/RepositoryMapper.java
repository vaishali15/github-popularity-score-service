package com.challenge.github.popularityscore.mapper;

import com.challenge.github.popularityscore.dto.GitHubRepoMetaDto;
import com.challenge.github.popularityscore.model.Repository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {
    Repository toModel(GitHubRepoMetaDto dto);

    GitHubRepoMetaDto toDto(Repository model);
}
