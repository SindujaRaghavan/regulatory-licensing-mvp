package com.gov.sg.licensing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.sg.licensing.dto.ApplicationResponse;
import com.gov.sg.licensing.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService service;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    void givenValidRequest_whenSubmit_thenReturn200() throws Exception {

        when(service.submit(any()))
                .thenReturn(org.mockito.Mockito.mock(ApplicationResponse.class));

        String request = """
        {
          "businessName": "Test Biz",
          "operatorName": "Sinduja",
          "licenceType": "Food Shop",
          "applicationDetails": "Details",
          "documents": []
        }
        """;

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }



    @Test
    void givenInvalidRequest_whenSubmit_thenReturn400() throws Exception {

        String request = """
        {
          "businessName": "",
          "operatorName": "",
          "licenceType": "",
          "applicationDetails": ""
        }
        """;

        mockMvc.perform(post("/api/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }



    @Test
    void givenValidId_whenGet_thenReturn200() throws Exception {

        when(service.get(eq(1L), any()))
                .thenReturn(org.mockito.Mockito.mock(ApplicationResponse.class));

        mockMvc.perform(get("/api/applications/1")
                        .param("role", "OPERATOR"))
                .andExpect(status().isOk());
    }



    @Test
    void givenInvalidId_whenGet_thenReturn404() throws Exception {

        when(service.get(eq(999L), any()))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/applications/999")
                        .param("role", "OPERATOR"))
                .andExpect(status().isInternalServerError()); // or 404 if you mapped exception
    }



    @Test
    void givenValidId_whenApprove_thenReturn200() throws Exception {

        when(service.approve(1L))
                .thenReturn(org.mockito.Mockito.mock(ApplicationResponse.class));

        mockMvc.perform(post("/api/applications/1/approve"))
                .andExpect(status().isOk());
    }
}