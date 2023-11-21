package com.stg.b2b.util;


import com.aspose.cells.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.aspose.cells.CellValueType.*;

public class XlsUtil {
    private static final Logger logger = LoggerFactory.getLogger(XlsUtil.class);

    public static Map<String, ArrayList<String>> uploadExcels(InputStream inputStream)  {
        return uploadExcels(inputStream,1);
    }

    public static Map<String, ArrayList<String>> uploadExcels(InputStream inputStream ,int startIndex)  {
        Map<String, ArrayList<String>> map = new HashMap<>();
        try {
            Workbook workbook = new Workbook(inputStream);
            Worksheet worksheet = workbook.getWorksheets().get(0);
            Cells cells = worksheet.getCells();
            for (int row = 0; row <= cells.getMaxDataRow(); row++) {
                if(row>=(startIndex-1)){
                    for (int column = 0; column <= cells.getMaxDataColumn(); column++) {
                        Cell cell = cells.get(row, column);
                        if (row == (startIndex -1)) {
                            if (!map.containsKey(processCellValue(cell))) {
                                String currentHeader=processCellValue(cell);
                                if(!currentHeader.isEmpty()){
                                    map.put(currentHeader, new ArrayList<>());
                                }
                            }
                        }else{
                            String currentHeader = processCellValue(cells.get(startIndex-1, column));
                            ArrayList<String> newList = map.get(currentHeader);
                            newList.add(processCellValue(cell));
                            map.put(String.valueOf(cells.get(startIndex-1, column)), newList);
                        }
                    }
                }
            }
            workbook.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    private static String processCellValue(Cell cell) {
        String out="";
        int valueType = cell.getType();
        switch (valueType) {
            case IS_STRING -> out = out + cell.getDisplayStringValue();
            case IS_DATE_TIME -> out = out + cell.getDateTimeValue();
            case IS_NUMERIC -> out = out + cell.getDoubleValue();
            case IS_BOOL -> out = out + cell.getBoolValue();
            case IS_ERROR -> logger.debug("Cell value has error in the file");
            case IS_NULL -> logger.debug("Cell value is null in the file");
            default -> logger.debug("Unknown case");
        }
        return out;
    }
}

