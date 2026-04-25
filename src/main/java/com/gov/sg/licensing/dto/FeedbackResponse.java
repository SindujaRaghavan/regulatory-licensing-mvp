package com.gov.sg.licensing.dto;

import java.time.LocalDateTime;

public record FeedbackResponse(Long id, String section, String comment, boolean resolved, Integer revisionNo, LocalDateTime createdAt) {}
