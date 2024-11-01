package com.kodality.vacation.request;

import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Serdeable
@Getter
@Setter
@Accessors(chain = true)
public class VacationRequest {
  private String employee;
  private LocalDateTime submittedAt;
  private String comment;
  // TODO: add missing fields
}
