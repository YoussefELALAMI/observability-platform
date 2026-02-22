# 🔭 Mini Observability Platform

> A production-grade backend platform for application observability — handling log ingestion, metric aggregation, and real-time alerting.

[![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![Kafka](https://img.shields.io/badge/Kafka-3.4-black?style=flat-square&logo=apachekafka)](https://kafka.apache.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red?style=flat-square&logo=redis)](https://redis.io/)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#️-tech-stack)
- [Quick Start](#-quick-start)
- [Project Status](#-project-status)
- [Documentation](#-documentation)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)

---

## 🧭 Overview

Mini Observability Platform is a portfolio project that simulates a real-world backend observability system. It is designed to demonstrate backend engineering skills including event-driven architecture, database design, caching strategies, and API development.

**Core capabilities:**
- 📥 **Log Ingestion** — High-throughput event ingestion via REST API and Kafka
- 📊 **Metric Aggregation** — Time-series metric processing and storage
- 🚨 **Alerting Engine** — Rule-based alerting with configurable thresholds

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| Database | PostgreSQL 15 |
| Message Broker | Apache Kafka 3.4 |
| Cache | Redis 7 |
| Build Tool | Maven 3.8+ |
| Infrastructure | Docker & Docker Compose |

---

## 🚀 Quick Start

### Prerequisites

Make sure you have the following installed before proceeding:

- **Java 17+** — `java -version`
- **Docker & Docker Compose** — `docker -v`
- **Maven 3.8+** — `mvn -v`

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/mini-observability-platform.git
cd mini-observability-platform
```

### 2. Start Infrastructure

Spin up PostgreSQL, Kafka, and Redis using Docker Compose:

```bash
docker-compose up -d
```

Verify all services are healthy:

```bash
docker-compose ps
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

### 4. Verify the Setup

```bash
# Ping endpoint
curl http://localhost:8080/api/v1/ping

# Health check
curl http://localhost:8080/actuator/health
```

You should see a `200 OK` response from ping and all components showing `UP` in the health check.

---

## ✅ Verification Checklist

Use this checklist after setup to confirm everything is working:

```
Environment
  [ ] Java 17+ installed       java -version
  [ ] Docker running           docker ps

Infrastructure
  [ ] Docker Compose services running    docker-compose ps
  [ ] PostgreSQL accessible              port 5432
  [ ] Redis accessible                   port 6379
  [ ] Kafka accessible                   port 9092

Application
  [ ] Application starts without errors
  [ ] Ping endpoint returns 200 OK
  [ ] Health endpoint shows all UP
  [ ] Logs table created in database
  [ ] Kafka topic "events" exists

Quality
  [ ] Tests pass               ./mvnw test
  [ ] Git initialized with first commit
```

---

## 📊 Project Status

| Phase | Description | Status |
|---|---|---|
| Phase 1 | Core Ingestion API | 🔄 In Progress |
| Phase 2 | Metrics Aggregation | ⏳ Planned |
| Phase 3 | Alerting Engine | ⏳ Planned |
| Phase 4 | Production Ready | ⏳ Planned |

---

## 📚 Documentation

- [Project Specification](./docs/SPECIFICATION.md)
- [Architecture Overview](./docs/ARCHITECTURE.md)
- [API Documentation](./docs/API.md)

---

## 🆘 Troubleshooting

<details>
<summary><strong>Cannot connect to Docker daemon</strong></summary>

```bash
# Start Docker Desktop (Mac/Windows)
# Or on Linux:
sudo systemctl start docker
```
</details>

<details>
<summary><strong>Port 5432 already in use</strong></summary>

```bash
# Find what's using the port
lsof -i :5432        # Mac/Linux
netstat -ano | findstr :5432   # Windows

# Stop the conflicting service, or change the port in docker-compose.yml
```
</details>

<details>
<summary><strong>Flyway migration failed</strong></summary>

```bash
# Drop and recreate the database
docker exec -it observability-postgres psql -U postgres -c "DROP DATABASE observability;"
docker exec -it observability-postgres psql -U postgres -c "CREATE DATABASE observability;"

# Restart the application
./mvnw spring-boot:run
```
</details>

<details>
<summary><strong>Application fails to start — Kafka error</strong></summary>

```bash
# Kafka may still be initializing — wait 30 seconds, then retry
# Check Kafka logs if the issue persists:
docker-compose logs kafka
```
</details>

<details>
<summary><strong>Tests fail with connection errors</strong></summary>

```bash
# Ensure all Docker services are running
docker-compose ps

# Restart if needed
docker-compose restart
```
</details>

If you're still stuck:
1. Run `docker-compose logs` to inspect service output
2. Check the application console for stack traces
3. Search the exact error message — it usually leads straight to the fix

---

## 👤 Author

**[Your Name]** — [GitHub](https://github.com/your-username) · [LinkedIn](https://linkedin.com/in/your-profile)

---

## 📝 License

This project is built for learning and portfolio purposes. Feel free to explore, fork, and build on it.