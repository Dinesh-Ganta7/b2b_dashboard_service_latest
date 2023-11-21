package com.stg.b2b.upload;

import com.stg.b2b.entity.Order;
import com.stg.b2b.entity.Position;
import com.stg.b2b.entity.PositionPK;
import com.stg.b2b.exception.FileException;
import com.stg.b2b.repository.OrderRepository;
import com.stg.b2b.repository.PositionRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PdfService {
    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);
    private static final String FOUND ="{} {}";
    private static final String NOT_FOUND ="{} not found.";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PositionRepository positionTblRepository;

    public List<List<Position>> readPdfs(MultipartFile[] files) throws FileException, IOException {/**pdf*/
        if (files.length < 1) {
            throw new FileException("select atleast 1 file");
        }
        List<List<Position>> positionTbls = new ArrayList<>();
        for (MultipartFile file : files) {
            positionTbls.add(this.readPdf(file));

        }
        return positionTbls;
    }

    public List<Position> readPdf(MultipartFile file) throws FileException, IOException {/**pdf*/
        if (file.isEmpty()) {
            throw new FileException("Empty file or File not found");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"pdf".equalsIgnoreCase(extension)) {
            throw new FileException("Invalid file format. Please upload a PDF file.");
        }
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper stripper = new PDFTextStripper();
            String text = "";
            for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
                if (pageNumber == 0 || pageNumber == 1) {
                    stripper.setStartPage(pageNumber + 1);
                    stripper.setEndPage(pageNumber + 1);
                    stripper.setStartBookmark(null);
                    stripper.setEndBookmark(null);
                    stripper.setParagraphStart("\n");
                    stripper.setParagraphEnd("");
                }
                String textNew = stripper.getText(document);
                String[] lines = textNew.split("\\r?\\n");
                try{
                    text = text + "\n" + String.join("\n", Arrays.copyOfRange(lines, 4, lines.length));
                }catch (Exception e){
                    throw new FileException("Data in pdf is not of usual format !!!");
                }
            }
            document.close();
            if (this.getOrderNo(text) == 0) {
                throw new FileException("No order number found in this pdf !!!");
            } else {
                int orderNo = this.getOrderNo(text);
                Order orderTbl =this.orderRepository.findByOrderNo(orderNo).orElseThrow(()->new FileException("Order number in PDF is not found in data !!!"));
                List<Position> positionsFromDB=this.positionTblRepository.findPositionsByOrderNoAndBroadcastDate(orderTbl.getId().getOrderNo(),orderTbl.getId().getOrdBroadcastDate());
                List<Position> activePositions = new ArrayList<>();
                orderTbl.setOrdNoOfPositions(this.getNoOfPositions(text));
                List<Integer> positionNosFromPdf=this.getPositionNo2(text);
                List<Integer> positionNosFromDb = new ArrayList<>();
                orderTbl.setOrdStratification(this.getPositionTitle(text).split("Duration")[0].trim());
                orderTbl.setOrdPreferredSkill(this.getSkillsPreferred(text));
                orderTbl.setOrdSkillRequired(this.getSkillsRequired(text));
                orderTbl.setOrdTargetRate(this.getTargetRate(text));
                orderTbl.setOrdJobDescription(this.getDescription(text));
                for(Position tbl:positionsFromDB){
                    positionNosFromDb.add(tbl.getId().getPositionNo());
                    tbl.setPosActiveStatus(positionNosFromPdf.contains(tbl.getId().getPositionNo()));
                    tbl.getId().setOrder(orderTbl);
                    tbl.setPosUpdatedBy("ADMIN");
                    tbl.setPosUpdatedAt(new Date(System.currentTimeMillis()));
                    activePositions.add(tbl);
                }
                for(int i :positionNosFromPdf){
                    if(!positionNosFromDb.contains(i)){
                        Position newPosition = new Position();
                        PositionPK tblPK = new PositionPK();
                        tblPK.setOrder(orderTbl);
                        tblPK.setPositionNo(i);
                        newPosition.setId(tblPK);
                        newPosition.setPosActiveStatus(true);
                        newPosition.setPosUpdatedBy("ADMIN");
                        newPosition.setPosUpdatedAt(new Date(System.currentTimeMillis()));
                        activePositions.add(newPosition);
                    }
                }
                return this.positionTblRepository.saveAll(activePositions);
            }
    }
    public List<Position> readPdf(MultipartFile file, Integer orderNoSelected) throws FileException, IOException {/**pdf*/
        if (file.isEmpty()) {
            throw new FileException("Empty file or File not found");
        }
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!"pdf".equalsIgnoreCase(extension)) {
            throw new FileException("Invalid file format. Please upload a PDF file.");
        }
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = "";
        for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
            if (pageNumber == 0 || pageNumber == 1) {
                stripper.setStartPage(pageNumber + 1);
                stripper.setEndPage(pageNumber + 1);
                stripper.setStartBookmark(null);
                stripper.setEndBookmark(null);
                stripper.setParagraphStart("\n");
                stripper.setParagraphEnd("");
            }
            String textNew = stripper.getText(document);
            String[] lines = textNew.split("\\r?\\n");
            text = text + "\n" + String.join("\n", Arrays.copyOfRange(lines, 4, lines.length));
        }
        document.close();
        if (this.getOrderNo(text) == 0) {
            throw new FileException("No order number found in this pdf !!!");
        } else {
            Integer orderNo = this.getOrderNo(text);
            if(!orderNo.equals(orderNoSelected)){
                throw new FileException("Wrong pdf selected , please select pdf with the correct orderNo!");
            }
            Order orderTbl =this.orderRepository.findByOrderNo(orderNo).orElseThrow(()->new FileException("Order number in PDF is not found in data !!!"));
            List<Position> positionsFromDB=this.positionTblRepository.findPositionsByOrderNoAndBroadcastDate(orderTbl.getId().getOrderNo(),orderTbl.getId().getOrdBroadcastDate());
            List<Position> activePositions = new ArrayList<>();
            orderTbl.setOrdNoOfPositions(this.getNoOfPositions(text));
            List<Integer> positionNosFromPdf=this.getPositionNo2(text);
            List<Integer> positionNosFromDb = new ArrayList<>();
            orderTbl.setOrdStratification(this.getPositionTitle(text).split("Duration")[0].trim());
            orderTbl.setOrdPreferredSkill(this.getSkillsPreferred(text));
            orderTbl.setOrdSkillRequired(this.getSkillsRequired(text));
            orderTbl.setOrdTargetRate(this.getTargetRate(text));
            orderTbl.setOrdJobDescription(this.getDescription(text));
            for(Position tbl:positionsFromDB){
                positionNosFromDb.add(tbl.getId().getPositionNo());
                tbl.setPosActiveStatus(positionNosFromPdf.contains(tbl.getId().getPositionNo()));
                tbl.getId().setOrder(orderTbl);
                tbl.setPosUpdatedBy("ADMIN");
                tbl.setPosUpdatedAt(new Date(System.currentTimeMillis()));
                activePositions.add(tbl);
            }
            for(int i :positionNosFromPdf){
                if(!positionNosFromDb.contains(i)){
                    Position newPosition = new Position();
                    PositionPK tblPK = new PositionPK();
                    tblPK.setOrder(orderTbl);
                    tblPK.setPositionNo(i);
                    newPosition.setId(tblPK);
                    newPosition.setPosActiveStatus(true);
                    newPosition.setPosUpdatedBy("ADMIN");
                    newPosition.setPosUpdatedAt(new Date(System.currentTimeMillis()));
                    activePositions.add(newPosition);
                }
            }
            return this.positionTblRepository.saveAll(activePositions);
        }
    }

    private int getOrderNo(String text) {
        return getIntegerAfterString(text, "Order Number:");
    }

    private float getTargetRate(String text) {
        Pattern pattern = Pattern.compile("Target Rate: (\\d+)");
        Matcher matcher = pattern.matcher(text);
        float targetRate = 0;
        if (matcher.find()) {
            String val = matcher.group(1);
            logger.info("Target Rate : {}", val);
            targetRate = Float.parseFloat(val);
        } else {
            logger.info("Target Rate not found");
        }
        return targetRate;
    }

    private int getNoOfPositions(String text) {
        return getIntegerAfterString(text, "Number of Positions:");
    }

    private int getIntegerAfterString(String text, String prefix) {
        Pattern pattern = Pattern.compile(prefix + " (\\d+)");
        Matcher matcher = pattern.matcher(text);
        int count = 0;
        if (matcher.find()) {
            String positions = matcher.group(1).trim();
            count = Integer.parseInt(positions);
            logger.info(FOUND, prefix, count);
        } else {
            logger.info(NOT_FOUND, prefix);
        }
        return count;
    }
    private List<Integer> getPositionNo(String text, int count) {
        Pattern pattern = Pattern.compile("Position No: (\\d+)");
        Matcher matcher = pattern.matcher(text);
        List<Integer> positionNumbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (matcher.find()) {
                String orderNumber = matcher.group(1);
                logger.info("Position Number: {}", orderNumber);
                positionNumbers.add(Integer.valueOf(orderNumber));
            } else {
                logger.info("Position Number not found");
            }
        }
        return positionNumbers;
    }

    private List<Integer> getPositionNo2(String text) {
        Pattern pattern = Pattern.compile("Position No: (\\d+)");
        Matcher matcher = pattern.matcher(text);
        List<Integer> positionNumbers = new ArrayList<>();
        boolean found =true;
        while(found){
            if (matcher.find()) {
                String orderNumber = matcher.group(1);
                logger.info("Position Number: {}", orderNumber);
                positionNumbers.add(Integer.valueOf(orderNumber));
            } else {
                found=false;
                logger.info("Position Number not found");
            }
        }
        return positionNumbers;
    }

    private String getPositionTitle(String text) {
        return getSingleLineTextAfterString(text, "Position Title:").split("\t")[0];
    }

    private String getDescription(String text) {
        return getMultiLineTextBWTNTwoStrings(text, "Position Description:", "Skills Required:");
    }

    private String getSkillsRequired(String text) {
        return getMultiLineTextBWTNTwoStrings(text, "Skills Required:", "Skills Preferred:");
    }

    private String getSkillsPreferred(String text) {
        return getMultiLineTextBWTNTwoStrings(text, "Skills Preferred:", "Experience Required:");
    }

    private String getMultiLineTextBWTNTwoStrings(String text, String prefix, String suffix) {
        Pattern pattern = Pattern.compile((prefix + "(.*?)" + suffix), Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        String skillsPreferred = "";
        if (matcher.find()) {
            skillsPreferred = matcher.group(1).trim().replace("\n", "");
            logger.info(FOUND, prefix, skillsPreferred);
        } else {
            logger.info(NOT_FOUND, prefix);
        }
        return skillsPreferred;
    }

    private String getSingleLineTextAfterString(String text, String prefix) {
        String positionTitleRegex = prefix + " (.*)";
        Pattern pattern = Pattern.compile(positionTitleRegex);
        Matcher matcher = pattern.matcher(text);
        String title = "";
        if (matcher.find()) {
            title = matcher.group(1);
            logger.info(FOUND, prefix, title);
        } else {
            logger.info(NOT_FOUND, prefix);
        }
        return title;
    }

}