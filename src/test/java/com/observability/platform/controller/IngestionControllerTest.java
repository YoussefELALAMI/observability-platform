package com.observability.platform.controller;

import com.observability.platform.api.controller.IngestionController;
import com.observability.platform.service.IngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngestionController.class)
public class IngestionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Our tool for firing fake HTTP requests

    @MockitoBean
    private IngestionService ingestionService; // Creates a Mockto mock AND registers it as a Spring bean

     // --- Helper method to create a valid JSON payload for testing ---
     private String validJsonPayload() {
         return """
                 {
                     "service": "payment-service",
                     "timestamp": "2024-06-01T12:00:00Z",
                     "level": "ERROR",
                     "message": "Something failed",
                     "metadata": {
                         "requestId": "req-abc-123"
                     }
                 }
                 """;
     }

     // --- Valid request → 202 Accepted ---
     @Test
     void ingest_validRequest_returnsAccepted() throws Exception {
         mockMvc.perform(
                 post("/api/v1/ingest")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(validJsonPayload()))
                 .andExpect(status().isAccepted());
     }

    // --- Invalid request → 400 Bad Request ---
    @Test
    void ingest_invalidRequest_returnsBadRequest() throws Exception {
         mockMvc.perform(
                 post("/api/v1/ingest")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content("{}")) // empty JSON is invalid because required fields are missing
                 .andExpect(status().isBadRequest());
    }

    // --- Duplicate request → 202 Accepted ---
    @Test
    void ingest_duplicateRequest_returnsAccepted() throws Exception {
         mockMvc.perform(
                 post("/api/v1/ingest")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validJsonPayload())
                    .content(validJsonPayload())) // sending the same payload twice simulates a duplicate request
                 .andExpect(status().isAccepted());
    }
}
