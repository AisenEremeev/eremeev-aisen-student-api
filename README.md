# Eremeev Aisen Student API

Author: Aisen Eremeev, IT2-2314.
Project focus: Student API baseline.

Spring Boot 3.5, Java 21, Gradle wrapper, PostgreSQL and Docker Compose.
GET / displays the author and application version.

## Run

Create `.env` from `.env.example` and replace the example password with a locally
generated value. Do not commit `.env`. Run `docker compose up -d --build`.
Application URL: http://localhost:8082/ . API: http://localhost:8082/api/students .
PostgreSQL is reached at service `db`, not localhost. Only the application port is
published on the host loopback interface.

## Test

`./gradlew test` runs automated HTTP/persistence tests using H2 in PostgreSQL mode.
The live Compose smoke test exercises the actual PostgreSQL database separately.

## API

POST /api/students creates a student (201).
GET /api/students lists students (200).
GET /api/students/{id} retrieves one student (200 or 404).
DELETE /api/students/{id} deletes a student (204 or 404).
Request fields: name, email, groupName. Invalid input returns 400.

## Linux

`scripts/Eremeev_Aisen_system_report.sh` writes the report to the personalized log.
The script mode is 744: owner rwx, group r, others r.
`>` replaces file contents; `>>` appends to the existing file.

## CI

Jenkins job: Eremeev_Aisen_Student_API. Stages: Checkout, Build, Test, Docker Build,
Verify. Polling checks GitHub every minute. Tests must pass before Docker Build.
CI integrates and checks code; Continuous Delivery keeps it ready for release;
Continuous Deployment releases successful changes automatically. This pipeline
verifies the image and does not deploy it.

Database secrets remain local in `.env`. Public SCM checkout and H2 tests do not
require Jenkins credentials. If private SCM or a registry is added, store its
credential in Jenkins Credentials and bind it only to the required stage.
Never commit passwords: Git history and image layers can retain them.

## CRUD feature by Aisen Eremeev
PUT /api/students/{id} updates name, email and groupName; invalid input returns 400 and a missing ID returns 404. Automated tests verify persisted updates and missing records.
