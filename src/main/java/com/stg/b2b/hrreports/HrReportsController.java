package com.stg.b2b.hrreports;


import com.stg.b2b.hrreports.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;


@RestController
@RequestMapping("reports")
public class HrReportsController {
    @Autowired
    HrReportsService hrReportsService;


    /**
     * Retrieves a List of all Recruiter Details
     *
     * @return Response entity which contains list of recruiter Details
     */
    @GetMapping("recruiterDetails")
    public ResponseEntity<List<RecruiterReportDto>> getRecruiterDetails(String recruiterName){
        return ResponseEntity.ok(this.hrReportsService.getRecruiterDetails(recruiterName));
    }

    /**
     * Retrieves a list of orders and their noOfPositions by recruiter name
     *
     * @param recruiterName
     * @return Response entity which contains list of orders and their noOfPositions
     */
    @GetMapping("positionDetails/{recruiterName}")
    public ResponseEntity<List<DetailsDto>> getDetails(@PathVariable String recruiterName){
        return ResponseEntity.ok(this.hrReportsService.getDetails(recruiterName));
    }

    /**
     * Retrieves a list of Primary skills and their noOfPositions
     *
     * @return Response entity which contains list of Primary skills and their noOfPositions
     */
    @GetMapping("positionBySkillDetails")
    public ResponseEntity<List<PositionBySkillDto>> getPositionBySkillDetails(){
        return ResponseEntity.ok(this.hrReportsService.getPositionBySkillDetails());
    }


    /**
     * Retrieves a list of Count of Active Orders and Positions based on month
     *
     * @return Response entity which contains list of Count of Active Orders and Positions based on month
     */
    @GetMapping("positionByMonth")
    public ResponseEntity<List<PositionByMonthDto>> getPositionByMonthDetails(){
        return ResponseEntity.ok(this.hrReportsService.getPositionByMonthDetails());
    }

    /**
     * Retrieves a list of Count of All Orders and Positions based on month
     *
     * @return Response entity which contains list of Count of All Orders and Positions based on month
     */
    @GetMapping("allOrdersByMonth")
    public ResponseEntity<List<PositionByMonthDto>> getAllOrdersByMonthDetails(){
        return ResponseEntity.ok(this.hrReportsService.getAllOrdersByMonth());
    }

    /**
     * Retrieves a list of Bid Details by using BidDetailsResponse
     *
     * @return Response entity which contains list of Bid Details by using BidDetailsResponse
     */
    @GetMapping("bidDetails")
    public ResponseEntity<List<BidDetailsResponse>> getBidReportsDetails(){
        return ResponseEntity.ok(this.hrReportsService.getBidReportsDetails());
    }


}
