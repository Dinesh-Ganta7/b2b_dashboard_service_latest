package com.stg.b2b.bidinfo;

import com.stg.b2b.bidinfo.dto.BidInfoDto;
import com.stg.b2b.bidinfo.dto.BidInfoResponse;
import com.stg.b2b.bidinfo.dto.InterviewRequestDto;
import com.stg.b2b.bidinfo.dto.InterviewResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("bid-info")
public class BidInfoController {
    private static final Logger logger = LoggerFactory.getLogger(BidInfoController.class);

    @Autowired
    private BidInfoService bidInfoService;

    /**
     * used to get list of  BidInfoDto based on activeStatus
     * @param activeStatus
     * @return Response entity which contains list of BidInfoDto
     */
    @GetMapping("{activeStatus}")
    public ResponseEntity<List<BidInfoResponse>> getBidInfo (@PathVariable Boolean activeStatus){
        return ResponseEntity.ok(bidInfoService.getBidInfo(activeStatus));
    }



    /**
     * used to update BidInfo by using BidInfoDto
     * @param bidInfoDto
     * @param auth
     * @return Response entity which contains updated BidInfoDto
     */
    @PutMapping()
    public ResponseEntity<BidInfoDto> updateBidInfo (@RequestBody BidInfoDto bidInfoDto, Authentication auth){
        return ResponseEntity.ok(bidInfoService.updateBidInfo(bidInfoDto, auth.getPrincipal().toString()));
    }
}
