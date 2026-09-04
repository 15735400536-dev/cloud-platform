package com.maxinhai.platform.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserVisitLogQueryDTO {

    private String clientId;
    private LocalDate visitDate;

}
