package com.observability.platform.api.controller;

import com.observability.platform.api.dto.request.EventRequest;
import com.observability.platform.service.IngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ingest")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestionService ingestionService;

    @PostMapping
    public ResponseEntity<Void> ingest(@Valid @RequestBody EventRequest eventRequest){
        ingestionService.ingest(eventRequest);
        return ResponseEntity.accepted().build(); // .accepted() [=>202] and not .createdk() [=>201] because we are not creating a resource, we are just accepting the data for processing.
    }
}