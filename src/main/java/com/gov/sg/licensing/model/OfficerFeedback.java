package com.gov.sg.licensing.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "officer_feedback")
public class OfficerFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false)
    private boolean resolved = false;

    @Column(nullable = false)
    private Integer revisionNo;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() {
        return id;
    }
    public Application getApplication() {
        return application;
    }
    public void setApplication(Application application) {
        this.application = application;
    }
    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public boolean isResolved() {
        return resolved;
    }
    public void setResolved(boolean resolved) {
        this.resolved = resolved;
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
}
