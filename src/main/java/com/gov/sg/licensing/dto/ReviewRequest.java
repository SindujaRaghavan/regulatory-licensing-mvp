package com.gov.sg.licensing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ReviewRequest(
        @NotEmpty @Valid List<FeedbackRequest> feedbacks
) {}