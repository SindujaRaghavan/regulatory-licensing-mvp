package com.gov.sg.licensing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SubmitApplicationRequest(

        @NotBlank @Size(max = 120)
        String businessName,

        @NotBlank @Size(max = 120)
        String operatorName,

        @NotBlank @Size(max = 120)
        String licenceType,

        @NotBlank @Size(max = 1000)
        String applicationDetails,

        @Valid
        List<DocumentRequest> documents

) {

    public SubmitApplicationRequest {
        if (documents == null) {
            documents = List.of();
        }
    }
}