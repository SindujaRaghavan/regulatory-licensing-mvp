package com.gov.sg.licensing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String businessName;

    @Column(nullable = false, length = 120)
    private String operatorName;

    @Column(nullable = false, length = 120)
    private String licenceType;

    @Column(length = 1000)
    private String applicationDetails;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 60)
    private ApplicationStatus status = ApplicationStatus.APPLICATION_RECEIVED;

    @Column(nullable = false)
    private Integer revisionNo = 1;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApplicationDocument> documents = new HashSet<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OfficerFeedback> feedbacks = new HashSet<>();

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public String getBusinessName() {

        return businessName;
    }
    public void setBusinessName(String businessName) {

        this.businessName = businessName;
    }
    public String getOperatorName() {

        return operatorName;
    }
    public void setOperatorName(String operatorName) {

        this.operatorName = operatorName;
    }
    public String getLicenceType() {

        return licenceType;
    }
    public void setLicenceType(String licenceType) {

        this.licenceType = licenceType;
    }
    public String getApplicationDetails() {

        return applicationDetails;
    }
    public void setApplicationDetails(String applicationDetails) {

        this.applicationDetails = applicationDetails;
    }
    public ApplicationStatus getStatus() {

        return status;
    }
    public void setStatus(ApplicationStatus status) {

        this.status = status;
    }
    public Integer getRevisionNo() {

        return revisionNo;
    }
    public void setRevisionNo(Integer revisionNo) {

        this.revisionNo = revisionNo;
    }
    public LocalDateTime getCreatedAt() {

        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {

        return updatedAt;
    }
    public Set<ApplicationDocument> getDocuments() {

        return documents;
    }
    public Set<OfficerFeedback> getFeedbacks() {
        return feedbacks;
    }
}
