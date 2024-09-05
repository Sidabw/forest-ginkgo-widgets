package com.brew.home.excel.poi.excel.cailian.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author shaogz
 * @since 2024/8/7 09:31
 */
@Slf4j
public abstract class AbstractCommonRecognition {

    protected final Sheet sheet;

    protected int headerRowFirstNum;

    protected int headerRowLastNum;

    protected int dataRowFirstNum;

    protected int subjectCodeColumn;

    protected int subjectNameColumn;

    protected int[] columns;

    protected final String[] attrs;

    protected final List<HashMap<String, String>> data;

    protected static final Set<String> SUBJECT_CODE_FUZZY_SET = new HashSet<>(Arrays.asList("科目编码", "科目编号", "科目代码"));

    protected AbstractCommonRecognition(Sheet sheet, String[] attrs) {
        this.sheet = sheet;
        this.headerRowFirstNum = -1;
        this.headerRowLastNum = -1;
        this.dataRowFirstNum = -1;

        this.subjectCodeColumn = -1;
        this.subjectNameColumn = -1;

        this.attrs = attrs;
        this.data = new ArrayList<>(attrs.length);
    }

    public abstract Set<String> getHeaderRowFirstLocateSet();

    public abstract boolean extractDataColumns();

    public boolean parse() {
        //找表头首行
        boolean headerRowFirstLocateSuccess = acquireHeaderRowFirstNum();
        if (!headerRowFirstLocateSuccess) {
            return false;
        }
        //预处理
        preHandle();
        //找表头科目编码、科目名称列
        boolean subjectCodeAndNameLocateSuccess = acquireSubjectCodeAndName();
        if (!subjectCodeAndNameLocateSuccess) {
            return false;
        }
        //找表头末行
        boolean headerRowLastNumLocateSuccess = acquireHeaderRowLastNum();
        if (!headerRowLastNumLocateSuccess) {
            return false;
        }
        //区分是否特殊模板
        int template = getTemplate();
        if (template>0) {
            return readDataInTemplateX(template);
        }
        //进入通用模板逻辑
        //找表头，子类负责
        boolean extractDataColumnsSuccess = extractDataColumns();
        if (!extractDataColumnsSuccess) {
            return false;
        }
        //通用的读数据
        readDataCommon();
        return true;
    }

    protected void readDataCommon() {
        //read data
        int lastRowNum = sheet.getLastRowNum();
        for (int i = dataRowFirstNum; i<=lastRowNum; i++) {
            if (sheet.getRow(i) == null) {
                continue;
            }
            Cell subjectCodeCell = sheet.getRow(i).getCell(subjectCodeColumn);
            Cell subjectNameCell = sheet.getRow(i).getCell(subjectNameColumn);
            if (isIllegalSubjectItems(subjectCodeCell, subjectNameCell)) {
                continue;
            }

            HashMap<String, String> curRowData = new HashMap<>();
            Row curRow = sheet.getRow(i);
            curRowData.put(attrs[0], getSubjectCodeVal(subjectCodeCell));
            for (int j=1; j<this.columns.length; j++) {
                if (this.columns[j] == -1) {
                    continue;
                }
                curRowData.put(attrs[j], getCellString(curRow.getCell(this.columns[j])));
            }
            data.add(curRowData);

            postHandleOnEachRowRead(curRow, curRowData);
            log.info("curRowData:{}", curRowData);
        }
    }

    protected void preHandle() {
        //override if required
    }

    public int getTemplate() {
        //override if required
        return 0;
    }

    public boolean readDataInTemplateX(int template) {
        //override if required
        return false;
    }

    protected void postHandleOnEachRowRead(Row curRow, HashMap<String, String> curRowData) {
        //override if required
    }

    protected Set<String> getSubjectCodeFuzzySet() {
        //override if required
        return SUBJECT_CODE_FUZZY_SET;
    }

    protected boolean acquireHeaderRowFirstNum() {
        Set<String> headerRowFirstLocateSet = getHeaderRowFirstLocateSet();
        for (int i = 0; i<10; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            for (int j = 0; j<=lastCellNum; j++) {
                Cell curCell = curRow.getCell(j);
                String curCellVal = getCellString(curCell);
                if (headerRowFirstLocateSet.contains(curCellVal)) {
                    this.headerRowFirstNum = i;
                    return true;
                }
            }
        }
        log.error("表头匹配失败, 行首匹配失败");
        return false;
    }

    protected boolean acquireSubjectCodeAndName() {
        Set<String> subjectCodeFuzzySet = getSubjectCodeFuzzySet();
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            for (int j = 0; j<=lastCellNum; j++) {
                Cell curCell = curRow.getCell(j);
                String curCellVal = getCellString(curCell);
                if (subjectCodeFuzzySet.contains(curCellVal)) {
                    this.subjectCodeColumn = j;
                } else if (Objects.equals("科目名称", curCellVal)) {
                    this.subjectNameColumn = j;
                }
            }
        }
        if (this.subjectCodeColumn == -1 || this.subjectNameColumn == -1) {
            log.error("表头匹配失败，科目编码/名称匹配失败");
            return false;
        }
        return true;
    }


    protected boolean acquireHeaderRowLastNum() {
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            Cell curCell = curRow.getCell(subjectCodeColumn);
            String subjectCodeVal = getSubjectCodeVal(curCell);
            if (isPureNumber(subjectCodeVal)) {
                this.headerRowLastNum = i-1;
                this.dataRowFirstNum = this.headerRowLastNum + 1;
                return true;
            }
        }
        log.error("表头匹配失败, 行尾匹配失败");
        return false;
    }

    protected String getCellStringZh(Cell curCell) {
        String original = getCellString(curCell);
        StringBuilder res = new StringBuilder();
        for (char curChar : original.toCharArray()) {
            if (curChar>127) {
                res.append(curChar);
            }
        }
        return res.toString();
    }

    protected String getCellString(Cell curCell) {
        return getCellString(curCell, "");
    }

    protected String getCellStringNullable(Cell curCell) {
        return getCellString(curCell, null);
    }

    protected String getCellString(Cell curCell, String defaultVal) {
        if (curCell == null) {
            return defaultVal;
        }
        String formatCellValue = new DataFormatter().formatCellValue(curCell);
        return rmBlank(formatCellValue);
    }

    protected String rmBlank(String original) {
        return original.replace("　","").replace(" ", "");
    }


    protected String getCellVal(Cell cell) {
        String formatCellValue = new DataFormatter().formatCellValue(cell);
        return rmBlank(formatCellValue);
    }

    protected boolean isPureNumber(String cellStr) {
        if (StringUtils.isEmpty(cellStr)) {
            return false;
        }
        for (char each : cellStr.toCharArray()) {
            if (!isPureNumber(each)) {
                return false;
            }
        }
        return true;
    }

    protected boolean isPureNumber(char character) {
        return character>=48&&character<=57;
    }



    protected String getSubjectCodeVal(Cell curCell) {
        String cellVal = getCellString(curCell, "");
        //对应金蝶k3/金蝶kis/及其它的科目编码特殊处理
        //测试文件：
        //金蝶k3前缀
        //金蝶kis前缀
        //辅助发生额及余额表_2018-07-20(1).xls
        return cellVal.replace(".", "").replace("_", "");
    }

    protected static Sheet createExcelSheet(String filepath) throws IOException {
        //for local test usage
        Path path = Paths.get(filepath);
        log.info("filepath:{}", filepath);
        Workbook rwb;
        if(filepath.toLowerCase().endsWith(".xls")) {
            rwb = new HSSFWorkbook(Files.newInputStream(path));
        } else {
            rwb = new XSSFWorkbook(Files.newInputStream(path));
        }
        return rwb.getSheetAt(0);
    }

    protected boolean isIllegalSubjectItems(Cell subjectCodeCell, Cell subjectNameCell) {
        //项目编码和项目名称不可同时为空
        boolean subjectCodeJudge = isEmpty(subjectCodeCell);
        boolean subjectNameJudge = isEmpty(subjectNameCell);
        //都为true，则为true
        if (subjectCodeJudge && subjectNameJudge) {
            return true;
        }
        //项目编码必须是纯数字或空；
        if(!isSubjectCodePureNumberOrEmpty(subjectCodeCell)) {
            return true;
        }
        //项目编码为空的情况下，项目名称不可是小计、合计等
        String subjectNameVal = getCellVal(subjectNameCell);
        return subjectNameVal.contains("小计") || subjectNameVal.contains("合计");
    }

    protected boolean isEmpty(Cell cell) {
        if(cell==null) {
            return true;
        }
        String cellStr = getCellVal(cell);
        return StringUtils.isEmpty(cellStr);
    }

    protected boolean isSubjectCodePureNumberOrEmpty(Cell subjectCodeCell) {
        String subjectCodeVal = getSubjectCodeVal(subjectCodeCell);
        if (StringUtils.isEmpty(subjectCodeVal)) {
            return true;
        }
        for (char each : subjectCodeVal.toCharArray()) {
            if (!(each>=48&&each<=57)) {
                return false;
            }
        }
        return true;
    }

}
