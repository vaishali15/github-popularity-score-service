# GitHub Popularity Score Service

A Spring Boot REST API that fetches GitHub repositories and computes a **popularity score** using stars, forks, and recency of updates.

## 🚀 Features
* REST API to search GitHub repos with filters (language, created-after date)
* Popularity scoring with configurable stars weight, forks weight & recency half-life
* Swagger/OpenAPI UI for interactive docs
* Actuator health/info endpoints
* Tests: controller (WebMvc) + end-to-end integration with WireMock


## 🧩 Tech Stack

* **Java 21**, **Spring Boot 3.5.6** (Spring MVC, Actuator, Validation)
* **Jackson** (snake_case for ingress), **Lombok**
* **OpenAPI/Swagger** via `springdoc-openapi-starter-webmvc-ui`
* **Testing:** JUnit 5, AssertJ, Mockito, WireMock 3.x

---

## 🚀 Run Locally

### Prerequisites

* Java 21+
* Maven 3.9+

### Clone & Run

```bash
git clone https://github.com/vaishali15/github-popularity-score-service.git
cd github-popularity-score-service
./mvnw spring-boot:run
```

### Configuration

The service calls the GitHub REST API. You can run unauthenticated (lower rate limits) or provide a personal access token.

```bash
export GITHUB_TOKEN=<your_github_pat>   # optional, improves rate limits
```

Key properties (already set in `application.yml`):

```yaml
spring:
  application:
    name: "GitHub Popularity Score Service"
  jackson:
    property-naming-strategy: SNAKE_CASE

server:
  port: 8080

app:
  github:
    base-url: https://api.github.com
    token: ${GITHUB_TOKEN:}       # optional
  scoring:
    stars-weight: 0.7
    forks-weight: 0.2
    recency-weight: 0.1
    recency-half-life-days: 30
```

---

## 📚 API

### Search Repositories

`GET /api/v1/repositories/search`

**Query parameters**

* `language` (string, required): primary language, e.g. `Java`
* `createdAfter` (string, required): ISO date `YYYY-MM-DD`, lower bound for repo creation
* `page` (int, optional, default `1`): 1-based page index
* `size` (int, optional, default `10`, max `100`): page size

**200 OK – Response body**

```json
{
    "page": 1,
    "size": 10,
    "total": 10,
    "totalAvailable": 123456,
    "items": [
        {
            "repositoryName": "spring-projects/spring-boot",
            "repositoryUrl": "https://github.com/spring-projects/spring-boot",
            "stars": 72000,
            "forks": 43000,
            "lastUpdated": "2025-09-01T12:34:56Z",
            "popularityScore": 50432.7
        }
    ]
}
```

**Examples**

```bash
curl "http://localhost:8080/api/v1/repositories/search?language=Java&createdAfter=2024-01-01&page=1&size=5"
```

### Error Handling (Problem Details)

Validation errors and server errors return Problem Details:

```json
{
    "type": "about:blank",
    "title": "Constraint violation",
    "status": 400,
    "detail": "Request parameters are invalid.",
    "instance": "/api/v1/repositories/search"
}
```

---

## 📖 Scoring (High-Level)

A weighted formula aggregates:

* **Stars** (weight default `0.7`)
* **Forks** (weight default `0.2`)
* **Recency** (weight default `0.1`) using **half-life** decay (default `30` days)

Tune weights via `app.scoring.*` in configuration.

---

## 🔎 Swagger / OpenAPI

Once the app is running:

* **Swagger UI:**
  `http://localhost:8080/swagger-ui/index.html`

* **OpenAPI JSON:**
  `http://localhost:8080/v1/api-docs`
* 
---

## 🩺 Actuator (Health & Info)

* Health: `GET http://localhost:8080/actuator/health`
* Info: `GET http://localhost:8080/actuator/info`
* Metrics: `GET http://localhost:8080/actuator/metrics`

---

## 🧪 Testing

### Run all tests

```bash
./mvnw -q clean test
```

### What’s covered

* **Unit**: `PopularityScoreCalculator`
* **WebMvc**: `RepositorySearchController` 
* **Integration (WireMock)**: stubs GitHub `/search/repositories` and verifies end-to-end behaviour.

---
