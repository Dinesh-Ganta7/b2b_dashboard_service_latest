package com.stg.b2b.hrreports;

import com.stg.b2b.hrreports.dto.*;
import com.stg.b2b.repository.BidInfoRepository;
import com.stg.b2b.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class HrRecruiterServiceImpl implements HrReportsService {

    @Autowired()
    PositionRepository positionRepository;

    @Autowired
    BidInfoRepository bidInfoRepository;


    /**
     * This method returns Recruiter Details
     * @return recruiterDtos
     */
    @Override
    public List<RecruiterReportDto> getRecruiterDetails(String recruiterName) {
        List<RecruiterReportDto> recruiterDtos = new ArrayList<>();
        List<Object[]> rows = this.positionRepository.findAllRecruiters(recruiterName);
        if (rows != null) {
            for (Object[] localDto : rows) {
                RecruiterReportDto recruiterDto = new RecruiterReportDto();
                recruiterDto.setRecruiter((String) localDto[2]);
                recruiterDto.setOrderCount(((Number) localDto[0]).intValue());
                recruiterDto.setNoOfPositions(((Number) localDto[1]).intValue());
                recruiterDtos.add(recruiterDto);
            }

        }
        return recruiterDtos;
    }

    /**
     * This method gives Orders and noOfPositions based on recruiterName
     * @param recruiterName
     * @return detailsDtos
     */
    @Override
    public List<DetailsDto> getDetails(String recruiterName) {
        List<DetailsDto> detailsDtos = new ArrayList<>();
        List<Object[]> rows = this.positionRepository.findDetails(recruiterName);
        if (rows != null) {
            for (Object[] localDto : rows) {
                DetailsDto detailsDto = new DetailsDto();
                detailsDto.setOrderNo(((Number) localDto[0]).intValue());
                detailsDto.setNoOfPositions(((Number) localDto[1]).intValue());
                detailsDtos.add(detailsDto);
            }

        }
        return detailsDtos;
    }


    /**
     * This method fetching Primary skills and their noOfPositions
     *
     * @return positionBySkillDtos
     */
    public List<PositionBySkillDto> getPositionBySkillDetails() {
        List<PositionBySkillDto> positionBySkillDtos = new ArrayList<>();
        List<String> skillGroups = new ArrayList<>();
        List<Object[]> rows = this.positionRepository.findPositionBySkill();
        if (rows != null) {
            for (Object[] localDto : rows) {
                PositionBySkillDto positionBySkillDto = new PositionBySkillDto();
                List<SkillGroupResponseDto> skillSetList = new ArrayList<>();
                int sumOfPositions = 0;
                if (!skillGroups.contains(localDto[0])) {
                    skillGroups.add(((String) localDto[0]));

                    for (Object[] skillset : rows) {

                        if ((skillset[0]).equals(localDto[0])) {

                            SkillGroupResponseDto skillGroup = new SkillGroupResponseDto((String) skillset[1], ((Number) (skillset[2]!=null?skillset[2]:0)).intValue());
                            skillSetList.add(skillGroup);
                            sumOfPositions += ((Number) (skillset[2]!=null?skillset[2]:0)).intValue();
                        }
                    }
                    positionBySkillDto.setSkillGroup(((String) localDto[0]));
                    positionBySkillDto.setSkillSet(skillSetList);
                    positionBySkillDto.setTotalPositions(sumOfPositions);
                    positionBySkillDtos.add(positionBySkillDto);
                }


            }
        }
        return positionBySkillDtos;
    }

    /**
     * This method fetching count of Active orders and positions based on month
     *
     * @return PositionByMonthDto
     */
    @Override
    public List<PositionByMonthDto> getPositionByMonthDetails() {
        return this.positionRepository.findPositionByMonth();
    }

    /**
     * This method fetching Count of All orders and positions based on month
     *
     * @return PositionByMonthDto
     */
    @Override
    public List<PositionByMonthDto> getAllOrdersByMonth() {
        return this.positionRepository.findOrdersByMonth();
    }


    /**
     * This method fetching details of active Bids
     *
     * @return BidDetailsResponse
     */
    @Override
    public List<BidDetailsResponse> getBidReportsDetails() {
        return this.bidInfoRepository.findBidDetails();
    }


}


