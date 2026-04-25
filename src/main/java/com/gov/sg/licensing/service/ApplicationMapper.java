package com.gov.sg.licensing.service;

import com.gov.sg.licensing.dto.*;
import com.gov.sg.licensing.model.*;
import com.gov.sg.licensing.dto.ApplicationResponse;
import com.gov.sg.licensing.dto.DocumentResponse;
import com.gov.sg.licensing.dto.FeedbackResponse;
import com.gov.sg.licensing.model.Application;
import com.gov.sg.licensing.model.ApplicationDocument;
import com.gov.sg.licensing.model.OfficerFeedback;
import com.gov.sg.licensing.model.UserRole;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ApplicationMapper {
    public ApplicationResponse toResponse(Application app, UserRole role) {
        return new ApplicationResponse(
                app.getId(),
                app.getBusinessName(),
                app.getOperatorName(),
                app.getLicenceType(),
                app.getApplicationDetails(),
                app.getStatus().name(),
                StatusViewMapper.displayLabel(app.getStatus(), role),
                app.getRevisionNo(),
                app.getCreatedAt(),
                app.getUpdatedAt(),
                app.getDocuments().stream()
                        .sorted(Comparator.comparing(ApplicationDocument::getUploadedAt,Comparator.nullsLast(Comparator.naturalOrder())))
                        .map(d -> new DocumentResponse(
                                d.getId(), d.getFileName(), d.getDocumentType(), d.getAiVerificationStatus(), d.getUploadedAt()
                        ))
                        .toList(),
                app.getFeedbacks().stream()
                        .sorted(Comparator.comparing(OfficerFeedback::getCreatedAt,Comparator.nullsLast(Comparator.naturalOrder())))
                        .map(f -> new FeedbackResponse(
                                f.getId(), f.getSection(), f.getComment(), f.isResolved(), f.getRevisionNo(), f.getCreatedAt()
                        ))
                        .toList()
        );
    }
}
