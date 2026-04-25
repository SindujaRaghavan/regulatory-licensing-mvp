package com.gov.sg.licensing.dto;

import java.time.LocalDateTime;

public record DocumentResponse(Long id, String fileName, String documentType, String aiVerificationStatus, LocalDateTime uploadedAt) {}
