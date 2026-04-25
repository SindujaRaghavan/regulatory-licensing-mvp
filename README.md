# Regulatory Licensing MVP

Backend MVP for a government-style Regulatory and Licensing Platform.

## Selected Use Cases

- Fully built: Use Case 1 — Operator Application Submission & Resubmission
- Partially built: Use Case 2 — Officer Review & Feedback

See `SCOPE.md` for scope decisions, assumptions, and deferred items.

## Tech Stack

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- H2 in-memory database
- Bean Validation
- Swagger / springdoc-openapi

## How to Run

```bash
mvn clean spring-boot:run
```

Application runs at:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

H2 Console:

```text
http://localhost:8080/h2-console
```

H2 JDBC URL:

```text
jdbc:h2:mem:licensingdb
```

Username:

```text
sa
```

Password is blank.

## API Flow

### 1. Operator submits application

```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "businessName": "ABC Food Pte Ltd",
    "operatorName": "Sinduja",
    "licenceType": "Food Shop Licence",
    "applicationDetails": "New food shop licence application",
    "documents": [
      {"fileName": "floor-plan.pdf", "documentType": "FLOOR_PLAN"},
      {"fileName": "acra-profile.pdf", "documentType": "ACRA_PROFILE"}
    ]
  }'
```

### 2. Officer starts review

```bash
curl -X POST http://localhost:8080/api/applications/1/review/start
```

### 3. Officer requests resubmission

```bash
curl -X POST http://localhost:8080/api/applications/1/review/request-resubmission \
  -H "Content-Type: application/json" \
  -d '{
    "feedbacks": [
      {"section": "Documents", "comment": "Please upload the latest ACRA profile."},
      {"section": "Business Details", "comment": "Operating address is incomplete."}
    ]
  }'
```

### 4. Operator views status and comments

```bash
curl "http://localhost:8080/api/applications/1?role=OPERATOR"
```

### 5. Operator resubmits corrected information

```bash
curl -X POST http://localhost:8080/api/applications/1/resubmit \
  -H "Content-Type: application/json" \
  -d '{
    "businessName": "ABC Food Pte Ltd",
    "operatorName": "Sinduja",
    "licenceType": "Food Shop Licence",
    "applicationDetails": "Updated application with complete operating address.",
    "documents": [
      {"fileName": "latest-acra-profile.pdf", "documentType": "ACRA_PROFILE"}
    ]
  }'
```

## Status Mapping

The API returns both:

- `internalStatus`
- `displayStatus`

`displayStatus` changes based on `role=OPERATOR` or `role=OFFICER`.

## Validation and Error Handling

Implemented:

- Required field validation using Bean Validation
- Global exception handling using `@RestControllerAdvice`
- Invalid status transition handling
- Not found handling

Example error response:

```json
{
  "timestamp": "2026-04-25T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/applications",
  "fieldErrors": {
    "businessName": "must not be blank"
  }
}
```
## Workflow Summary

The system supports the following lifecycle:

APPLICATION_RECEIVED → UNDER_REVIEW → APPROVED
APPLICATION_RECEIVED → UNDER_REVIEW → REJECTED
APPLICATION_RECEIVED → UNDER_REVIEW → PENDING_PRE_SITE_RESUBMISSION
→ PRE_SITE_RESUBMITTED → UNDER_REVIEW → APPROVED

Key rules:
- Approval/Reject only allowed from UNDER_REVIEW
- Resubmission requires review to restart
- Multiple resubmission cycles are supported

## AI Usage

I used ChatGPT to help scaffold the initial Spring Boot structure, DTOs, and sample README/Postman examples.
I reviewed and adjusted the generated output manually, especially around status transition validation, role-based display status mapping, validation annotations, and exception handling.
I intentionally kept AI verification mocked because the assessment asks for pragmatic scoping rather than building a complete AI pipeline.
I discarded overly complex suggestions such as microservices and external file storage because they would reduce delivery confidence within the assessment time limit.

## Trade-offs

- Chose a modular monolith instead of microservices for faster delivery and simpler local testing.
- Stored document metadata only; actual file upload/storage can be added later.
- Used request parameter role instead of real authentication to keep the MVP focused.
- Mocked AI verification to show integration shape without overbuilding.
 
## Design Decisions

- Used Set instead of List for collections to avoid Hibernate MultipleBagFetchException
- Implemented status transition validation in service layer to enforce business rules
- Used role-based display mapping instead of exposing internal states
- Treated each submission as a new application (no duplicate restriction in MVP)
- Chose modular monolith architecture for faster delivery and simplicity

## Known Limitations

- No authentication/authorization (role passed as request param)
- No duplicate application validation
- No persistent file storage (metadata only)
- No audit trail/history table for status changes

## Testing

Basic tests are implemented to validate key application flows and status transitions.

Test coverage includes:
- Application submission
- Status transition validation (review, resubmission, approval, rejection)
- Controller endpoint validation using MockMvc
Postman collection is also provided to test end-to-end API flows manually.

## What I Would Do Next

- Add Spring Security with Officer and Operator roles.
- Add real file upload and object storage.
- Add audit/event table for every status transition.
- Add notification service using async events.
- Add full Use Case 3 site visit checklist workflow.
- Add integration tests for all key status transitions.
