package com.gov.sg.licensing.dto;

import jakarta.validation.constraints.NotBlank;

public record DocumentRequest(
        @NotBlank String fileName,
        @NotBlank String documentType
) {}