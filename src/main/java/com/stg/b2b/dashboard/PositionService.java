package com.stg.b2b.dashboard;

import com.stg.b2b.dashboard.dto.*;
import com.stg.b2b.repository.*;
import com.stg.b2b.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class PositionService {

    private static final Logger logger = LoggerFactory.getLogger(PositionService.class);


    @Autowired
    PositionRepository positionTblRepository;

    @Autowired
    MasterRepository masterRepository;

    @Autowired
    BusinessUnitRepository businessUnitRepository;


    @Autowired
    OrderRepository orderTblRepository;

    @Autowired
    BidInfoRepository bidInfoTblRepository;


    /**
     * This method getPositionByRegionData() returns Positions data  by Region and Position Type
     *
     * @return List<PositionByRegionDto>
     */
    public List<PositionByRegionDto> getPositionByRegionData() {
        List<String> regionNames = positionTblRepository.getAllRegionNames().orElseThrow(()-> new NotFoundException("Region names data not found"));

        List<PositionByRegionDto> positionByRegionDtos = new ArrayList<>(); //Create Empty PositionByRegionDto

        List<PositionCountByRegionAndType> positionsCountByRegion = positionTblRepository.getPositionsCountByRegion().orElseThrow(() -> new NotFoundException("Something Went Wrong getting positionsCountByRegion Data"));

        logger.info("Loaded positionsCountByRegion Data");


        regionNames.forEach((eachRegion)->{
            PositionByRegionDto positionByRegionDto = new PositionByRegionDto();
            positionByRegionDto.setRegion(eachRegion);
            HashMap<String,Integer> orderTypeCount = new HashMap<>();
            AtomicInteger grandTotal = new AtomicInteger();
            positionsCountByRegion.forEach(eachResult -> {

                if (eachRegion.equalsIgnoreCase(eachResult.getRegion())) {
                    if (orderTypeCount.containsKey(eachResult.getPositionType())) {
                        orderTypeCount.put(
                                eachResult.getPositionType(),
                                orderTypeCount.get(eachResult.getPositionType()) + eachResult.getNoOfVacancy()
                        );
                    } else {
                        orderTypeCount.put(eachResult.getPositionType(), eachResult.getNoOfVacancy());
                    }
                    grandTotal.addAndGet(eachResult.getNoOfVacancy());
                }

                positionByRegionDto.setOrderTypeCount(orderTypeCount);
                positionByRegionDto.setGrandTotal(grandTotal.intValue());



            });
            if(positionByRegionDto.getGrandTotal()>0){
                positionByRegionDtos.add(positionByRegionDto);
            }

        });


        return positionByRegionDtos;
    }


    /**
     * This method getPositionByBusinessUnit() returns Positions data  by Business Unit and Position Type
     *
     * @return List<PositionByBUDTO>
     */
    public List<PositionByBUDTO> getPositionByBusinessUnit() {
        List<String> buNames = positionTblRepository.getAllBuNames().orElseThrow(()-> new NotFoundException("Region names data not found"));

        List<PositionByBUDTO> positionByBUDTOS = new ArrayList<>(); //Create empty Response Position BY BU DTO List

        List<PositionCountByBUAndType> positionsCountByBU = positionTblRepository.getPositionsCountByBU().orElseThrow(() -> new NotFoundException("Something went wrong getting positions by BU data")); //Get Position by BU and Type from positions db
        logger.info("Loaded Positions By BU Count Data");

        buNames.forEach((buName)->{
            PositionByBUDTO positionByBUDTO = new PositionByBUDTO();
            positionByBUDTO.setBusinessUnit(buName);
            HashMap<String,Integer> orderTypeCount = new HashMap<>();
            AtomicInteger grandTotal = new AtomicInteger();
            positionsCountByBU.forEach(eachResult -> {

                if (buName.equalsIgnoreCase(eachResult.getBusinessLineName())) {
                    if (orderTypeCount.containsKey(eachResult.getPositionType())) {
                        orderTypeCount.put(
                                eachResult.getPositionType(),
                                orderTypeCount.get(eachResult.getPositionType()) + eachResult.getNoOfVacancy()
                        );
                    } else {
                        orderTypeCount.put(eachResult.getPositionType(), eachResult.getNoOfVacancy());
                    }
                    grandTotal.addAndGet(eachResult.getNoOfVacancy());
                }

                positionByBUDTO.setOrderTypeCount(orderTypeCount);
                positionByBUDTO.setGrandTotal(grandTotal.intValue());



            });
            if(positionByBUDTO.getGrandTotal()>0){
                positionByBUDTOS.add(positionByBUDTO);
            }

        });

        return positionByBUDTOS;

    }


    /**
     * This method getPositionBySkillGroupDTO() returns Positions data  by Skill Group
     *
     * @return List<PositionBySkillGroupDTO>
     */
    public List<PositionBySkillGroupDTO> getPositionBySkillGroupDTO() {
        List<PositionBySkillGroupDTO> positionsCountBySkillGroup = positionTblRepository.getPositionsCountBySkillGroup().orElseThrow(() -> new NotFoundException("Positions By SKill Group not fetched"));
        logger.info("Loaded positionsCountBySkillGroup Data");
        return positionsCountBySkillGroup;
    }


    /**
     * This method getPositionByManager() returns Positions data  by Manager
     *
     * @return Map<String, List < PositionByManagerQueryResultMapper>>
     */
    public Map<String, List<PositionByManagerQueryResultMapper>> getPositionByManager() {

        List<String> managerTypes = new ArrayList<>(Arrays.asList("ll6_manager", "ll5_manager", "ll4_manager", "ll3_manager", "ll2_manager"));
        Map<String, List<PositionByManagerQueryResultMapper>> positionByManagersResult = new HashMap<>();
        managerTypes.forEach(eachManagerType -> {
            if (eachManagerType.equals("ll6_manager")) {
                List<PositionByManagerQueryResultMapper> ll6Positions = positionTblRepository.getPositionByManager(eachManagerType).orElseThrow(() -> new NotFoundException("ll6 Positions Data Not Fetched"));
                positionByManagersResult.put("ll6", ll6Positions);
            } else if (eachManagerType.equals("ll5_manager")) {
                List<PositionByManagerQueryResultMapper> ll5Positions = positionTblRepository.getPositionByManager(eachManagerType).orElseThrow(() -> new NotFoundException("ll5 Positions Data Not Fetched"));
                positionByManagersResult.put("ll5", ll5Positions);
            } else if (eachManagerType.equals("ll4_manager")) {
                List<PositionByManagerQueryResultMapper> ll4Positions = positionTblRepository.getPositionByManager(eachManagerType).orElseThrow(() -> new NotFoundException("ll4 Positions Data Not Fetched"));
                positionByManagersResult.put("ll4", ll4Positions);
            } else if (eachManagerType.equals("ll3_manager")) {
                List<PositionByManagerQueryResultMapper> ll3Positions = positionTblRepository.getPositionByManager(eachManagerType).orElseThrow(() -> new NotFoundException("ll3 Positions Data Not Fetched"));
                positionByManagersResult.put("ll3", ll3Positions);
            } else if (eachManagerType.equals("ll2_manager")) {
                List<PositionByManagerQueryResultMapper> ll2Positions = positionTblRepository.getPositionByManager(eachManagerType).orElseThrow(() -> new NotFoundException("ll2 Positions Data Not Fetched"));
                positionByManagersResult.put("ll2", ll2Positions);
            }

        });
        logger.info("Loaded All Managers Data");
        return positionByManagersResult;
    }


    /**
     * This method getPositionByType() returns Positions data  by Position Type
     *
     * @return List<PositionsTypeDTO>
     */
    public List<PositionsTypeDTO> getPositionByType() {
        List<PositionsTypeDTO> positionsByTypeResult = positionTblRepository.findPositionsByType().orElseThrow(() -> new NotFoundException("Position By Type Data Not Fetched"));
        ;
        return positionsByTypeResult;
    }


    /**
     * .orElseThrow(()->new NotFoundException("Open Positions By Position Type Data  not fetched"));
     * logger.info("Positions By Type Result Data Loaded");
     * This method getOpenPositionsSummaryData() returns Positions Summary Data
     *
     * @return OpenPositionsSummaryDTO
     */
    public OpenPositionsSummaryDTO getOpenPositionsSummaryData() {

        int allOpenPositions = 0;
        int allClosedPositions = 0;
        int totalActiveBids = 0;
        int closedBidsCount = 0;

        AtomicInteger today = new AtomicInteger();
        AtomicInteger thisWeek = new AtomicInteger();
        AtomicInteger thisMonth = new AtomicInteger();
        AtomicInteger previousMonth = new AtomicInteger();

        allOpenPositions = positionTblRepository.findPositionsCount(true).orElseThrow(() -> new NotFoundException("Open Positions Count Data Not Fetched"));
        logger.info("Open Positions Count Data");

        allClosedPositions = positionTblRepository.findPositionsCount(false).orElseThrow(() -> new NotFoundException("Closed Positions Count Data NOt Fetched"));

        totalActiveBids = bidInfoTblRepository.countByBidActiveStatus(true).orElseThrow(() -> new NotFoundException("Active Bids Count Data Not Found"));
        logger.info("Active Bids Count Data Loaded");

        closedBidsCount = bidInfoTblRepository.countByBidActiveStatus(false).orElseThrow(() -> new NotFoundException("Closed Bids Count Data Not Found"));
        logger.info("Closed Bids Count Data Loaded");

        List<Integer> sourcing = bidInfoTblRepository.getSourcingPositionCount().orElseThrow(() -> new NotFoundException("Sourcing Positions Data not Fetched"));
        logger.info("Sourcing Positions Data Loaded");

        List<Integer> notSourcing = bidInfoTblRepository.getNonSourcingPositionCount().orElseThrow(() -> new NotFoundException("Not Sourcing Positions Data not Fetched"));
        logger.info("Not Sourcing Positions Data Fetched");


        String todayDateString = String.valueOf(LocalDate.now());
        int currentMonth = DateUtils.currentMonthNumber();
        int currentWeek = DateUtils.getWeekNumber(LocalDate.now());

        List<OpenPositionCountByBroadcastDateMapper> positionsCountByBroadcastDate = positionTblRepository.findPositionsCountByBroadcastDate(true).orElseThrow(() -> new NotFoundException("positionsCountByBroadcastDate Data not Fetched"));
        logger.info("PositionsCountByBroadcastDate Data Fetched");

        positionsCountByBroadcastDate.forEach(eachPositionCountByDate -> {
            String eachDate = String.valueOf(eachPositionCountByDate.getBroadcastDate());
            LocalDate eachLocalDate = LocalDate.parse(eachDate);

            int eachDateWeekOfYear = DateUtils.getWeekNumber(eachLocalDate);

            int eachWeekDay = DateUtils.getWeekDayNumber(eachLocalDate);


            if (todayDateString.equals(eachDate)) {
                today.addAndGet(eachPositionCountByDate.getPositionsCount());
            }
            if (eachLocalDate.getMonthValue() == currentMonth) {
                thisMonth.addAndGet(eachPositionCountByDate.getPositionsCount());
            }
            if ((eachLocalDate.getMonthValue()) == (currentMonth - 1)) {
                previousMonth.addAndGet(eachPositionCountByDate.getPositionsCount());
            }
            if (currentWeek == eachDateWeekOfYear && (eachWeekDay >= 1 && eachWeekDay <= 6)) {
                thisWeek.addAndGet(eachPositionCountByDate.getPositionsCount());
            }

        });


        return new OpenPositionsSummaryDTO(allOpenPositions, allClosedPositions, totalActiveBids, closedBidsCount, sourcing, notSourcing, today.intValue(), thisWeek.intValue(), thisMonth.intValue(), previousMonth.intValue());
    }


    public ClosedPositionsSummaryDTO getClosedPositionsSummaryData() {


        int closedPositionsCount;
        AtomicInteger today = new AtomicInteger();
        AtomicInteger thisWeek = new AtomicInteger();
        AtomicInteger thisMonth = new AtomicInteger();
        AtomicInteger previousMonth = new AtomicInteger();


        closedPositionsCount = positionTblRepository.findPositionsCount(false).orElseThrow(() -> new NotFoundException("Closed Positions Count Data Not Fetched"));
        ;

        System.out.println(closedPositionsCount);
        String todayDateString = String.valueOf(LocalDate.now());
        int currentMonth = DateUtils.currentMonthNumber();
        int currentWeek = DateUtils.getWeekNumber(LocalDate.now());

        List<OpenPositionCountByBroadcastDateMapper> positionsCountByBroadcastDate = positionTblRepository.findPositionsCountByBroadcastDate(false).orElseThrow(() -> new NotFoundException("positionsCountByBroadcastDate Data not Fetched"));
        logger.info("PositionsCountByBroadcastDate Data Fetched");

        positionsCountByBroadcastDate.forEach(eachPositionCountByDate -> {
            String eachDate = String.valueOf(eachPositionCountByDate.getBroadcastDate());
            LocalDate eachLocalDate = LocalDate.parse(eachDate);

            int eachDateWeekOfYear = DateUtils.getWeekNumber(eachLocalDate);

            int eachWeekDay = DateUtils.getWeekDayNumber(eachLocalDate);


            if (todayDateString.equals(eachDate)) {
                today.addAndGet(eachPositionCountByDate.getPositionsCount());
            }
            if (eachLocalDate.getMonthValue() == currentMonth) {
                thisMonth.addAndGet(eachPositionCountByDate.getPositionsCount());
            }
            if ((eachLocalDate.getMonthValue()) == (currentMonth - 1)) {
                previousMonth.addAndGet(eachPositionCountByDate.getPositionsCount());
            }
            if (currentWeek == eachDateWeekOfYear && (eachWeekDay >= 1 && eachWeekDay <= 6)) {
                thisWeek.addAndGet(eachPositionCountByDate.getPositionsCount());
            }

        });


        return new ClosedPositionsSummaryDTO(today.intValue(), thisWeek.intValue(), thisMonth.intValue(), previousMonth.intValue(), closedPositionsCount);
    }


    public List<PositionsByMonthMapper> getOpenPositionByMonth() {
        PositionByMonthDto openPositionByMonth = new PositionByMonthDto();
        List<PositionsByMonthMapper> result = positionTblRepository.findPositionsByMonth(true).orElseThrow(() -> new NotFoundException("Open positions count data by month not fetched"));
        return result;
    }

    public List<PositionsByMonthMapper> getClosedPositionByMonth() {
        PositionByMonthDto openPositionByMonth = new PositionByMonthDto();
        List<PositionsByMonthMapper> result = positionTblRepository.findPositionsByMonth(false).orElseThrow(() -> new NotFoundException("Closed positions count data by month not fetched"));
        return result;
    }

    public List<String> getOrderTypeByRegion(){
        return positionTblRepository.getOrderTypeByRegion().orElseThrow(() -> new NotFoundException("Data not Found"));
    }

    public List<String> getOrderTypeByBusinessUnit(){
        return positionTblRepository.getOrderTypeByBusinessUnit().orElseThrow(() -> new NotFoundException("Data not Found"));
    }

    public GraphResponseDto getPositionsCountWDates(String period, String toggle) {
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
        List<CountWDateDto> countWDateDto ="all".equals(toggle)? this.positionTblRepository.getPositionCountWDates(startDate,endDate):this.positionTblRepository.getPositionCountWDates(startDate,endDate,"open".equals(toggle)?true:false);
        graphResponseDto.setData(countWDateDto);
        return graphResponseDto;
    }
}
