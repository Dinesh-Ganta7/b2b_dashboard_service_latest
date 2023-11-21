package com.stg.b2b.candidate;

import com.stg.b2b.repository.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);
    @Autowired
    private CandidateRepository candidateRepository;

    public List<CandidateDto> getAllCandidates() {
        logger.info("GetAllCandidates Called");
        return this.candidateRepository.getAllCandidates();
    }
}
