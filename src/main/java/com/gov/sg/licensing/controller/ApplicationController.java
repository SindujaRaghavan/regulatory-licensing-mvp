package com.gov.sg.licensing.controller;

import com.gov.sg.licensing.dto.*;
import com.gov.sg.licensing.dto.ApplicationResponse;
import com.gov.sg.licensing.dto.ReviewRequest;
import com.gov.sg.licensing.dto.SubmitApplicationRequest;
import com.gov.sg.licensing.model.UserRole;
import com.gov.sg.licensing.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @Operation(summary = "Operator submits a new application")
    @PostMapping
    public ApplicationResponse submit(@Valid @RequestBody SubmitApplicationRequest request) {
        return service.submit(request);
    }

    @Operation(summary = "View application by role. Use role=OPERATOR or OFFICER")
    @GetMapping("/{id}")
    public ApplicationResponse get(@PathVariable Long id,
                                   @RequestParam(defaultValue = "OPERATOR") UserRole role) {
        return service.get(id, role);
    }

    @Operation(summary = "Officer starts application review")
    @PostMapping("/{id}/review/start")
    public ApplicationResponse startReview(@PathVariable Long id) {

        return service.startReview(id);
    }

    @Operation(summary = "Officer requests pre-site resubmission with contextual feedback")
    @PostMapping("/{id}/review/request-resubmission")
    public ApplicationResponse requestResubmission(@PathVariable Long id,
                                                   @Valid @RequestBody ReviewRequest request) {
        return service.requestResubmission(id, request);
    }

    @Operation(summary = "Operator resubmits only corrected information/documents")
    @PostMapping("/{id}/resubmit")
    public ApplicationResponse resubmit(@PathVariable Long id,
                                        @Valid @RequestBody SubmitApplicationRequest request) {
        return service.resubmit(id, request);
    }

    @Operation(summary = "Officer approves an application")
    @PostMapping("/{id}/approve")
    public ApplicationResponse approve(@PathVariable Long id) {

        return service.approve(id);
    }

    @Operation(summary = "Officer rejects an application")
    @PostMapping("/{id}/reject")
    public ApplicationResponse reject(@PathVariable Long id) {

        return service.reject(id);
    }
}
