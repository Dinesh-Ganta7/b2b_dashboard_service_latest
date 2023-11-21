package com.stg.b2b.candidate;

import java.sql.Date;

public interface CandidateDto {
     String getCandidateName();
     Double getCandidateRate();
     Date getCandidateSubmissionDate();
     Boolean getCandidateActiveStatus();
     String getCandidateStatus();
     String getCandidateDeclinedCode();
     String GetCandidateDeclinedReason();
     Date GetCandidateDeclinedDate();
     Integer getFileId();
     Integer getOrderNo();
}
