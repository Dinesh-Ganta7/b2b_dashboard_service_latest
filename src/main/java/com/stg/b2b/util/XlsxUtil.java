package com.stg.b2b.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
public class XlsxUtil {
    public static final String COLUMN_DUPLICATE_FOUND = "Column duplicate found!!!!!!!!!!!!";
    private static final Logger logger = LoggerFactory.getLogger(XlsxUtil.class);


    public Map<String, ArrayList<String>> importSheetWithSheetIndexOld(InputStream inputStream, int index) throws IOException {
        return this.importSheetWithSheetIndexOld(inputStream,index,1);
    }
    public Map<String, ArrayList<String>> importSheetWithSheetIndexOld(InputStream inputStream, int index,int startRowNumberInExcel) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
        HSSFSheet sheet = workbook.getSheetAt(index);
        Map<String, ArrayList<String>> map = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            int j = 0;
            if((startRowNumberInExcel-1)<=i){
                for (Cell cell : row) {
                    if (indexOldExtractedMethod(startRowNumberInExcel, evaluator, sheet, map, i, j, cell)) break;
                    j++;
                }
            }

            i++;
        }
        workbook.close();
        return map;
    }

    private boolean indexOldExtractedMethod(int startRowNumberInExcel, HSSFFormulaEvaluator evaluator, HSSFSheet sheet, Map<String, ArrayList<String>> map, int i, int j, Cell cell) {
        if (i == (startRowNumberInExcel -1)) {
            if (sheet.getRow(i).getCell(j) == null) {
                return true;
            } else if (!map.containsKey(cell.getStringCellValue())) {
                map.put(cell.getStringCellValue(), new ArrayList<>());
            } else {
                logger.info(COLUMN_DUPLICATE_FOUND);
            }
        } else {
            ArrayList<String> newList = map.get(sheet.getRow((startRowNumberInExcel -1)).getCell(j).toString());
            newList.add(this.check(sheet.getRow(i).getCell(j), evaluator));
            map.put(sheet.getRow((startRowNumberInExcel -1)).getCell(j).toString(), newList);
        }
        return false;
    }

    /**
    public Map<String, ArrayList<String>> importSheetWithSheetNameOld(InputStream inputStream, String sheetName) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
        HSSFSheet sheet = workbook.getSheet(sheetName);
        Map<String, ArrayList<String>> map = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            int j = 0;
            for (Cell cell : row) {
                if (i == 0) {
                    if (sheet.getRow(i).getCell(j) == null) {
                        break;
                    } else if (!map.keySet().contains(cell.getStringCellValue())) {
                        map.put(cell.getStringCellValue(), new ArrayList<>());
                    } else {
                        logger.info(COLUMN_DUPLICATE_FOUND);
                    }
                } else {
                    ArrayList<String> newList = map.get(sheet.getRow(0).getCell(j).toString());
                    newList.add(this.check(sheet.getRow(i).getCell(j), evaluator));
                    map.put(sheet.getRow(0).getCell(j).toString(), newList);
                }
                j++;
            }
            i++;
        }
        workbook.close();
        return map;
    }
     */
    public Map<String, ArrayList<String>> importSheetWithSheetIndex(InputStream inputStream, int index) throws IOException {
        return importSheetWithSheetIndex(inputStream,index,1);
    }
    public Map<String, ArrayList<String>> importSheetWithSheetIndex(InputStream inputStream, int index,int startRowNumberInExcel) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Sheet sheet = workbook.getSheetAt(index);
        Map<String, ArrayList<String>> map = new HashMap<>();

        int i = 0;
        for (Row row : sheet) {
            int j = 0;
            if(i>=(startRowNumberInExcel-1)){
                for (Cell cell : row) {
                    if (extractedMethodForIndexNew(startRowNumberInExcel, evaluator, sheet, map, i, j, cell)) break;
                    j++;
                }
            }

            i++;
        }
        workbook.close();
        return map;
    }

    private boolean extractedMethodForIndexNew(int startRowNumberInExcel, FormulaEvaluator evaluator, Sheet sheet, Map<String, ArrayList<String>> map, int i, int j, Cell cell) {
        if (i == (startRowNumberInExcel -1)) {
            if (sheet.getRow(i).getCell(j) == null) {
                return true;
            } else if (!map.containsKey(cell.getStringCellValue())) {
                map.put(cell.getStringCellValue(), new ArrayList<>());
            } else {
                logger.info(COLUMN_DUPLICATE_FOUND);
            }
        } else {
            ArrayList<String> newList = map.get(sheet.getRow((startRowNumberInExcel -1)).getCell(j).toString());
            newList.add(this.check(cell, evaluator));
            map.put(sheet.getRow((startRowNumberInExcel -1)).getCell(j).toString(), newList);
        }
        return false;
    }

    /**
    public Map<String, ArrayList<String>> importSheetWithSheetName(InputStream inputStream, String sheetName) throws IOException {
        Workbook workbook = new XSSFWorkbook(inputStream);
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Sheet sheet = workbook.getSheet(sheetName);
        Map<String, ArrayList<String>> map = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            int j = 0;
            for (Cell cell : row) {
                if (i == 0) {
                    if (sheet.getRow(i).getCell(j) == null) {
                        break;
                    } else if (!map.keySet().contains(cell.getStringCellValue())) {
                        map.put(cell.getStringCellValue(), new ArrayList<>());
                    } else {
                        logger.info(COLUMN_DUPLICATE_FOUND);
                    }
                } else {
                    ArrayList<String> newList = map.get(sheet.getRow(0).getCell(j).toString());
                    newList.add(this.check(cell, evaluator));
                    map.put(sheet.getRow(0).getCell(j).toString(), newList);
                }
                j++;
            }
            i++;
        }
        workbook.close();
        return map;
    }
*/
    public String check(Cell cell, FormulaEvaluator evaluator) {
        String out = "";
        if (cell != null) {
            int cellType = cell.getCellType();

            switch (cellType) {
                case 0:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        out = out + cell.getDateCellValue();

                    } else {
                        out = out + cell.getNumericCellValue();
                    }
                    break;
                case 1:
                    out = out + cell.getStringCellValue();
                    break;
                case 2:
                    CellValue cellValue = evaluator.evaluate(cell);
                    int type = cellValue.getCellType();
                    if (type == 0 && DateUtil.isCellDateFormatted(cell)) {
                        out = out + cell.getDateCellValue();
                    } else if (type == 0) {
                        out = out + cellValue.getNumberValue();
                    } else if (type == 1) {
                        out = out + cellValue.getStringValue();
                    } else if (type == 4) {
                        out = out + cellValue.getBooleanValue();
                    } else if (type == 5) {
                        /**out = out + cellValue.getErrorValue();*/
                    }
                    break;
                case 3:
                    /**logger.info("Blank cell");*/
                    break;
                case 4:
                    out = out + cell.getBooleanCellValue();
                    break;
                case 5:
                    /**logger.info("error");*/
                    break;
                default:
                    break;
            }

        }
        return out;
    }

    public String check(HSSFCell cell, FormulaEvaluator evaluator) {
        String out = "";
        if (cell != null) {
            int cellType = cell.getCellType();

            switch (cellType) {
                case 0:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        out = out + cell.getDateCellValue();

                    } else {
                        out = out + cell.getNumericCellValue();
                    }
                    break;
                case 1:
                    out = out + cell.getStringCellValue();
                    break;
                case 2:
                    CellValue cellValue = evaluator.evaluate(cell);
                    int type = cellValue.getCellType();
                    if (type == 0 && DateUtil.isCellDateFormatted(cell)) {
                        out = out + cell.getDateCellValue();
                    } else if (type == 0) {
                        out = out + cellValue.getNumberValue();
                    } else if (type == 1) {
                        out = out + cellValue.getStringValue();
                    } else if (type == 4) {
                        out = out + cellValue.getBooleanValue();
                    } else if (type == 5) {
                        /**out = out + cellValue.getErrorValue();*/
                    }
                    break;
                case 3:
                    /**logger.info("Blank cell");*/
                    break;
                case 4:
                    out = out + cell.getBooleanCellValue();
                    break;
                case 5:
                    /**logger.info("error");*/
                    break;
                default:
                    break;
            }

        }
        return out;
    }
}
