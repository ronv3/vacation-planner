package com.org.vacation.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private long id;
    private long employeeId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate vacationStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate vacationEnd;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime submittedAt;
}
