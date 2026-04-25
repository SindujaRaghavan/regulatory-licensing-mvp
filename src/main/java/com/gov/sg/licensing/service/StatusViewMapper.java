package com.gov.sg.licensing.service;

import com.gov.sg.licensing.model.ApplicationStatus;
import com.gov.sg.licensing.model.UserRole;

public final class StatusViewMapper {
    private StatusViewMapper() {}

    public static String displayLabel(ApplicationStatus status, UserRole role) {
        if (role == UserRole.OPERATOR) {
            return switch (status) {
                case APPLICATION_RECEIVED -> "Submitted";
                case UNDER_REVIEW -> "Under Review";
                case PENDING_PRE_SITE_RESUBMISSION -> "Pending Pre-Site Resubmission";
                case PRE_SITE_RESUBMITTED -> "Pre-Site Resubmitted";
                case APPROVED -> "Approved";
                case REJECTED -> "Rejected";
            };
        }
        return switch (status) {
            case APPLICATION_RECEIVED -> "Application Received";
            case UNDER_REVIEW -> "Under Review";
            case PENDING_PRE_SITE_RESUBMISSION -> "Pending Pre-Site Resubmission";
            case PRE_SITE_RESUBMITTED -> "Pre-Site Resubmitted";
            case APPROVED -> "Approved";
            case REJECTED -> "Rejected";
        };
    }
}
