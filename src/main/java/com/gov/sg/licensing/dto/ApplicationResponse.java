package com.gov.sg.licensing.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ApplicationResponse(
        Long id,
        String businessName,
        String operatorName,
        String licenceType,
        String applicationDetails,
        String internalStatus,
        String displayStatus,
        Integer revisionNo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<DocumentResponse> documents,
        List<FeedbackResponse> feedbacks
) {}
