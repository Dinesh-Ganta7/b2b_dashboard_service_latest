package com.stg.b2b.dashboard;


import com.stg.b2b.dashboard.dto.BidInfoSummaryDTO;
import com.stg.b2b.dashboard.dto.PositionByManagerQueryResultMapper;

import com.stg.b2b.bidinfo.BidInfoService;



import com.stg.b2b.order.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    PositionService positionService;

    @Autowired
    private BidInfoService bidInfoService;

    @Autowired
    OrderService orderService;



    @GetMapping("/openPositions")
    public ResponseEntity<Map<String,Object>> getOpenPositions(){
        Map<String, Object> positionsDataResponseMap = new HashMap<>();
        positionsDataResponseMap.put("positionsByBUData", positionService.getPositionByBusinessUnit());
        positionsDataResponseMap.put("positionsByRegionData", positionService.getPositionByRegionData());
        positionsDataResponseMap.put("positionBySkillGroupData",positionService.getPositionBySkillGroupDTO());
        positionsDataResponseMap.put("positionByType", positionService.getPositionByType());
        positionsDataResponseMap.put("openPositionsSummaryData", positionService.getOpenPositionsSummaryData());
        positionsDataResponseMap.put("closedPositionsSummaryData", positionService.getClosedPositionsSummaryData());
        positionsDataResponseMap.put("openPositionsByMonthData", positionService.getOpenPositionByMonth());
        positionsDataResponseMap.put("closedPositionsByMonthData", positionService.getClosedPositionByMonth());
        return ResponseEntity.ok(positionsDataResponseMap);

    }

    @GetMapping("/bidSubmission")
    public ResponseEntity<Map<String,BidInfoSummaryDTO>> getBidSubmissionSummary(){
        Map<String, BidInfoSummaryDTO> bidDataResponseMap = new HashMap<>();
        bidDataResponseMap.put("openBidsData",bidInfoService.getActiveBidInfoSummary() );
        bidDataResponseMap.put("closedBidsData", bidInfoService.getClosedBidInfoSummary());
        return ResponseEntity.ok(bidDataResponseMap);
    }

    @GetMapping("/orders")
    public ResponseEntity<Map<String,Object>> orderSummary(){
        Map<String,Object> ordersData = new HashMap<>();
        ordersData.put("openAndClosedOrdersCount", orderService.getOpenAndClosedOrdersCount());
        ordersData.put("ordersCountToday", orderService.getCountOfTodayOrders());
        return  ResponseEntity.ok(ordersData);
    }

    @GetMapping("/positionByManager")
    public ResponseEntity<Map<String, List<PositionByManagerQueryResultMapper>>> getPositionsByManager(){
        return ResponseEntity.ok(positionService.getPositionByManager());
    }

    @GetMapping("/orderTypeByRegion")
    public ResponseEntity<List<String>> getOrderTypeByRegion(){
        return ResponseEntity.ok(positionService.getOrderTypeByRegion());
    }

    @GetMapping("/orderTypeByBu")
    public ResponseEntity<List<String>> getOrderTypeByBusinessUnit(){
        return ResponseEntity.ok(positionService.getOrderTypeByBusinessUnit());
    }

    @GetMapping("/{period}/{entity}/{toggle}")
    public Object getGraphData(@PathVariable String period,
                               @PathVariable String entity,
                               @PathVariable String toggle){
        if("orders".equals(entity)){
            return this.orderService.getOrdersCountWDates(period,toggle);
        }else if("positions".equals(entity)){
            return this.positionService.getPositionsCountWDates(period,toggle);
        }else if("bids".equals(entity)){
            return this.bidInfoService.getBidsCountWDates(period,toggle);
        }
        return null;
    }

}
