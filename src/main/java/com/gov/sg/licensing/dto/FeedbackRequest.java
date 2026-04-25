package com.gov.sg.licensing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public record FeedbackRequest(
        @NotBlank @Size(max = 120) String section,
        @NotBlank @Size(max = 1000) String comment
) {}
