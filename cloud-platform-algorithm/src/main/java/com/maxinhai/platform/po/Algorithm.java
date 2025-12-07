package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Algorithm extends RecordEntity {

    private String code;
    private String name;
    private Boolean enable;

}
