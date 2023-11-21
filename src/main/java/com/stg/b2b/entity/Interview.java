package com.stg.b2b.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "interview")
@ToString
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id",unique = true, nullable = false)
    private Integer interviewId;

    @Column(name = "interview_date", nullable = false)
    private LocalDateTime interviewDate;

    @Column(name = "interview_result",length = 1024)
    private String interviewResult;

    @ManyToOne
    @JoinColumn(name = "bid_no",referencedColumnName = "bid_no",   nullable=false)
    @JsonBackReference(value = "bid_interview")
    private BidInfo bidInfo;

}
