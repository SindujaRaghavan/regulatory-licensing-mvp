package com.gov.sg.licensing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_documents")
public class ApplicationDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String aiVerificationStatus;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }
    public Application getApplication() {
        return application;
    }
    public void setApplication(Application application) {
        this.application = application;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    public String getAiVerificationStatus() {
        return aiVerificationStatus;
    }
    public void setAiVerificationStatus(String aiVerificationStatus) {
        this.aiVerificationStatus = aiVerificationStatus;
    }
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}
