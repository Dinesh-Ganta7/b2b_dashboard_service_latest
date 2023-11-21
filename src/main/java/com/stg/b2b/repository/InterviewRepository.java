package com.stg.b2b.repository;

import com.stg.b2b.bidinfo.dto.InterviewResponse;
import com.stg.b2b.entity.Interview;
import com.stg.b2b.util.QueryConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
public interface InterviewRepository extends JpaRepository<Interview, Integer>{

    @Query(value = QueryConstants.GET_INTERVIEWS_QUERY, nativeQuery = true)
    List<InterviewResponse> getAllInterviewsByBids(int bid_no);
}
