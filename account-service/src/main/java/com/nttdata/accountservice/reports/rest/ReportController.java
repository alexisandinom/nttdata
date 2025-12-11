package com.nttdata.accountservice.reports.rest;

import com.nttdata.accountservice.reports.application.ReportManager;
import com.nttdata.accountservice.reports.application.dto.ReportRequestDTO;
import com.nttdata.accountservice.reports.application.dto.ReportResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Account statement reports API")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportManager reportManager;

    public ReportController(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @GetMapping("/{client-id}")
    @Operation(summary = "Generate account statement report for a client")
    public Mono<ResponseEntity<ReportResponseDTO>> generateReport(
            @PathVariable("client-id") @NotBlank String clientId,
            @RequestParam("startDate") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        logger.info("Generating report for client: {}, from: {} to: {}", clientId, startDate, endDate);
        
        ReportRequestDTO request = new ReportRequestDTO();
        request.setClientId(clientId);
        request.setStartDate(startDate);
        request.setEndDate(endDate);

        return reportManager.generateReport(request)
                .map(ResponseEntity::ok);
    }
}


