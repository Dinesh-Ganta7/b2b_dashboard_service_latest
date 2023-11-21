package com.stg.b2b.repository;

import com.stg.b2b.candidate.CandidateDto;
import com.stg.b2b.entity.Candidate;
import com.stg.b2b.entity.CandidatePk;
import com.stg.b2b.util.QueryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, CandidatePk> {
    @Query(value = "SELECT * FROM candidate c WHERE c.candidate_file_id = :fileId and c.order_no = :orderNo and c.ord_broadcast_date =:ordBroadcastDate",nativeQuery = true)
    Candidate findCandidateByOrderAndFileId(int fileId, Integer orderNo, Date ordBroadcastDate);

    @Query(value = QueryConstants.GET_CANDIDATES_QUERY, nativeQuery = true)
    List<CandidateDto> getAllCandidates();
}
