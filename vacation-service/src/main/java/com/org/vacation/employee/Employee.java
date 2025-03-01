package com.org.vacation.employee;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Serdeable
@Getter
@Setter
@Accessors(chain = true)
public class Employee {

    private Long Id;
    private String employeeName;
    private int remainingVacationDays;

}
