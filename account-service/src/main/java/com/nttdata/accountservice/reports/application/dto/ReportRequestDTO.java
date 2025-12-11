package com.nttdata.accountservice.reports.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ReportRequestDTO {
    @NotNull(message = "Client identification cannot be null")
    @NotBlank(message = "Client identification cannot be empty")
    @Length(min = 1, max = 13, message = "Client identification must be between 1 and 13 characters")
    private String clientId;

    @NotNull(message = "Start date cannot be null")
    @PastOrPresent(message = "Start date must be today or a past date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @PastOrPresent(message = "End date must be today or a past date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}


