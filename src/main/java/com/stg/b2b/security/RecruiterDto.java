package com.stg.b2b.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecruiterDto {
    private String givenName;
    private String title;
    private String mailId;
}
