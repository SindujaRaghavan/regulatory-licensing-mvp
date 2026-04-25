# Scope Proposal

## Chosen MVP

I implemented **Use Case 1: Operator Application Submission & Resubmission** as the primary use case.
I implemented a focused part of **Use Case 2: Officer Application Review & Feedback** to support the end-to-end resubmission flow.

## Built

- Operator submits an application with business details and document metadata.
- Mock AI verification status is generated per document.
- Officer can move an application to Under Review.
- Officer can request resubmission with section-specific comments.
- Operator can resubmit corrected application details/documents.
- Application status transitions are validated.
- Operator and Officer status labels are separated through a status view mapper.
- Exception handling and validation are implemented for key API paths.
- Swagger UI and Postman collection are included.
- The implemented workflow follows a structured status transition model supporting submission, review, 
resubmission, approval, and rejection. Detailed workflow summary is documented in README.md.

## Deferred / Mocked

- Actual drag-and-drop UI is deferred because this submission is backend-only.
- Actual file storage is mocked by storing document filename and type only.
- AI verification is mocked using simple filename rules.
- Notification delivery is deferred; in a production version this would be event-driven.
- Full Use Case 3 site visit checklist is deferred to keep scope focused.
- Authentication/authorization is not implemented; role is passed as request parameter for demo.
- Duplicate application validation is intentionally not enforced as requirements did not specify restrictions on multiple submissions.

## Assumptions

- One backend service is sufficient for this MVP.
- H2 in-memory database is acceptable for local assessment/demo.
- Operators should not see internal-only labels; display status is mapped based on role.
- Resubmission replaces current document metadata and keeps earlier feedback as audit history.

## Tech Stack / Architecture

The project uses Java 17, Spring Boot 3, Spring Web, Spring Data JPA, H2, Bean Validation, and springdoc-openapi for Swagger.
The architecture is intentionally simple:
Controller → Service → Repository → Entity. Business rules such as status transitions are placed in the service layer 
so they are testable and not mixed with HTTP logic. This keeps the MVP small while still showing
production-oriented separation of concerns.
