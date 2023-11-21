package com.stg.b2b.bidinfo;

import com.stg.b2b.bidinfo.dto.BidInfoResponse;
import com.stg.b2b.bidinfo.dto.InterviewRequestDto;
import com.stg.b2b.bidinfo.dto.InterviewResponse;
import com.stg.b2b.dashboard.dto.BidInfoSummaryDTO;
import com.stg.b2b.dashboard.dto.BidsByReceivedDateMapper;
import com.stg.b2b.bidinfo.dto.BidInfoDto;
import com.stg.b2b.dashboard.dto.GraphResponseDto;
import com.stg.b2b.entity.BidInfo;
import com.stg.b2b.exception.BadRequestException;
import com.stg.b2b.exception.NotFoundException;
import com.stg.b2b.repository.BidInfoRepository;
import com.stg.b2b.repository.InterviewRepository;
import com.stg.b2b.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BidInfoService{
    private static final Logger logger = LoggerFactory.getLogger(BidInfoService.class);

    @Autowired
    private BidInfoRepository bidInfoRepository;

    public List<BidInfoResponse> getBidInfo(Boolean activeStatus) {
        logger.info("getBidInfo() is called in BidInfoServiceImpl.");
        return this.bidInfoRepository.getAllBids(activeStatus);

    }

    public BidInfoDto updateBidInfo(BidInfoDto bidInfoDto, String userName) {

        logger.info("updateBidInfo() is called in BidInfoTblServiceImpl");
        BidInfo bidInfo = this.bidInfoRepository.findByBidNo( bidInfoDto.getBidNo());
        if(bidInfo == null){
            throw new NotFoundException("Bid Number : " + bidInfoDto.getBidNo() + " not found.");
        }
        bidInfo.setBidOverTarget(bidInfoDto.getBidOverTarget());
        if(bidInfoDto.getBidPoNumber() != null && !bidInfoDto.getBidPoNumber().equals("")){
            bidInfo.setBidPoNumber(bidInfoDto.getBidPoNumber());
            bidInfo.setBidStatus("Shortlisted");
        }
        bidInfo.setBidExternalOrInternal(bidInfoDto.getBidExternalOrInternal());
        bidInfo.setBidSkillSet(bidInfoDto.getBidSkillSet());
        bidInfo.setBidUpdatedBy(userName);
        bidInfo.setBidUpdatedAt(Date.valueOf(LocalDate.now()));

        try {
            BidInfo updatedBidInfo = this.bidInfoRepository.save(bidInfo);
            BidInfoDto responseDto = new BidInfoDto();
            responseDto.setBidName(updatedBidInfo.getBidName());
            responseDto.setOrdBroadcastDate(updatedBidInfo.getOrder().getId().getOrdBroadcastDate());
            responseDto.setOrderNo(updatedBidInfo.getOrder().getId().getOrderNo());
            BeanUtils.copyProperties(updatedBidInfo, responseDto);
            logger.info("updateBidInfo() is completed  and BidInfoTbl details updated for  BidNumber: {} and returned updatedBidInfo.", updatedBidInfo.getBidNo());
            return responseDto;
        }catch (Exception exception){
            throw  new BadRequestException("Update failed for Bid number: " + bidInfoDto.getBidNo());
        }
    }

    public BidInfoSummaryDTO getActiveBidInfoSummary() {

        logger.debug("Entered Inside  BidInfoTblServiceImpl getBidInfoSummary() Method");


        List<BidsByReceivedDateMapper> bidInfos = bidInfoRepository.getBidsByReceivedDate(true).orElseThrow(()-> new NotFoundException("Bids  Data Not Fetched"));

        AtomicInteger today = new AtomicInteger();
        AtomicInteger thisWeek = new AtomicInteger();
        AtomicInteger thisMonth = new AtomicInteger();
        AtomicInteger previousMonth = new AtomicInteger();

        String todatDateString = String.valueOf(LocalDate.now());
        int currentMonth = DateUtils.currentMonthNumber();
        int currentWeek = DateUtils.getWeekNumber(LocalDate.now());

        bidInfos.forEach(eachBid->{
            String eachDate = String.valueOf(eachBid.getBidReceivedDate()); //Converts Each Bid Date to string

            LocalDate eachLocalDate = LocalDate.parse(eachDate); //Converts String Date to LocalDate


            int eachDateWeekOfYear = DateUtils.getWeekNumber(eachLocalDate);

            int eachWeekDay = DateUtils.getWeekDayNumber(eachLocalDate);

            if(todatDateString.equals(eachDate)){
                today.addAndGet(eachBid.getBidsCount());
            }
            if(eachLocalDate.getMonthValue() == currentMonth){
                thisMonth.addAndGet(eachBid.getBidsCount());
            }
            if((eachLocalDate.getMonthValue()) == (currentMonth - 1 )){
                previousMonth.addAndGet(eachBid.getBidsCount());
            }
            if(currentWeek == eachDateWeekOfYear && (eachWeekDay >= 1 && eachWeekDay <=6)){
                thisWeek.addAndGet(eachBid.getBidsCount());
            }

        });

        logger.debug("Exited From BidInfoTblServiceImpl getBidInfoSummary() Method");
        return new BidInfoSummaryDTO(today.intValue(), thisWeek.intValue(), thisMonth.intValue(),  previousMonth.intValue());

    }


    public BidInfoSummaryDTO getClosedBidInfoSummary() {

        logger.debug("Entered Inside  BidInfoTblServiceImpl getBidInfoSummary() Method");


        List<BidsByReceivedDateMapper> bidInfos = bidInfoRepository.getBidsByReceivedDate(false).orElseThrow(()-> new NotFoundException("Bids  Data Not Fetched"));

        AtomicInteger today = new AtomicInteger();
        AtomicInteger thisWeek = new AtomicInteger();
        AtomicInteger thisMonth = new AtomicInteger();
        AtomicInteger previousMonth = new AtomicInteger();

        String todatDateString = String.valueOf(LocalDate.now());
        int currentMonth = DateUtils.currentMonthNumber();
        int currentWeek = DateUtils.getWeekNumber(LocalDate.now());

        bidInfos.forEach(eachBid->{
            String eachDate = String.valueOf(eachBid.getBidReceivedDate()); //Converts Each Bid Date to string

            LocalDate eachLocalDate = LocalDate.parse(eachDate); //Converts String Date to LocalDate


            int eachDateWeekOfYear = DateUtils.getWeekNumber(eachLocalDate);

            int eachWeekDay = DateUtils.getWeekDayNumber(eachLocalDate);

            if(todatDateString.equals(eachDate)){
                today.addAndGet(eachBid.getBidsCount());
            }
            if(eachLocalDate.getMonthValue() == currentMonth){
                thisMonth.addAndGet(eachBid.getBidsCount());
            }
            if((eachLocalDate.getMonthValue()) == (currentMonth - 1 )){
                previousMonth.addAndGet(eachBid.getBidsCount());
            }
            if(currentWeek == eachDateWeekOfYear && (eachWeekDay >= 1 && eachWeekDay <=6)){
                thisWeek.addAndGet(eachBid.getBidsCount());
            }

        });

        logger.debug("Exited From BidInfoTblServiceImpl getBidInfoSummary() Method");
        return new BidInfoSummaryDTO(today.intValue(), thisWeek.intValue(), thisMonth.intValue(),  previousMonth.intValue());

    }


    public GraphResponseDto getBidsCountWDates(String period, String toggle) {
        LocalDate endDateLocal = LocalDate.now();
        LocalDate startDateLocal;
        switch (period){
            case "1W":
                startDateLocal = endDateLocal.minusDays(8);
                break;
            case "1M":
                startDateLocal = endDateLocal.minusDays(32);
                break;
            case "3M":
                startDateLocal = endDateLocal.minusDays(94);
                break;
            case "6M":
                startDateLocal = endDateLocal.minusDays(188);
                break;
            case "1Y":
                startDateLocal = endDateLocal.minusDays(370);
                break;
            default:
                startDateLocal = endDateLocal.minusDays(33);
                break;
        }

        java.sql.Date startDate = java.sql.Date.valueOf(startDateLocal);
        java.sql.Date endDate = Date.valueOf(endDateLocal);

        GraphResponseDto graphResponseDto = new GraphResponseDto();
        graphResponseDto.setStartDate(startDate);
        graphResponseDto.setLastDate(endDate);
        graphResponseDto.setData("all".equals(toggle)?this.bidInfoRepository.getBidsCountWDate(startDate,endDate):this.bidInfoRepository.getBidsCountWDate(startDate,endDate,"open".equals(toggle)?true:false));
        return graphResponseDto;
    }
}

