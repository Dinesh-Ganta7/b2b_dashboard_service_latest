package com.stg.b2b.upload;

import com.stg.b2b.entity.*;
import com.stg.b2b.exception.FileException;
import com.stg.b2b.repository.*;
import com.stg.b2b.util.DateUtils;
import com.stg.b2b.util.XlsUtil;
import com.stg.b2b.util.XlsxUtil;
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
public class BidXlUploadService {

    public static final String BID_STATUS = "bidStatus";
    public static final String ORDER_NO = "orderNo";
    public static final String CANDIDATE_NAME = "candidateName";
    public static final String BID_NO = "bidNo";
    private static final Logger logger = LoggerFactory.getLogger(BidXlUploadService.class);
    private static final String FILE_NOT_REQUIRED_TYPE = "The file is not of required type !";
    private static final String EXCEL_COLUMN_DELIMITER = ":";
    public static final String BID_RATE = "bidRate";

    @Autowired
    private XlsxUtil util;

    @Autowired
    private BidInfoRepository bidInfoTblRepository;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private MasterRepository masterRepository;
    @Autowired
    private CandidateRepository candidateRepository;



    public List<Integer> processBidReceivedFile(MultipartFile[] files) throws IOException {
        logger.debug("Entered processBidReceivedFile");
        Map<String, ArrayList<String>> bidsMap = new HashMap<>();
        String extension = FilenameUtils.getExtension(files[0].getOriginalFilename());
        bidsMap = checkExtensionForBidExcel(files, extension);
        List<Master> masters = this.masterRepository.findByMasterCategory("bidReceivedExcel").orElseThrow(() -> new FileException("No master values for bidReceivedExcel found in data"));
        String orderNo = "";
        String bidNo="";
        String bidStatus="";
        String candidateName="";
        String bidReceivedDate = "";

        for (Master master : masters) {
            if (ORDER_NO.equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                orderNo = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }else if ("bidNo".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                bidNo = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }else if ("bidStatus".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                bidStatus = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if ("candidateName".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                candidateName = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }else if ("bidReceivedDate".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                bidReceivedDate = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }
        }
        if (orderNo.isEmpty()) {
            throw new FileException("orderNo not found in master data for bidReceivedExcel");
        }
        if (bidNo.isEmpty()) {
            throw new FileException("bidNo not found in master data for bidReceivedExcel");
        }
        if (bidStatus.isEmpty()) {
            throw new FileException("bidStatus not found in master data for bidReceivedExcel");
        }
        if (candidateName.isEmpty()) {
            throw new FileException("candidateName not found in master data for bidReceivedExcel");
        }
        if (bidReceivedDate.isEmpty()) {
            throw new FileException("bidReceivedDate not found in master data for bidReceivedExcel");
        }

        List<String> orderNos = bidsMap.get(orderNo);
        List<String> bidNos = bidsMap.get(bidNo);
        List<String> bidStatuses = bidsMap.get(bidStatus);
        List<String> candidateNames = bidsMap.get(candidateName);
        List<String> bidReceivedDates = bidsMap.get(bidReceivedDate);

        List<BidInfo> bidInfos = new ArrayList<>();
        List<Integer> bidNums = new ArrayList<>();
        List<Integer> orderNoNotInDataBase=new ArrayList<>();

        int i=0;
        for(String bidNum :bidNos){
            if(!bidNum.isEmpty()){
                Integer bidNum2=(int)Float.parseFloat(bidNum);
                bidNums.add(bidNum2);
                BidInfo bidInfo = this.bidInfoTblRepository.findByBidNo(bidNum2);
                if(bidInfo!=null){
                    bidInfo.setBidStatus(bidStatuses.get(i));
                    bidInfos.add(bidInfo);
                }else{
                    int orderNumInXl = (int)Float.parseFloat(orderNos.get(i));
                    Optional<Order> order = this.orderRepository.findByOrderNo(orderNumInXl);
                    if(order.isPresent()){
                        bidInfo = new BidInfo();
                        bidInfo.setBidNo(bidNum2);
                        bidInfo.setOrder(order.get());
                        bidInfo.setBidStatus(bidStatuses.get(i));
                        bidInfo.setBidActiveStatus(true);
                        bidInfo.setBidName(candidateNames.get(i));
                        bidInfo.setBidReceivedDate(DateUtils.formatDate(bidReceivedDates.get(i),"dd-MMM-yyyy hh:mm:ss a"));
                        bidInfos.add(bidInfo);
                    }else{
                        orderNoNotInDataBase.add(orderNumInXl);
                    }
                }
            }
            i++;
        }
        try {
            bidInfoTblRepository.closeBidNos(bidNums);
            this.bidInfoTblRepository.saveAll(bidInfos);
        }catch (Exception e){
            throw new FileException("Error while saving data from bidReceived file,Contact development team");
        }

        return orderNoNotInDataBase;
    }
    public List<Integer> processCandidateSubmissionReport(MultipartFile[] files) throws IOException {
        logger.debug("Entered processCandidateSubmissionReport");
        String extension = FilenameUtils.getExtension(files[0].getOriginalFilename());
        Map<String, ArrayList<String>> candidateSubmissionMap = new HashMap<>();

        if (extension != null) {
            candidateSubmissionMap =checkExtension(files, extension);
        } else {
            return new ArrayList<>();
        }
        List<Master> masters = this.masterRepository.findByMasterCategory("candidateSubmissionReportExcel").orElseThrow(() -> new FileException("No master values for candidateSubmissionReportExcel found in data"));

        String bidStatus = "";
        String candidateName = "";
        String orderNo = "";
        String bidRate = "";
        String fileId = "";
        String bidReceivedDate = "";


        for (Master master : masters) {
            if (ORDER_NO.equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                orderNo = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if ("fileId".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                fileId = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if (BID_STATUS.equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                bidStatus = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if (CANDIDATE_NAME.equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                candidateName = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if (BID_RATE.equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                bidRate = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }else if ("bidReceivedDate".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                bidReceivedDate = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }
        }
        if (orderNo.isEmpty()) {
            throw new FileException("orderNo not found in master data for Submission file");
        }
        if (fileId.isEmpty()) {
            throw new FileException("fileId not found in master data for Submission file");
        }
        if (bidStatus.isEmpty()) {
            throw new FileException("bidStatus not found in master data for Submission file");
        }
        if (candidateName.isEmpty()) {
            throw new FileException("candidateName not found in master data for Submission file");
        }
        if (bidRate.isEmpty()) {
            throw new FileException("bidRate not found in master data for Submission file");
        }
        if (bidReceivedDate.isEmpty()) {
            throw new FileException("bidReceivedDate not found in master data for Submission file");
        }
        List<String> orderNos = candidateSubmissionMap.get(orderNo);
        List<String> candidateFullNames = candidateSubmissionMap.get(candidateName);
        List<String> bidRates = candidateSubmissionMap.get(bidRate);
        List<String> statuses = candidateSubmissionMap.get(bidStatus);
        List<String> fileIds = candidateSubmissionMap.get(fileId);
        List<String> bidReceivedDates = candidateSubmissionMap.get(bidReceivedDate);
        List<Candidate> updatingCandidates = new ArrayList<>();
        List<Integer> notFoundOrders = new ArrayList<>();
        int i = 0;
        for (String orderString : orderNos) {
            int orderNumInXl = (int)Float.parseFloat(orderNos.get(i));
            Optional<Order> order = this.orderRepository.findByOrderNo(orderNumInXl);
            if(order.isPresent()){
                int fileIdInXl =(int)Float.parseFloat(fileIds.get(i));
                Candidate candidate = this.candidateRepository.findCandidateByOrderAndFileId(fileIdInXl,order.get().getId().getOrderNo(),order.get().getId().getOrdBroadcastDate());
                if(candidate!=null){
                    candidate.setCandidateRate((double) Float.parseFloat(bidRates.get(i)));
                    candidate.setCandidateStatus(statuses.get(i));
                    candidate.setCandidateName(candidateFullNames.get(i));
                    updatingCandidates.add(candidate);
                }else {
                    CandidatePk candidatePk = new CandidatePk();
                    candidatePk.setOrder(order.get());
                    candidatePk.setFileId(fileIdInXl);
                    candidate =new Candidate();
                    candidate.setCandidatePk(candidatePk);
                    candidate.setCandidateRate((double) Float.parseFloat(bidRates.get(i)));
                    candidate.setCandidateStatus(statuses.get(i));
                    candidate.setCandidateName(candidateFullNames.get(i));
                    Date date = DateUtils.formatDate(bidReceivedDates.get(i),"dd-MMM-yy");
                    candidate.setCandidateSubmissionDate(date);
                    updatingCandidates.add(candidate);
                }
            }else{
                notFoundOrders.add(orderNumInXl);
            }
            i++;
        }
        try {
            this.candidateRepository.saveAll(updatingCandidates);
        }catch (Exception e){
            throw new FileException("An Error occured while saving candidateSubmissionReport , please contact development team");
        }
        return notFoundOrders;
    }
    public List<Integer> processCandidateDeclineFile(MultipartFile[] files) throws IOException {
        Map<String, ArrayList<String>> candidateDeclineMap;
        logger.debug("Entered processCandidateDeclineReport");
        String extension = FilenameUtils.getExtension(files[0].getOriginalFilename());
        candidateDeclineMap = checkExtension(files,extension);
        List<Master> masters = this.masterRepository.findByMasterCategory("candidateDeclineReportExcel").orElseThrow(() -> new FileException("No master values for candidateDeclineReportExcel found in data"));
        String orderNo = "";
        String fileId = "";
        String declinedCode = "";
        String reason = "";
        String declinedDate = "";
        for (Master master : masters) {
            if (ORDER_NO.equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                orderNo = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }else if ("fileId".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                fileId = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if ("declinedCode".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                declinedCode = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if ("reason".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                reason = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            } else if ("declinedDate".equals(master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[0])) {
                declinedDate = master.getMasterName().split(EXCEL_COLUMN_DELIMITER)[1];
            }

        }
        if (orderNo.isEmpty()) {
            throw new FileException("orderNo not found in master data for decline report");
        }
        if (fileId.isEmpty()) {
            throw new FileException("fileId not found in master data for decline report");
        }
        if (declinedCode.isEmpty()) {
            throw new FileException("declinedCode not found in master data for decline report");
        }
        if (declinedDate.isEmpty()) {
            throw new FileException("declinedDate not found in master data for decline report");
        }
        if (reason.isEmpty()) {
            throw new FileException("reason not found in master data for decline report");
        }
        List<String> orderNos = candidateDeclineMap.get(orderNo);
        List<String> fileIds = candidateDeclineMap.get(fileId);
        List<String> declinedCodes = candidateDeclineMap.get(declinedCode);
        List<String> declinedReasons = candidateDeclineMap.get(reason);
        List<String> declinedDates = candidateDeclineMap.get(declinedDate);
        List<Candidate> updatingCandidates = new ArrayList<>();
        List<Integer> notFoundOrderNo = new ArrayList<>();
        int i = 0;
        for (String orderString : orderNos) {
            int orderNumInXl = (int)Float.parseFloat(orderNos.get(i));
            Optional<Order> order = this.orderRepository.findByOrderNo(orderNumInXl);
            if(order.isPresent()) {
                int fileIdInXl = (int) Float.parseFloat(fileIds.get(i));
                Candidate candidate = this.candidateRepository.findCandidateByOrderAndFileId(fileIdInXl, order.get().getId().getOrderNo(), order.get().getId().getOrdBroadcastDate());
                if (candidate != null) {
                    candidate.setCandidateDeclinedCode(declinedCodes.get(i));
                    candidate.setCandidateDeclinedReason(declinedReasons.get(i));
                    Date date = DateUtils.formatDate(declinedDates.get(i),"dd-MMM-yy");
                    candidate.setCandidateDeclinedDate(date);
                    updatingCandidates.add(candidate);
                }
            }else{
                notFoundOrderNo.add(orderNumInXl);
            }
            i++;
        }
        try {
            this.candidateRepository.saveAll(updatingCandidates);
        }catch (Exception exception){
            throw new FileException("An Error occured while saving candidateSubmissionReport , please contact development team");
        }
        return notFoundOrderNo;
    }

    private Map<String, ArrayList<String>> checkExtensionForBidExcel(MultipartFile[] files, String extension) throws IOException {
        if ("xls".equalsIgnoreCase(extension)) {
            return  XlsUtil.uploadExcels(files[0].getInputStream(), 1);
        } else if ("xlsx".equalsIgnoreCase(extension) || "xlsm".equalsIgnoreCase(extension)) {
            return this.util.importSheetWithSheetIndex(files[0].getInputStream(), 0);

        } else {
            throw new FileException(FILE_NOT_REQUIRED_TYPE);
        }
    }

    private Map<String, ArrayList<String>> checkExtension(MultipartFile[] files, String extension) throws IOException {
        if ("xls".equalsIgnoreCase(extension)) {
            return XlsUtil.uploadExcels(files[0].getInputStream(), 3);
        } else if ("xlsx".equalsIgnoreCase(extension) || "xlsm".equalsIgnoreCase(extension)) {
            return this.util.importSheetWithSheetIndex(files[0].getInputStream(), 0, 3);
        } else {
            throw new FileException(FILE_NOT_REQUIRED_TYPE);
        }
    }
}
