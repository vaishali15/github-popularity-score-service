package com.challenge.github.popularityscore.service;

import com.challenge.github.popularityscore.model.ApiSearchResponse;

public interface RepositorySearchService {

    ApiSearchResponse search(String language, String createdAfter, int page, int size);
}
