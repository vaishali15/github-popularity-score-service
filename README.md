# GitHub Popularity Score Service

A Spring Boot REST API that fetches GitHub repositories and computes a **popularity score** using stars, forks, and recency of updates.

## 🚀 Features
* REST API to search GitHub repos with filters (language, created-after date)
* Popularity scoring with configurable stars weight, forks weight & recency half-life

## 🧩 Tech Stack

* **Java 21**, **Spring Boot 3.5.6**
* **Jackson**, **Lombok**
* **OpenAPI / Swagger for API documentation**
* **Testing:** JUnit 5, AssertJ, Mockito, WireMock 3.x

---

## 🚀 Run Locally

### Prerequisites

* Java 21+
* Maven 3.9+

### Clone & Run

```bash
1. git clone https://github.com/vaishali15/github-popularity-score-service.git
2. cd github-popularity-score-service
3. ./mvnw spring-boot:run
```

### Configuration

The service interacts with the GitHub REST API. You have the option to use it without authentication (with lower rate limits) or to provide a personal access token for higher limits.

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

## 🔎 Swagger / OpenAPI

* **Swagger UI:**
  `http://localhost:8080/swagger-ui/index.html`

* **OpenAPI JSON:**
  `http://localhost:8080/v1/api-docs`

---

## 🩺 Actuator (Health & Info)

* Health: `GET http://localhost:8080/actuator/health`
* Info: `GET http://localhost:8080/actuator/info`
* Metrics: `GET http://localhost:8080/actuator/metrics`

---
