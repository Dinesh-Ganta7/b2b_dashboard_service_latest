package com.stg.b2b.bidinfo;

import com.stg.b2b.bidinfo.dto.CreateInterviewDto;
import com.stg.b2b.bidinfo.dto.InterviewDto;
import com.stg.b2b.bidinfo.dto.InterviewRequestDto;
import com.stg.b2b.bidinfo.dto.InterviewResponse;
import com.stg.b2b.entity.BidInfo;
import com.stg.b2b.entity.Interview;
import com.stg.b2b.exception.BadRequestException;
import com.stg.b2b.exception.NotFoundException;
import com.stg.b2b.repository.BidInfoRepository;
import com.stg.b2b.repository.InterviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class InterviewService {

    private static final Logger logger = LoggerFactory.getLogger(InterviewService.class);
    @Autowired
    private InterviewRepository interviewRepository;
    @Autowired
    private BidInfoRepository bidInfoRepository;
    public InterviewDto updateInterview(InterviewDto interviewDto, String userName) {
        logger.info("updateInterview() is called in InterviewServiceImpl");

        Interview interview = this.interviewRepository.findById(interviewDto.getInterviewId()).orElseThrow(() -> new NotFoundException("Interview is not scheduled on " + interviewDto.getInterviewDate() + ". So can update."));
        interview.setInterviewDate(interviewDto.getInterviewDate());
        interview.setInterviewResult(interviewDto.getInterviewResult());
        interview.getBidInfo().setBidUpdatedBy(userName);
        interview.getBidInfo().setBidUpdatedAt(Date.valueOf(LocalDate.now()));
        Interview updateInterview = this.interviewRepository.save(interview);
        InterviewDto updatedInterviewDto = new InterviewDto();
        BeanUtils.copyProperties(updateInterview, updatedInterviewDto);
        logger.info("updateInterview() is completed  and Interview details updated for  BidNumber: {} and returned updatedBidInfo.", interview.getBidInfo().getBidNo());

        return updatedInterviewDto;
    }

    public InterviewDto createInterview(CreateInterviewDto createInterviewDto, String userName) {
        logger.info("createInterview() is called in InterviewServiceImpl");
        BidInfo bidInfo = bidInfoRepository.findByBidNo(createInterviewDto.getBidNo());
        if(bidInfo == null){
            throw new NotFoundException("Bid is not found, please provide valid bid details.");
        }

        Interview interview = new Interview();
        bidInfo.setBidUpdatedBy(userName);
        bidInfo.setBidUpdatedAt(Date.valueOf(LocalDate.now()));
        interview.setInterviewDate(createInterviewDto.getInterviewDate());
        interview.setInterviewResult(createInterviewDto.getInterviewResult());
        interview.setBidInfo(bidInfo);

        try {
            Interview createdInterview =  this.interviewRepository.save(interview);
            InterviewDto interviewDto = new InterviewDto();
            BeanUtils.copyProperties(createdInterview, interviewDto);
            logger.info("createInterview() is completed  and Interview details created  for  BidNumber: {} and returned updatedBidInfo.", createdInterview.getBidInfo().getBidNo());

            return interviewDto;
        }catch (Exception exception){
            throw  new BadRequestException("Enter valid interview details.");
        }

    }

    public List<InterviewResponse> getInterviewsByBids(int bid_no){
        return interviewRepository.getAllInterviewsByBids(bid_no);
    }
}
