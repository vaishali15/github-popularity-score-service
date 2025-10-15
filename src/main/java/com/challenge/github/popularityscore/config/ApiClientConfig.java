package com.challenge.github.popularityscore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.Optional;

/**
 * Configures the GitHub RestClient.Builder to use everywhere in application
 */
@Configuration(proxyBeanMethods = false)
public class ApiClientConfig {

    @Bean
    @Primary
    RestClient.Builder githubRestClientBuilder(GithubProperties githubProperties, ObjectMapper objectMapper) {
        var jackson = new MappingJackson2HttpMessageConverter(objectMapper);

        var githubRestClientBuilder = RestClient.builder()
            .baseUrl(githubProperties.baseUrl())
            .messageConverters(converters -> converters.add(0, jackson))
            .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .defaultHeader("X-GitHub-Api-Version", "2022-11-28");

        Optional.ofNullable(githubProperties.token())
            .filter(token -> !token.isBlank())
            .ifPresent(token -> githubRestClientBuilder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token));

        return githubRestClientBuilder;
    }
}
