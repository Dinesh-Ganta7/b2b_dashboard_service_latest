package com.stg.b2b.bidinfo;

import com.stg.b2b.bidinfo.dto.CreateInterviewDto;
import com.stg.b2b.bidinfo.dto.InterviewDto;
import com.stg.b2b.bidinfo.dto.InterviewRequestDto;
import com.stg.b2b.bidinfo.dto.InterviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("interview")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;

    /**
     * used to update interview based on interviewId for specific bid
     * @param interviewDto
     * @return Response entity which container updated InterviewDto
     */
    @PutMapping
    public ResponseEntity<InterviewDto> updateInterview(@RequestBody InterviewDto interviewDto, Authentication auth)
    {
        return ResponseEntity.ok(this.interviewService.updateInterview(interviewDto, auth.getPrincipal().toString()));
    }

    /**
     * used create Interview by using bidNo which contains createInterviewDto for specific bid
     * @param createInterviewDto
     * @return Response entity which contains InterviewDto ( After creation )
     */
    @PostMapping
    public ResponseEntity<InterviewDto> createInterview(@RequestBody CreateInterviewDto createInterviewDto, Authentication auth){
        return ResponseEntity.ok(this.interviewService.createInterview(createInterviewDto, auth.getPrincipal().toString()));
    }

    /**
     *
     * @pathVariable bidNo
     * @return Response entity which contains list of Interviews
     */
    @GetMapping("/list/{bidNo}")
    public ResponseEntity<List<InterviewResponse>> getInterviewsByBids(@PathVariable int bidNo){
        return ResponseEntity.ok(interviewService.getInterviewsByBids(bidNo));
    }
}
