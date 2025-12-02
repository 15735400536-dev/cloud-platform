package com.maxinhai.platform.dto.alarm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmEditDTO {

    private String id;
    private String key;
    private String name;
    private Boolean enable;

}
