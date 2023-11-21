package com.stg.b2b.bidinfo.dto;

import java.time.LocalDateTime;

public interface InterviewResponse {

     Integer getInterviewId();
     LocalDateTime getInterviewDate();
     String getInterviewResult();
}
