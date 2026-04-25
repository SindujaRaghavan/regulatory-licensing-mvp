package com.gov.sg.licensing;

import com.gov.sg.licensing.dto.*;
import com.gov.sg.licensing.exception.InvalidStatusTransitionException;
import com.gov.sg.licensing.model.*;
import com.gov.sg.licensing.repository.ApplicationRepository;
import com.gov.sg.licensing.service.ApplicationMapper;
import com.gov.sg.licensing.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository repository;

    @Mock
    private ApplicationMapper mapper;

    @InjectMocks
    private ApplicationService service;



    @Test
    void givenValidRequest_whenSubmit_thenApplicationCreatedWithReceivedStatus() {
        // Given
        SubmitApplicationRequest request = new SubmitApplicationRequest(
                "Biz",
                "Sinduja",
                "Food Shop",
                "Details",
                List.of(new DocumentRequest("doc.pdf", "LICENSE"))
        );

        Application savedApp = new Application();
        savedApp.setStatus(ApplicationStatus.APPLICATION_RECEIVED);

        when(repository.save(any())).thenReturn(savedApp);
        when(mapper.toResponse(any(), eq(UserRole.OPERATOR)))
                .thenReturn(mock(ApplicationResponse.class));


        ApplicationResponse response = service.submit(request);


        assertNotNull(response);
        verify(repository).save(any());
    }



    @Test
    void givenApplicationReceived_whenStartReview_thenStatusBecomesUnderReview() {
        // Given
        Application app = new Application();
        app.setStatus(ApplicationStatus.APPLICATION_RECEIVED);

        when(repository.findDetailedById(1L)).thenReturn(Optional.of(app));
        when(repository.save(any())).thenReturn(app);
        when(mapper.toResponse(any(), eq(UserRole.OFFICER)))
                .thenReturn(mock(ApplicationResponse.class));


        service.startReview(1L);


        assertEquals(ApplicationStatus.UNDER_REVIEW, app.getStatus());
    }

    @Test
    void givenInvalidStatus_whenStartReview_thenThrowException() {

        Application app = new Application();
        app.setStatus(ApplicationStatus.APPROVED);

        when(repository.findDetailedById(1L)).thenReturn(Optional.of(app));


        assertThrows(InvalidStatusTransitionException.class,
                () -> service.startReview(1L));
    }



    @Test
    void givenUnderReview_whenRequestResubmission_thenStatusPendingResubmission() {

        Application app = new Application();
        app.setStatus(ApplicationStatus.UNDER_REVIEW);
        app.setRevisionNo(1);

        ReviewRequest request = new ReviewRequest(
                List.of(new FeedbackRequest("Section1", "Fix this"))
        );

        when(repository.findDetailedById(1L)).thenReturn(Optional.of(app));
        when(repository.save(any())).thenReturn(app);
        when(mapper.toResponse(any(), eq(UserRole.OFFICER)))
                .thenReturn(mock(ApplicationResponse.class));


        service.requestResubmission(1L, request);


        assertEquals(ApplicationStatus.PENDING_PRE_SITE_RESUBMISSION, app.getStatus());
        assertFalse(app.getFeedbacks().isEmpty());
    }



    @Test
    void givenPendingResubmission_whenResubmit_thenStatusResubmittedAndRevisionIncremented() {

        Application app = new Application();
        app.setStatus(ApplicationStatus.PENDING_PRE_SITE_RESUBMISSION);
        app.setRevisionNo(1);

        SubmitApplicationRequest request = new SubmitApplicationRequest(
                "Updated Biz",
                "Sinduja",
                "Food Shop",
                "Updated details",
                List.of(new DocumentRequest("doc.pdf", "LICENSE"))
        );

        when(repository.findDetailedById(1L)).thenReturn(Optional.of(app));
        when(repository.save(any())).thenReturn(app);
        when(mapper.toResponse(any(), eq(UserRole.OPERATOR)))
                .thenReturn(mock(ApplicationResponse.class));


        service.resubmit(1L, request);


        assertEquals(ApplicationStatus.PRE_SITE_RESUBMITTED, app.getStatus());
        assertEquals(2, app.getRevisionNo());
    }



    @Test
    void givenUnderReview_whenApprove_thenStatusApproved() {
        // Given
        Application app = new Application();
        app.setStatus(ApplicationStatus.UNDER_REVIEW);

        when(repository.findDetailedById(1L)).thenReturn(Optional.of(app));
        when(repository.save(any())).thenReturn(app);
        when(mapper.toResponse(any(), eq(UserRole.OFFICER)))
                .thenReturn(mock(ApplicationResponse.class));


        service.approve(1L);


        assertEquals(ApplicationStatus.APPROVED, app.getStatus());
    }

    @Test
    void givenInvalidStatus_whenApprove_thenThrowException() {

        Application app = new Application();
        app.setStatus(ApplicationStatus.APPLICATION_RECEIVED);

        when(repository.findDetailedById(1L)).thenReturn(Optional.of(app));


        assertThrows(InvalidStatusTransitionException.class,
                () -> service.approve(1L));
    }



    @Test
    void givenUnderReview_whenReject_thenStatusRejected() {

        Application app = new Application();
        app.setStatus(ApplicationStatus.UNDER_REVIEW);

        when(repository.findDetailedById(1L)).thenReturn(Optional.of(app));
        when(repository.save(any())).thenReturn(app);
        when(mapper.toResponse(any(), eq(UserRole.OFFICER)))
                .thenReturn(mock(ApplicationResponse.class));


        service.reject(1L);


        assertEquals(ApplicationStatus.REJECTED, app.getStatus());
    }
}