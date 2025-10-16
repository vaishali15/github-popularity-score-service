package com.challenge.github.popularityscore.controller;


import com.challenge.github.popularityscore.dto.egress.PageResponseDto;
import com.challenge.github.popularityscore.dto.egress.RepositoryResponseDto;
import com.challenge.github.popularityscore.service.GithubRepositorySearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static java.time.OffsetDateTime.parse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepositorySearchController.class)
public class RepositorySearchControllerTest {

    private static final String SEARCH_URL = "/api/v1/repositories/search";
    private static final OffsetDateTime SAMPLE_TIME = parse("2025-10-01T00:00:00Z");

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    GithubRepositorySearchService service;

    @Test
    void searchRepositories() throws Exception {
        var item = new RepositoryResponseDto(
            "test/repo", "https://github.com/test/repo",
            10, 2, parse("2025-10-01T00:00:00Z"), 123.45
        );
        var pageResponseDto = new PageResponseDto<>(1, 2, 1, 100, List.of(item));

        when(service.search("Java", "2024-01-01", 1, 2)).thenReturn(pageResponseDto);

        var mvcResult = mvc.perform(get("/api/v1/repositories/search")
                .param("language", "Java")
                .param("createdAfter", "2024-01-01")
                .param("page", "1").param("size", "2"))
            .andExpect(status().isOk())
            .andReturn();

        var body = mvcResult.getResponse().getContentAsString();

        var type = mapper.getTypeFactory()
            .constructParametricType(PageResponseDto.class, RepositoryResponseDto.class);
        PageResponseDto<RepositoryResponseDto> page = mapper.readValue(body, type);

        assertThat(page.totalAvailable()).isEqualTo(100);
        assertThat(page.items()).isNotEmpty();
    }

    @Test
    void invalidRequestParams() throws Exception {
        mvc.perform(get(SEARCH_URL)
                .param("language", "")
                .param("createdAfter", "bad")
                .param("page", "0")
                .param("size", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }
}
