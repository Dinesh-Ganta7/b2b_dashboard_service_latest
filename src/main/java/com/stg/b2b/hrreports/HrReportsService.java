package com.stg.b2b.hrreports;

import com.stg.b2b.hrreports.dto.*;


import java.util.List;


public interface HrReportsService {

    List<RecruiterReportDto> getRecruiterDetails(String recruiterName);

    List<DetailsDto> getDetails(String recruiterName);

    List<PositionBySkillDto> getPositionBySkillDetails();

    List<PositionByMonthDto> getPositionByMonthDetails();

    List<PositionByMonthDto> getAllOrdersByMonth();

    List<BidDetailsResponse> getBidReportsDetails();


}
