package com.stg.b2b.candidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private static final Logger logger = LoggerFactory.getLogger(CandidateController.class);
    @Autowired
    private CandidateService candidateService;

    /**
     * returns list of Candidates
     * @return Response entity which contains list of Candidate DTO
     */
    @GetMapping()
    public ResponseEntity<List<CandidateDto>> getAllCandidates(){
        logger.info("Getting all Candidates");
        List<CandidateDto> candidates = this.candidateService.getAllCandidates();
        logger.info("Found {} candidates",candidates.size());
        return ResponseEntity.ok(candidates);
    }
}
