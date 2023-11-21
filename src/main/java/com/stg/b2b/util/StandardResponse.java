package com.stg.b2b.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StandardResponse {

    // Standard response for every api and error
    private Date timestamp;

    private String message;

    private String details;

    private String status;

    private Object data;
}
