package com.brew.home.excel.poi.excel.cailian;

import com.brew.home.excel.poi.excel.cailian.common.AbstractCommonRecognition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.*;


/**
 * 专用模板1:
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/专用模板-1/科目余额表 (3).xlsx
 * <p> 通用模板：
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/丰荣-2021余额表-2.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/丰荣-2021余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/丰荣-2022余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/中恒科-202401-4月发生额及余额表.xlsx
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/用友t13-202401-4月发生额及余额表.xlsx
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/用友u8-2020科目余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/北京金色廊桥文化传播有限公司_余额表(2020年1月).xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/金财互联-余额表(2020年1月).xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/航天A3-潮星余额表(2).XLSX
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/金蝶k3-唐山2018年度科目余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/云代账2-1554300136科目余额表#2017年第1-12期.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/新版金蝶mini-仟化成余额表1.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/易代账-飞凡创意余额表_201712-201712.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/云代账1-盛唐文化17年12月余额表(1)(1).xlsx
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/苗苗-北京世信网航空服务有限公司科目余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/金蝶-伶仃2017年12月科目余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/金蝶mini-伶仃2017年12月科目余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/金蝶kis-17尚学-科目余额表.xlsx
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/东方博大发生额及余额表_2018-07-25-54.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/深蓝色12余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/博尼达12月余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/百奥迈-余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/博远国际 余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/亚和维12月发生额及余额表_2018-07-23.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/布鲁德-余额表-1.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/辅助发生额及余额表_2018-07-20 (1).xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/发生额及余额表_2018-07-20.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/博尼达2018年1-4月余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/博尼达17年1-12月科目余额表.xls
 * <p> /Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/用友t6-余额表2.xlsx
 * @author shaogz
 * @since 2024/8/1 10:48
 */
@Slf4j
public class CommonRecognitionTestV2 extends AbstractCommonRecognition {

    private static final Set<String> HEADER_ROW_FIRST_SET = new HashSet<>(Arrays.asList("科目编码", "科目编号", "科目代码",
            "科目名称", "借方累计", "贷方累计", "期初余额", "期末余额", "本年累计发生额"));


    private static final Set<String> JF_QCMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("期初余额借方", "期初借方", "期初借方余额", "期初余额(借方)"));

    private static final Set<String> DF_QCMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("期初余额贷方", "期初贷方", "期初贷方余额", "期初余额(贷方)"));

    private static final Set<String> BN_JF_LJMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("本年借方累计", "本年累计发生额(借方)", "本年累计发生额借方"));

    private static final Set<String> BN_DF_LJMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("本年贷方累计", "本年累计发生额(贷方)", "本年累计发生额贷方"));

    private static final String[] EXTRACT_ATTRS = new String[]{"subject_code", "subject_name", "jf_qcmny", "df_qcmny", "jf_ljmny", "df_ljmny"};

    public CommonRecognitionTestV2(Sheet sheet) {
        super(sheet, EXTRACT_ATTRS);
    }


    public static void main(String[] args) throws Exception {
        //找首行、末行
        //模板判断...模板1/2/../通用模板
        //结果数据：科目编码、科目名称、期初余额借方、期初余额贷方、本年累计借方、本年累计贷方
        //HSSFWorkbook
        //XSSFWorkbook
        //0~30
        String filepath = "/Users/feiyi/Downloads/导账包需求/测试文件-整理后/通用模板/东方博大发生额及余额表_2018-07-25-54.xls";
//        String filepath = args[3];
        Sheet excelSheet = AbstractCommonRecognition.createExcelSheet(filepath);

        CommonRecognitionTestV2 cailianReadTestV2 = new CommonRecognitionTestV2(excelSheet);
        boolean parseSuccess = cailianReadTestV2.parse();
        if (parseSuccess) {
            //...
            System.out.println("parse success.");
        }

    }


    String subjectCodeValPrevious = null;
    @Override
    public void postHandleOnEachRowRead(Row curRow, HashMap<String, String> curRowData) {
        Cell subjectCodeCell = curRow.getCell(subjectCodeColumn);
        String subjectCodeVal = getSubjectCodeVal(subjectCodeCell);

        boolean aidedItemExist = false;
        if (!StringUtils.isEmpty(subjectCodeVal)) {
            subjectCodeValPrevious = subjectCodeVal;
        } else {
            subjectCodeVal = subjectCodeValPrevious;
            aidedItemExist = true;
        }
        curRowData.put(attrs[0], subjectCodeVal);

        if (aidedItemExist) {
            curRowData.put("fzcode", getAidedItemSubjectCode(curRow.getCell(subjectNameColumn)));
        }
    }

    @Override
    public Set<String> getHeaderRowFirstLocateSet() {
        return HEADER_ROW_FIRST_SET;
    }


    @Override
    public boolean extractDataColumns() {
        //int[] = 期初借方，期初贷方，本年累计借方，本年累计贷方
        boolean singleHeaderRow = (headerRowLastNum - headerRowFirstNum) == 0;
        int[] res;
        if (singleHeaderRow) {
            res = acquireMnyColumnsSingleHeader(sheet, headerRowFirstNum);
        } else {
            res = acquireMnyColumnsMultiHeader(sheet, headerRowFirstNum);
        }
        if (res[0] == -1 || res[1] == -1) {
            log.error("表头匹配失败，期初余额借方/贷方匹配失败");
            return false;
        }

        this.columns = new int[res.length+2];
        this.columns[0] = this.subjectCodeColumn;
        this.columns[1] = this.subjectNameColumn;
        for(int i=2, j=0; i<this.columns.length && j<res.length; i++, j++) {
            this.columns[i] = res[j];
        }
        return true;
    }

    @Override
    public boolean readDataInTemplateX(int template) {
        //template作为拓展字段，目前不用
        Row tableHeaderFirst = sheet.getRow(headerRowFirstNum);
        short tableHeaderFirstCellNum = tableHeaderFirst.getLastCellNum();
        //找到 '余额方向'
        int yefx = acquireYefxInTemplate1(tableHeaderFirst, tableHeaderFirstCellNum);
        //找到期初余额、本年累计发生额借方、本年累计发生额贷方
        int[] mnyColumns = acquireMnyColumnsInTemplate1(sheet, headerRowFirstNum);
        int columnQcMny = mnyColumns[0];
        if (columnQcMny == -1) {
            log.error("表头匹配失败，期初余额匹配失败");
            return false;
        }
        this.columns = new int[mnyColumns.length+1];
        this.columns[0] = this.subjectCodeColumn;
        this.columns[1] = this.subjectNameColumn;
        this.columns[2] = -1;
        this.columns[3] = -1;
        for(int i=4, j=1; i<this.columns.length && j<mnyColumns.length; i++, j++) {
            this.columns[i] = mnyColumns[j];
        }

        //read data
        int lastRowNum = sheet.getLastRowNum();
        for (int i = dataRowFirstNum; i<lastRowNum; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            if (Objects.equals("借", getCellVal(curRow.getCell(yefx)))) {
                this.columns[2] = columnQcMny;
            } else if (Objects.equals("贷", getCellVal(curRow.getCell(yefx)))) {
                this.columns[3] = columnQcMny;
            }

            //项目编码必须是纯数字或空
            Cell subjectCodeCell = sheet.getRow(i) == null? null : sheet.getRow(i).getCell(subjectCodeColumn);
            Cell subjectNameCell = sheet.getRow(i) == null? null : sheet.getRow(i).getCell(subjectNameColumn);
            if (isIllegalSubjectItems(subjectCodeCell, subjectNameCell)) {
                continue;
            }
            HashMap<String, String> curRowData = new HashMap<>();
            for (int j=0; j<this.columns.length; j++) {
                if (this.columns[j] == -1) {
                    continue;
                }
                curRowData.put(attrs[j], getCellString(curRow.getCell(this.columns[j])));
            }
            data.add(curRowData);
            log.info("curRowData:{}", curRowData);
        }
        return true;
    }

    /**
     * getTemplate  获取模板类型， 目前只有通用和特殊-1
     * @return int 0 表示通用模板，1及其他表示特殊模板
     * @author shaogz 2024/8/7 10:26
     */
    @Override
    public int getTemplate() {
        //表头里有'余额方向'，且往下只有借/贷，则匹配成功
        boolean match1 = false;
        int yefxColumnNum = -1;
        for (int i=headerRowFirstNum; i<=headerRowLastNum; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow==null||match1) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            for (int j=0; j<=lastCellNum; j++) {
                Cell curCell = curRow.getCell(j);
                String cellStringZh = getCellStringZh(curCell);
                if (Objects.equals("余额方向", cellStringZh)) {
                    match1 = true;
                    yefxColumnNum = j;
                    break;
                }
            }
        }
        if (!match1) {
            return 0;
        }
        //往下读三行
        for (int i=headerRowLastNum+1; i<headerRowLastNum+4; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow==null) {
                continue;
            }
            String cellStringZh = getCellStringZh(curRow.getCell(yefxColumnNum));
            if (!Objects.equals("借", cellStringZh) && !Objects.equals("贷", cellStringZh) && !StringUtils.isEmpty(cellStringZh)) {
                return 0;
            }
        }
        return 1;
    }

    private String getAidedItemSubjectCode(Cell aidedItemName) {
        String cellString = getCellStringNullable(aidedItemName);
        if (StringUtils.isEmpty(cellString)) {
            return null;
        }
        Deque<Character> deque1 = new ArrayDeque<>();
        Deque<Character> deque2 = new ArrayDeque<>();
        StringBuilder res = new StringBuilder();
        for (char each: cellString.toCharArray()) {
            if (each=='【' || each=='（' || each == '[' || each=='(') {
                deque1.push(each);
            } else if (each == '】' && Objects.equals(deque1.peek(), '【')) {
                deque1.pop();
            } else if (each == '）' && Objects.equals(deque1.peek(), '（')) {
                deque1.pop();
            } else if (each == ']' && Objects.equals(deque1.peek(), '[')) {
                deque1.pop();
            } else if (each == ')' && Objects.equals(deque1.peek(), '(')) {
                deque1.pop();
            } else if (!deque1.isEmpty()){
                deque2.push(each);
            }
        }
        if (deque1.isEmpty()) {
            Iterator<Character> characterIterator = deque2.descendingIterator();
            while (characterIterator.hasNext()) {
                res.append(characterIterator.next());
            }
            return res.toString();
        }
        return null;
    }

    private int acquireYefxInTemplate1(Row tableHeaderFirst, short tableHeaderFirstCellNum) {
        int yefx = -1;
        for (int i = 0; i<= tableHeaderFirstCellNum; i++) {
            String cellStringZh = getCellStringZh(tableHeaderFirst.getCell(i));
            if (Objects.equals("余额方向", cellStringZh)) {
                yefx = i;
                break;
            }
        }
        return yefx;
    }

    private int[] acquireMnyColumnsInTemplate1(Sheet sheet, int headerRowFirstNum) {
        int[] res = new int[]{-1, -1, -1};
        Row headerRowFirst = sheet.getRow(headerRowFirstNum);
        short rowFirstLastCellNum = headerRowFirst.getLastCellNum();
        //期初余额
        int qcye = -1;
        //本年累计
        int bnlj = -1;
        for (int i=0; i<rowFirstLastCellNum; i++) {
            Cell curCell = headerRowFirst.getCell(i);
            String cellStringZh = getCellStringZh(curCell);
            if(Objects.equals("期初余额", cellStringZh)) {
                qcye = i;
            }else if (Objects.equals("本年累计发生额", cellStringZh)) {
                bnlj = i;
            }
        }
        res[0] = qcye;

        Row headerRowSec = sheet.getRow(headerRowFirstNum + 1);
        if (Objects.equals("借方", getCellStringZh(headerRowSec.getCell(bnlj))) &&
                Objects.equals("贷方", getCellStringZh(headerRowSec.getCell(bnlj+1)))) {
            res[1] = bnlj;
            res[2] = bnlj+1;
        }
        return res;
    }

    private int[] acquireMnyColumnsSingleHeader(Sheet sheet, int headerRowFirstNum) {
        int[] res = new int[]{-1, -1, -1, -1};
        Row curRow = sheet.getRow(headerRowFirstNum);
        short lastCellNum = curRow.getLastCellNum();
        for (int i=0; i<=lastCellNum; i++) {
            Cell curCell = curRow.getCell(i);
            String cellStringZh = getCellStringZh(curCell);
            if (JF_QCMNY_FUNZZY_SET.contains(cellStringZh)) {
                res[0] = i;
            } else if (DF_QCMNY_FUNZZY_SET.contains(cellStringZh)) {
                res[1] = i;
            } else if (BN_JF_LJMNY_FUNZZY_SET.contains(cellStringZh)) {
                res[2] = i;
            } else if (BN_DF_LJMNY_FUNZZY_SET.contains(cellStringZh)) {
                res[3] = i;
            }
        }
        return res;
    }

    private void acquireMnyColumnsMultiHeaderQc(Sheet sheet, int headerRowFirstNum, Row rowFirst, short rowFirstLastCellNum, int[] res) {
        //期初
        int qcmnyColumn = -1;
        for (int i=0; i<=rowFirstLastCellNum;i++){
            String cellStringZh = getCellStringZh(rowFirst.getCell(i));
            if (Objects.equals("期初", cellStringZh) || Objects.equals("期初余额", cellStringZh)) {
                qcmnyColumn = i;
                break;
            }
        }
        String cellStr1 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(qcmnyColumn));
        String cellStr2 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(qcmnyColumn+1));
        if ((Objects.equals("借方", cellStr1)&& Objects.equals("贷方", cellStr2))) {
            res[0] = qcmnyColumn;
            res[1] = qcmnyColumn+1;
        }
    }

    private int[] acquireMnyColumnsMultiHeader(Sheet sheet, int headerRowFirstNum) {
        int[] res = new int[]{-1, -1, -1, -1};
        //part-1:多行表头的找：期初借方、期初贷方
        Row rowFirst = sheet.getRow(headerRowFirstNum);
        short rowFirstLastCellNum = rowFirst.getLastCellNum();
        acquireMnyColumnsMultiHeaderQc(sheet, headerRowFirstNum, rowFirst, rowFirstLastCellNum, res);
        //part-2:多行表头的找：本年累计借方、本年累计贷方
        for (int i=0; i<=rowFirstLastCellNum;i++){
            String cellStringZh = getCellStringZh(rowFirst.getCell(i));
            if (Objects.equals("借方累计", cellStringZh)) {
                res[2] = i;
            } else if (Objects.equals("贷方累计", cellStringZh)) {
                res[3] = i;
            }
        }
        if (res[2] != -1 && res[3] != -1) {
            return res;
        }
        //本年累计
        int bnljmnyColumn = -1;
        for (int i=0; i<=rowFirstLastCellNum;i++){
            String cellStringZh = getCellStringZh(rowFirst.getCell(i));
            if (Objects.equals("本年累计", cellStringZh) || Objects.equals("本年累计发生额", cellStringZh)) {
                bnljmnyColumn = i;
                break;
            }
        }
        if (bnljmnyColumn==-1) {
            return res;
        }
        String cellStr3 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(bnljmnyColumn));
        String cellStr4 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(bnljmnyColumn+1));
        if ((Objects.equals("借方", cellStr3)&& Objects.equals("贷方", cellStr4))) {
            res[2] = bnljmnyColumn;
            res[3] = bnljmnyColumn+1;
        }
        return res;
    }


}
