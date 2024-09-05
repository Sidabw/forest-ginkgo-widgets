package com.brew.home.excel.poi.excel.bak;

import lombok.extern.slf4j.Slf4j;
/**
 * @author shaogz
 * @since 2024/7/30 17:16
 */
@Slf4j
public class CailianReadTest {

//    public static void main(String[] args) throws Exception{
//        String filePath = "/Users/feiyi/Downloads/导账包需求/丰庆燕春/科目余额表 (3).xlsx";
////        String filePath = "/Users/feiyi/Downloads/导账包需求/26-财政资金20240604/3-准备公司的近3年及本年的科目期初余额表/202401-4月发生额及余额表.xlsx";
//        XSSFWorkbook rwb = new XSSFWorkbook(new FileInputStream(filePath));
//        XSSFSheet sheet = rwb.getSheetAt(0);
//        int pinpoint = tryAcquireBigClass(sheet);
//        if (pinpoint == -1) {
//            log.info("大类匹配失败.");
//            return;
//        }
//        int templateNum = tryAcquireTemplateNum(sheet, pinpoint);
//        if (templateNum == -1) {
//            log.info("子类模板匹配失败.");
//            return;
//        }
//        int[] commonColumns = acquireCommonColumns(sheet, pinpoint);
//        int columnSubjectCode = commonColumns[0];
//        int columnSubjectName = commonColumns[1];
//        int columnQcMny = commonColumns[2];
//        if (templateNum == 1) {
//            XSSFRow tableHeaderFirst = sheet.getRow(pinpoint);
//            short tableHeaderFirstCellNum = tableHeaderFirst.getLastCellNum();
//            int yefx = -1;
//            for (int i = 0; i<=tableHeaderFirstCellNum; i++) {
//                XSSFCell curCell = tableHeaderFirst.getCell(i);
//                if(curCell==null) {
//                    continue;
//                }
//                if (Objects.equals("余额方向", curCell.getStringCellValue())) {
//                    yefx = i;
//                    break;
//                }
//            }
//
//            int lastRowNum = sheet.getLastRowNum();
//            for (int i = pinpoint+2; i<lastRowNum;i++) {
//                XSSFRow curRow = sheet.getRow(i);
//                if (curRow==null) {
//                    continue;
//                }
//                XSSFCell subjectCodeCell = curRow.getCell(columnSubjectCode);
//                if (StringUtils.isEmpty(rmBlank(subjectCodeCell == null ? "":subjectCodeCell.getStringCellValue()))) {
//                    continue;
//                }
//                log.info("columnSubjectCode：{}", curRow.getCell(columnSubjectCode));
//                log.info("columnSubjectName：{}", curRow.getCell(columnSubjectName));
//
//                if (Objects.equals("借", curRow.getCell(yefx).getStringCellValue())) {
//                    log.info("columnJfMny：{}", curRow.getCell(columnQcMny));
//                } else if (Objects.equals("贷", curRow.getCell(yefx).getStringCellValue())) {
//                    log.info("columnDfMny：{}", curRow.getCell(columnQcMny));
//                }
//            }
//
//            return;
//        }
//
//
//        if (templateNum == 2) {
//            int columnJfMny;
//            int columnDfMny;
//            String tmpStr = sheet.getRow(pinpoint + 1).getCell(columnQcMny).getStringCellValue();
//            if (Objects.equals("借方", tmpStr)) {
//                columnJfMny = columnQcMny;
//                columnDfMny = columnQcMny + 1;
//            } else {
//                columnJfMny = columnQcMny + 1;
//                columnDfMny = columnQcMny;
//            }
//            //read data
//            int lastRowNum = sheet.getLastRowNum();
//            String subjectCodeValPrevious = null;
//            for (int i = pinpoint +2; i<lastRowNum; i++) {
//                XSSFRow curRow = sheet.getRow(i);
//                if (curRow==null) {
//                    continue;
//                }
//                XSSFCell subjectCodeCell = curRow.getCell(columnSubjectCode);
//                if (!isPureNumberOrEmpty(subjectCodeCell)) {
//                    continue;
//                }
//
//                String subjectCodeVal = rmBlank(subjectCodeCell.getStringCellValue());
//                if (!StringUtils.isEmpty(subjectCodeVal)) {
//                    subjectCodeValPrevious = subjectCodeVal;
//                } else {
//                    subjectCodeVal = subjectCodeValPrevious;
//                }
//                log.info("columnSubjectCode：{}", subjectCodeVal);
//                log.info("columnSubjectName：{}", curRow.getCell(columnSubjectName));
//                log.info("columnJfMny：{}", curRow.getCell(columnJfMny));
//                log.info("columnDfMny：{}", curRow.getCell(columnDfMny));
//            }
//
//        }
//
//    }


//    private static int[] acquireCommonColumns(XSSFSheet sheet, int pinpoint) {
//        int[] res = new int[]{-1, -1, -1};
//        XSSFRow tableHeaderFirst = sheet.getRow(pinpoint);
//        short tableHeaderFirstCellNum = tableHeaderFirst.getLastCellNum();
//        for (int i = 0; i<=tableHeaderFirstCellNum; i++) {
//            XSSFCell curCell = tableHeaderFirst.getCell(i);
//            if(curCell==null) {
//                continue;
//            }
//            if (Objects.equals("科目编码", curCell.getStringCellValue())) {
//                res[0] = i;
//            } else if (Objects.equals("科目名称", curCell.getStringCellValue())) {
//                res[1] = i;
//            } else if (Objects.equals("期初余额", curCell.getStringCellValue())) {
//                res[2] = i;
//            }
//        }
//        return res;
//    }
//
//
//    private static int tryAcquireBigClass(XSSFSheet sheet) {
//        int pinpoint = -1;
//        for (int i = 0; i < 10; i++) {
//            String rowStr = getCellMergedStr(sheet, i);
//            if (rowStr.contains("科目编码") &&rowStr.contains("科目名称") &&rowStr.contains("期初余额")) {
//                pinpoint = i;
//                break;
//            }
//        }
//        return pinpoint;
//    }
//
//    private static int tryAcquireTemplateNum(XSSFSheet sheet, int pinpoint) {
//        int templateNum = -1;
//        String rowStr = getCellMergedStr(sheet, pinpoint);
//        if (rowStr.contains("余额方向")) {
//            templateNum = 1;
//            return templateNum;
//        }
//
//        //尝试往下读一行，在期初余额下面找借方和贷方
//        int qcMny = -1;
//        XSSFRow curRow = sheet.getRow(pinpoint);
//        short lastCellNum = curRow.getLastCellNum();
//        for (int i=0; i<=lastCellNum; i++) {
//            XSSFCell curCell = curRow.getCell(i);
//            if (curCell==null) {
//                continue;
//            }
//            if (Objects.equals("期初余额", curCell.getStringCellValue())) {
//                qcMny = i;
//                break;
//            }
//        }
//
//        String cellStr1 = sheet.getRow(pinpoint + 1).getCell(qcMny).getStringCellValue();
//        String cellStr2 = sheet.getRow(pinpoint + 1).getCell(qcMny+1).getStringCellValue();
//        if ((Objects.equals("借方", cellStr1)&& Objects.equals("贷方", cellStr2))||
//                Objects.equals("贷方", cellStr1)&& Objects.equals("借方", cellStr2)) {
//            templateNum = 2;
//        }
//        return templateNum;
//    }
//
//    private static String getCellMergedStr(XSSFSheet sheet, int i) {
//        XSSFRow row = sheet.getRow(i);
//        if (row == null) {
//            return "";
//        }
//        StringBuilder rowStrBuilder = new StringBuilder();
//        Iterator<Cell> cellIterator = row.cellIterator();
//        while (cellIterator.hasNext()) {
//            String stringCellValue = cellIterator.next().getStringCellValue();
//            if (StringUtils.isEmpty(stringCellValue)) {
//                continue;
//            }
//            rowStrBuilder.append(stringCellValue);
//        }
//        return rowStrBuilder.toString();
//    }

}
