package com.gov.sg.licensing.service;

import com.gov.sg.licensing.dto.*;
import com.gov.sg.licensing.exception.InvalidStatusTransitionException;
import com.gov.sg.licensing.exception.ResourceNotFoundException;
import com.gov.sg.licensing.model.*;
import com.gov.sg.licensing.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationService {

    private final ApplicationRepository repository;
    private final ApplicationMapper mapper;

    public ApplicationService(ApplicationRepository repository, ApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public ApplicationResponse submit(SubmitApplicationRequest request) {
        Application app = new Application();
        updateApplicationFields(app, request);
        app.setStatus(ApplicationStatus.APPLICATION_RECEIVED);

        mapDocuments(app, request);
        return saveAndMap(app, UserRole.OPERATOR);
    }


    @Transactional(readOnly = true)
    public ApplicationResponse get(Long id, UserRole role) {
        return mapper.toResponse(find(id), role);
    }


    public ApplicationResponse startReview(Long id) {
        Application app = find(id);
        validateStatus(app, ApplicationStatus.APPLICATION_RECEIVED, ApplicationStatus.PRE_SITE_RESUBMITTED);

        app.setStatus(ApplicationStatus.UNDER_REVIEW);
        return saveAndMap(app, UserRole.OFFICER);
    }

    public ApplicationResponse requestResubmission(Long id, ReviewRequest request) {
        Application app = find(id);
        validateStatus(app, ApplicationStatus.UNDER_REVIEW);

        mapFeedbacks(app, request);
        app.setStatus(ApplicationStatus.PENDING_PRE_SITE_RESUBMISSION);

        return saveAndMap(app, UserRole.OFFICER);
    }

    public ApplicationResponse resubmit(Long id, SubmitApplicationRequest request) {
        Application app = find(id);
        validateStatus(app, ApplicationStatus.PENDING_PRE_SITE_RESUBMISSION);

        updateApplicationFields(app, request);
        app.setRevisionNo(app.getRevisionNo() + 1);
        app.setStatus(ApplicationStatus.PRE_SITE_RESUBMITTED);

        app.getDocuments().clear();
        mapDocuments(app, request);
        markFeedbackResolved(app);

        return saveAndMap(app, UserRole.OPERATOR);
    }

    public ApplicationResponse approve(Long id) {
        Application app = find(id);
        validateStatus(app, ApplicationStatus.UNDER_REVIEW);

        app.setStatus(ApplicationStatus.APPROVED);
        return saveAndMap(app, UserRole.OFFICER);
    }

    public ApplicationResponse reject(Long id) {
        Application app = find(id);
        validateStatus(app, ApplicationStatus.UNDER_REVIEW);

        app.setStatus(ApplicationStatus.REJECTED);
        return saveAndMap(app, UserRole.OFFICER);
    }



    private Application find(Long id) {
        return repository.findDetailedById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + id));
    }

    private ApplicationResponse saveAndMap(Application app, UserRole role) {
        return mapper.toResponse(repository.save(app), role);
    }

    private void validateStatus(Application app, ApplicationStatus... allowedStatuses) {
        for (ApplicationStatus status : allowedStatuses) {
            if (app.getStatus() == status) return;
        }
        throw new InvalidStatusTransitionException("Invalid status transition from " + app.getStatus());
    }

    private void updateApplicationFields(Application app, SubmitApplicationRequest request) {
        app.setBusinessName(request.businessName());
        app.setOperatorName(request.operatorName());
        app.setLicenceType(request.licenceType());
        app.setApplicationDetails(request.applicationDetails());
    }

    private void mapDocuments(Application app, SubmitApplicationRequest request) {
        for (DocumentRequest docReq : request.documents()) {
            ApplicationDocument doc = new ApplicationDocument();
            doc.setApplication(app);
            doc.setFileName(docReq.fileName());
            doc.setDocumentType(docReq.documentType());
            doc.setAiVerificationStatus(mockAiStatus(docReq.fileName()));
            app.getDocuments().add(doc);
        }
    }

    private void mapFeedbacks(Application app, ReviewRequest request) {
        for (FeedbackRequest feedbackRequest : request.feedbacks()) {
            OfficerFeedback feedback = new OfficerFeedback();
            feedback.setApplication(app);
            feedback.setSection(feedbackRequest.section());
            feedback.setComment(feedbackRequest.comment());
            feedback.setRevisionNo(app.getRevisionNo());
            app.getFeedbacks().add(feedback);
        }
    }

    private void markFeedbackResolved(Application app) {
        app.getFeedbacks().forEach(f -> f.setResolved(true));
    }

    private String mockAiStatus(String fileName) {
        if (fileName == null || fileName.isBlank()) return "FAILED";
        return fileName.toLowerCase().endsWith(".pdf") ? "PASSED" : "FLAGGED_NON_PDF";
    }
}
