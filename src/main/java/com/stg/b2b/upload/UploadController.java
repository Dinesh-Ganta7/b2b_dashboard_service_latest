package com.stg.b2b.upload;

import com.stg.b2b.entity.BidInfo;
import com.stg.b2b.entity.Position;
import com.stg.b2b.exception.FileException;
import com.stg.b2b.upload.dto.NotificationDto;
import com.stg.b2b.util.XlsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class UploadController {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    private BidXlUploadService bidXlUploadService;
    @Autowired
    private NotificationExcelService notificationExcelService;
    @Autowired
    private PdfService pdfService;
    /** apis for uploading orders from notification excel*/
    /**
     * used to get all the closing and new orders based on the Excel provided
     * @return returns notification dto containing new and closing orders
     */
    @PostMapping("orders/upload-old")
    public ResponseEntity<NotificationDto> uploadOrderNotificationFile(@RequestParam MultipartFile[] files) throws IOException {
        logger.debug("uploadOrderNotificationFile entered");
        return ResponseEntity.ok(this.notificationExcelService.getOrdersJson(files));
    }
    @PostMapping("orders/upload")
    public ResponseEntity<String> uploadOrderNotificationFile2(@RequestParam MultipartFile[] files) throws IOException {
        logger.debug("uploadOrderNotificationFile entered");
        this.notificationExcelService.saveOrdersOnAccepting(this.notificationExcelService.getOrdersJson2(files));
        return ResponseEntity.ok("\"Success\"");
    }

    /**
     * @param dto-> from the uploadOrderNotificationFile is received here
     * @return returns the confirmation string on success or throws an exception
     */
    @PostMapping("orders/accept")
    public ResponseEntity<String> saveOrdersOnSubmit(@RequestBody NotificationDto dto){
        logger.debug("saveOrdersOnSubmit entered");
        return ResponseEntity.ok(this.notificationExcelService.saveOrdersOnAccepting(dto));
    }
    /**
     * used to upload pdfs to create positions and update order details
     * @return list of positions for each file uploaded
     */
    @PostMapping("position/upload")
    public ResponseEntity< List<List<Position>>> uploadPdfs(@RequestParam MultipartFile[] files) throws FileException, IOException {
        logger.info("uploadPdfs() called in controller");
        return  ResponseEntity.ok(this.pdfService.readPdfs(files));
    }

    @PostMapping("position/upload/{orderNoSelected}")
    public ResponseEntity<List<Position>> uploadPosition(@RequestParam MultipartFile[] files, @PathVariable("orderNoSelected") int orderNo) throws FileException, IOException {
        logger.info("uploadPdfs() called in controller");
        return  ResponseEntity.ok(this.pdfService.readPdf(files[0],orderNo));
    }
    @PostMapping("bid-info/received")
    public ResponseEntity<List<Integer>> processBidReceivedFile(@RequestParam MultipartFile[] files) throws IOException {
        logger.debug("Entered processBidReceivedFile");
        return ResponseEntity.ok(this.bidXlUploadService.processBidReceivedFile(files));
    }

    @PostMapping("bid-info/submission")
    public ResponseEntity<List<Integer>> processSubmissionFile(@RequestParam MultipartFile[] files) throws IOException {
        logger.debug("Entered processSubmissionFile");
        return ResponseEntity.ok(this.bidXlUploadService.processCandidateSubmissionReport(files));
    }

    @PostMapping("bid-info/declined")
    public ResponseEntity<List<Integer>> processDeclineFile(@RequestParam MultipartFile[] files) throws IOException {
        logger.debug("Entered processDeclineFile");
        return ResponseEntity.ok(this.bidXlUploadService.processCandidateDeclineFile(files));
    }

}
