package com.stg.b2b.upload;

import com.stg.b2b.entity.*;
import com.stg.b2b.exception.FileException;
import com.stg.b2b.repository.*;
import com.stg.b2b.upload.dto.ExcelResponseDto;
import com.stg.b2b.upload.dto.NotificationDto;
import com.stg.b2b.upload.dto.UnassignedDto;
import com.stg.b2b.upload.dto.UniqueOrderDto;
import com.stg.b2b.util.DateUtils;
import com.stg.b2b.util.XlsUtil;
import com.stg.b2b.util.XlsxUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

@Service
public class NotificationExcelService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationExcelService.class);


    private Map<String, ArrayList<String>> notificationMap = new HashMap<>();
    private static final String DELIMITER_FOR_EXCEL_COLUMNS = ":";

    private UnassignedDto valuesForUnassignedFieldsInOrder;

    @Autowired
    private XlsxUtil util;
    @Autowired
    private MasterRepository masterRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderFollowUpRepository orderFollowUpRepository;

    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private BusinessUnitRepository businessUnitRepository;

    @PostConstruct
    public void init() {
        if(valuesForUnassignedFieldsInOrder==null){
            valuesForUnassignedFieldsInOrder = new UnassignedDto();
            Optional<Manager> managerOptional = this.managerRepository.findByLl6Manager("Unassigned");
            if(managerOptional.isPresent()){
                this.valuesForUnassignedFieldsInOrder.setManager(managerOptional.get());
            }else {
                Manager manager1 = new Manager();
                manager1.setLl6Manager("Unassigned");
                manager1.setLl5Manager("Unassigned");
                manager1.setLl4Manager("Unassigned");
                manager1.setLl3Manager("Unassigned");
                manager1.setLl2Manager("Unassigned");
                manager1 = this.managerRepository.save(manager1);
                this.valuesForUnassignedFieldsInOrder.setManager(manager1);
            }
            Optional<Master> region = this.masterRepository.findByMasterCategoryAndMasterName("region","Unassigned");
            if(region.isPresent()){
                valuesForUnassignedFieldsInOrder.setRegion(region.get());
            }else{
                Master master = new Master();
                master.setMasterCategory("region");
                master.setMasterName("Unassigned");
                master = this.masterRepository.save(master);
                valuesForUnassignedFieldsInOrder.setRegion(master);
            }
            Optional<Master> orderType = this.masterRepository.findByMasterCategoryAndMasterName("order type","Unassigned");
            if(orderType.isPresent()){
                valuesForUnassignedFieldsInOrder.setOrderType(orderType.get());
            }else{
                Master master = new Master();
                master.setMasterCategory("order type");
                master.setMasterName("Unassigned");
                master = this.masterRepository.save(master);
                valuesForUnassignedFieldsInOrder.setOrderType(master);
            }
            BusinessUnit businessUnit = this.businessUnitRepository.findByBuNameAndProductLineName("Unassigned","Unassigned").orElseThrow(()->new FileException("Please add a business unit name called 'Unassigned' and product line called 'Unassigned' in it!"));
            valuesForUnassignedFieldsInOrder.setBusinessUnit(businessUnit);
        }
    }

    public NotificationDto getOrdersJson(MultipartFile[] files) throws IOException {
        logger.debug("getOrdersJson entered");
        String extension = FilenameUtils.getExtension(files[0].getOriginalFilename());
        fileCheck(files, extension);
        List<Master> columnNamesMaster = this.masterRepository.findByMasterCategory("openNotificationExcel").orElseThrow(() -> new FileException("Columns for \"openNotificationExcel\" not found the master data!"));
        String orderNo = "";
        String broadcastDate = "";
        for (Master master : columnNamesMaster) {
            if ("orderNo".equals(master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                orderNo = master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[1];
            } else if ("ordBroadcastDate".equals(master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                broadcastDate = master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[1];
            }
        }
        if (orderNo.isEmpty()) {
            throw new FileException("No column name for orderNo for openNotificationExcel found in Master");
        } else if (broadcastDate.isEmpty()) {
            throw new FileException("No column name for ordBroadcastDate for openNotificationExcel found in Master");
        }
        List<Integer> orderNosFromExcel;
        List<Order> ordersFromExcel = new ArrayList<>();
        orderNosFromExcel = getIntegers(orderNo, ordersFromExcel);
        List<Order> orders = this.orderRepository.findByOrdActiveStatus(true);
        NotificationDto notificationDto = new NotificationDto();
        ArrayList<ExcelResponseDto> closingOrders = new ArrayList<>();
        ArrayList<Integer> oldOrderNos = new ArrayList<>();
        for (Order order : orders) {
            oldOrderNos.add(order.getId().getOrderNo());
            if (!orderNosFromExcel.contains(order.getId().getOrderNo())) {
                ExcelResponseDto excelResponseDto = new ExcelResponseDto();
                excelResponseDto.setOrderNo(order.getId().getOrderNo());
                excelResponseDto.setBroadcastDate(order.getId().getOrdBroadcastDate());
                excelResponseDto.setStratification(order.getOrdStratification());
                closingOrders.add(excelResponseDto);
            }
        }
        ArrayList<ExcelResponseDto> newOrders = new ArrayList<>();
        for (Order tbl : ordersFromExcel) {
            if (!oldOrderNos.contains(tbl.getId().getOrderNo())) {
                ExcelResponseDto excelResponseDto = new ExcelResponseDto();
                excelResponseDto.setOrderNo(tbl.getId().getOrderNo());
                excelResponseDto.setBroadcastDate(tbl.getId().getOrdBroadcastDate());
                excelResponseDto.setStratification(tbl.getOrdStratification());
                newOrders.add(excelResponseDto);
            }
        }
        notificationDto.setClosingOrders(closingOrders);
        notificationDto.setNewOrders(newOrders);
        return notificationDto;
    }

    public NotificationDto getOrdersJson2(MultipartFile[] files) throws IOException {
        logger.debug("getOrdersJson2 entered");
        String extension = FilenameUtils.getExtension(files[0].getOriginalFilename());
        fileCheck(files, extension);
        List<Master> columnNamesMaster = this.masterRepository.findByMasterCategory("openNotificationExcel").orElseThrow(() -> new FileException("Columns for \"openNotificationExcel\" not found the master data!"));
        String orderNo = "";
        String broadcastDate = "";
        String stratification = "";
        for (Master master : columnNamesMaster) {
            if ("orderNo".equals(master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                orderNo = master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[1];
            } else if ("ordBroadcastDate".equals(master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                broadcastDate = master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[1];
            } else if ("ordStratification".equals(master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                stratification = master.getMasterName().split(DELIMITER_FOR_EXCEL_COLUMNS)[1];
            }
        }
        if (orderNo.isEmpty()) {
            throw new FileException("No column name for orderNo for openNotificationExcel found in Master");
        } else if (broadcastDate.isEmpty()) {
            throw new FileException("No column name for ordBroadcastDate for openNotificationExcel found in Master");
        } else if (stratification.isEmpty()) {
            throw new FileException("No column name for stratification for openNotificationExcel found in Master");
        }
        List<UniqueOrderDto> uniqueOrderDtosFormExcel = getUniqueOrderDtoList(orderNo, broadcastDate, stratification);
        List<Order> activeOrdersFromDb = this.orderRepository.findByOrdActiveStatus(true);

        ArrayList<UniqueOrderDto> uniqueOrderDtosFromDb = new ArrayList<>();
        ArrayList<ExcelResponseDto> closingOrders = new ArrayList<>();

        for (Order order : activeOrdersFromDb) {
            UniqueOrderDto uniqueOrderDto = new UniqueOrderDto();
            uniqueOrderDto.setOrdBroadcastDate(order.getId().getOrdBroadcastDate());
            uniqueOrderDto.setOrderNo(order.getId().getOrderNo());
            uniqueOrderDto.setStratification(order.getOrdStratification());
            uniqueOrderDtosFromDb.add(uniqueOrderDto);

            if (!uniqueOrderDtosFormExcel.contains(uniqueOrderDto)) {
                ExcelResponseDto excelResponseDto = new ExcelResponseDto();
                excelResponseDto.setOrderNo(order.getId().getOrderNo());
                excelResponseDto.setBroadcastDate(order.getId().getOrdBroadcastDate());
                excelResponseDto.setStratification(order.getOrdStratification());
                closingOrders.add(excelResponseDto);
            }
        }

        ArrayList<ExcelResponseDto> newOrders = new ArrayList<>();

        for (UniqueOrderDto tbl : uniqueOrderDtosFormExcel) {
            if (!uniqueOrderDtosFromDb.contains(tbl)) {
                ExcelResponseDto excelResponseDto = new ExcelResponseDto();
                excelResponseDto.setOrderNo(tbl.getOrderNo());
                excelResponseDto.setBroadcastDate(tbl.getOrdBroadcastDate());
                excelResponseDto.setStratification(tbl.getStratification());
                newOrders.add(excelResponseDto);
            }
        }
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setClosingOrders(closingOrders);
        notificationDto.setNewOrders(newOrders);
        logger.info("OpenNotificationExcel processed");
        return notificationDto;
    }

    private List<UniqueOrderDto> getUniqueOrderDtoList(String orderNo, String ordBroadcastDate, String stratification) {
        List<UniqueOrderDto> uniqueOrderDtoListFromExcel = new ArrayList<>();
        if (this.notificationMap.get(orderNo) != null && (!this.notificationMap.get(orderNo).isEmpty())) {
            if (this.notificationMap.get(ordBroadcastDate) != null && (!this.notificationMap.get(ordBroadcastDate).isEmpty())) {
                if (this.notificationMap.get(stratification) != null && (!this.notificationMap.get(stratification).isEmpty())) {
                    for (int i = 0; i < this.notificationMap.get(orderNo).size(); i++) {
                        UniqueOrderDto uniqueOrderDto = new UniqueOrderDto();
                        uniqueOrderDto.setOrderNo(Integer.parseInt(this.notificationMap.get(orderNo).get(i)));
                        uniqueOrderDto.setOrdBroadcastDate(DateUtils.formatDate(this.notificationMap.get(ordBroadcastDate).get(i), "dd-MMM-yyyy"));
                        uniqueOrderDto.setStratification(this.notificationMap.get(stratification).get(i));
                        uniqueOrderDtoListFromExcel.add(uniqueOrderDto);
                    }
                } else {
                    throw new FileException("No stratification found in the excel You have uploaded please contact the developer!");
                }
            } else {
                throw new FileException("No ordBroadcastDate found in the excel You have uploaded please contact the developer!");
            }
        } else {
            throw new FileException("No orders found in the excel You have uploaded please contact the developer!");
        }
        return uniqueOrderDtoListFromExcel;
    }

    private List<Integer> getIntegers(String orderNo, List<Order> ordersFromExcel) {
        List<Integer> orderNosFromExcel;
        if (this.notificationMap.get(orderNo) != null) {
            if ((!this.notificationMap.get(orderNo).isEmpty())) {
                orderNosFromExcel = this.notificationMap.get(orderNo)
                        .stream()
                        .map(Integer::parseInt)
                        .toList();
                for (int i = 0; i < orderNosFromExcel.size(); i++) {
                    ordersFromExcel.add(this.getOrderAccordingToExcel(i));
                }
            } else {
                throw new FileException("No orders found in the excel You have uploaded please contact the developer!");
            }
        } else {
            throw new FileException("No orders found in the excel You have uploaded please contact the developer!");
        }
        return orderNosFromExcel;
    }

    private void fileCheck(MultipartFile[] files, String extension) throws IOException {
        if ("xls".equalsIgnoreCase(extension)) {
            this.notificationMap = XlsUtil.uploadExcels(files[0].getInputStream(), 1);
        } else if ("xlsx".equalsIgnoreCase(extension) || "xlsm".equalsIgnoreCase(extension)) {
            this.notificationMap = this.util.importSheetWithSheetIndex(files[0].getInputStream(), 0);

        } else {
            throw new FileException("The file is not of required type !");
        }
    }

    public Order getOrderAccordingToExcel(int iteration) {
        logger.debug("getOrderAccordingToExcel entered");
        Order tbl = new Order();
        OrderPk orderPk = new OrderPk();
        List<Master> columnNamesMaster = this.masterRepository.findByMasterCategory("openNotificationExcel").orElseThrow(() -> new FileException("Columns for \"openNotificationExcel\" not found the master data!"));
        for (Master master : columnNamesMaster) {
            String columnName = master.getMasterName();
            if ("orderNo".equals(columnName.split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                orderPk.setOrderNo(Integer.parseInt(this.notificationMap.get(columnName.split(DELIMITER_FOR_EXCEL_COLUMNS)[1]).get(iteration)));
            } else if ("ordStratification".equals(columnName.split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                tbl.setOrdStratification(this.notificationMap.get(columnName.split(DELIMITER_FOR_EXCEL_COLUMNS)[1]).get(iteration));
            } else if ("orderType".equals(columnName.split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                logger.info("Tried to insert order type but failed");
            } else if ("ordBroadcastDate".equals(columnName.split(DELIMITER_FOR_EXCEL_COLUMNS)[0])) {
                orderPk.setOrdBroadcastDate(DateUtils.formatDate(this.notificationMap.get(columnName.split(DELIMITER_FOR_EXCEL_COLUMNS)[1]).get(iteration), "dd-MMM-yyyy"));
            }
        }
        tbl.setId(orderPk);
        tbl.setOrdActiveStatus(true);
        return tbl;
    }

    public String saveOrdersOnAccepting(NotificationDto dto) {
        logger.debug("saveOrdersOnAccepting entered");
        List<Order> tbls = new ArrayList<>();
        if (dto.getNewOrders() != null && !dto.getNewOrders().isEmpty()) {
            for (ExcelResponseDto num : dto.getNewOrders()) {
                Order tbl = new Order();
                tbl.setOrdActiveStatus(true);
                tbl.setOrdSkillGroup("Unassigned");
                tbl.setRegion(this.valuesForUnassignedFieldsInOrder.getRegion());
                tbl.setOrderType(this.valuesForUnassignedFieldsInOrder.getOrderType());
                tbl.setBusinessUnit(this.valuesForUnassignedFieldsInOrder.getBusinessUnit());
                tbl.setManager(this.valuesForUnassignedFieldsInOrder.getManager());
                OrderPk orderPk = new OrderPk();
                orderPk.setOrderNo(num.getOrderNo());
                tbl.setOrdStratification(num.getStratification());
                orderPk.setOrdBroadcastDate(num.getBroadcastDate());
                tbl.setId(orderPk);
                tbls.add(tbl);
            }
            this.orderRepository.saveAll(tbls);
        }

        for (ExcelResponseDto num : dto.getClosingOrders()) {
            this.positionRepository.updateStatusByOrderNoAndBroadcastDate(num.getOrderNo(),num.getBroadcastDate(),"ADMIN",new Date(System.currentTimeMillis()));
            this.orderRepository.updateStatusByOrderNoAndBroadcastDate(num.getOrderNo(),num.getBroadcastDate(),"ADMIN",new Date(System.currentTimeMillis()));
        }



        return "\"Update success !\"";
    }
}
