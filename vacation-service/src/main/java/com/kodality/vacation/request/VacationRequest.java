package com.kodality.vacation.request;

import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Serdeable
@Getter
@Setter
@Accessors(chain = true)
public class VacationRequest {

    private long employeeId;
    private LocalDate vacationStart;
    private LocalDate vacationEnd;
    private String comment;
    private LocalDateTime submittedAt;
}
