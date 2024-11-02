package com.kodality.vacation.request;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Serdeable
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class VacationRequest {
  @jakarta.persistence.Id
  @Id
  private Long id;

  private String employeeName;
  private LocalDate vacationStart;
  private LocalDate vacationEnd;
  private String comment;
  private LocalDateTime submittedAt;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  // TODO: add missing fields
}
