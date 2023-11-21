package com.stg.b2b.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "candidate")
@ToString
public class Candidate {

   @EmbeddedId
   private CandidatePk candidatePk;

    @Column(name = "candidate_name", length = 250)
    private String candidateName;

    @Column(name = "candidate_rate")
    private Double candidateRate;

    @Column(name = "candidate_submission_date")
    private Date candidateSubmissionDate;

    @Column(name="candidate_active_status")
    private Boolean candidateActiveStatus;

    @Column(name="candidate_status",length = 250)
    private String candidateStatus;

    @Column(name = "candidate_declined_code",length = 250)
    private String candidateDeclinedCode;

    @Column(name = "candidate_declined_reason",length = 500)
    private String candidateDeclinedReason;

    @Column(name = "candidate_declined_date")
    private Date candidateDeclinedDate;

    @Column(name = "candidate_created_by",length = 250)
    private String candidateCreatedBy;

    @Column(name = "candidate_created_at")
    private Date candidateCreatedAt;

    @Column(name = "candidate_updated_by",length = 250)
    private String candidateUpdatedBy;

    @Column(name = "candidate_updated_at")
    private Date candidateUpdatedAt;

}
