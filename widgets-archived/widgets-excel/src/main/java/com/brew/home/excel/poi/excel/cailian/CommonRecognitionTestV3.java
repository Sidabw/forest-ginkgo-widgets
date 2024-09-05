package com.brew.home.excel.poi.excel.cailian;

import com.brew.home.excel.poi.excel.cailian.common.AbstractCommonRecognition;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.*;

/**
 * @author shaogz
 * @since 2024/8/6 15:00
 */
@Slf4j
public class CommonRecognitionTestV3 extends AbstractCommonRecognition {

    private static final Set<String> HEADER_ROW_FIRST_SET = new HashSet<>(Arrays.asList("科目编码", "科目编号", "科目代码",
            "科目名称", "凭证字号", "凭证", "凭证号数", "凭证号", "凭证字"));

    private static final Set<String> JF_MNY_FUNZZY_SET = new HashSet<>(Arrays.asList("借方", "借方金额", "原币金额借方"));

    private static final Set<String> DF_MNY_FUNZZY_SET = new HashSet<>(Arrays.asList("贷方", "贷方金额", "原币金额贷方"));

    private static final Set<String> DATE_FUZZY_SET = new HashSet<>(Arrays.asList("日期", "制单日期"));

    private static final Set<String> PZ_NO_FUZZY_SET = new HashSet<>(Arrays.asList("凭证号数", "凭证号", "凭证字号", "凭证字", "凭证类别"));


    private static final String[] EXTRACT_ATTRS = new String[]{
            "subj_code", //科目编码
            "subj_name",  //科目名称
            "dpz_date", //凭证制单日期
            "pz_no", //凭证字号
            "zy", //摘要
            "jf_mny", //借方金额
            "df_mny", //贷方金额
            "nnum", //数量
            "nprice", //价格
            "fzmc", //辅助名称（给列是'辅助项'用的）
            "fzxm", //辅助项目（给列是'客户/部门/职员/供应商/项目/存货'用的）
            "fzmc" //辅助名称（按&合并fzxm后的值）
    };

    /**
     * 由于在preHandle中追加列时，无法准确的拿到lastCellColumn(多行表头的case下)，所以改为从固定50开始往后追加。
     */
    private int lastCellColumn = 50;

    protected CommonRecognitionTestV3(Sheet sheet) {
        super(sheet, EXTRACT_ATTRS);
    }

    public static void main(String[] args) throws IOException {
        //结果数据：科目编码、科目名称、日期、凭证号数、摘要、借方金额、贷方金额、数量、价格
        Sheet excelSheet = AbstractCommonRecognition.createExcelSheet(args[0]);
        CommonRecognitionTestV3 cailianReadTestV3 = new CommonRecognitionTestV3(excelSheet);
        boolean parseSuccess = cailianReadTestV3.parse();
        if (parseSuccess) {
            System.out.println("//xxx");
        }
    }

    @Override
    public Set<String> getHeaderRowFirstLocateSet() {
        return HEADER_ROW_FIRST_SET;
    }

    @Override
    protected void preHandle() {
        //把没有科目名称/科名编码的，给创建出来
        boolean subjectCodeAndNameExists = acquireSubjectCodeAndName();
        int[] mergedSubject = acquireSubjectCodeAndNameMerged();
        if (!subjectCodeAndNameExists && mergedSubject[0] >= 0 && mergedSubject[1] >= 0) {
            splitSubjectCodeAndName(mergedSubject);
        }
        //把凭证类别、凭证号合并，放到最后一列
        int[] splitPzNo = acquireSplitPaNo();
        if (splitPzNo[0]>=0 && splitPzNo[1]>=0 && splitPzNo[2]>=0) {
            mergePzNo(splitPzNo);
        }
        //同时存在方向、金额，则转换为标准的借方金额、贷方金额
        int[] mnyAndDirection = acquireMnyAndDirection();
        if (mnyAndDirection[0]>=0 && mnyAndDirection[1]>=0 && mnyAndDirection[2]>=0) {
            convert2StdDfJfMny(mnyAndDirection);
        }
        //规整辅助项
        int[] aidedItemArr = acquireAidedItem();
        if (!isIllegal(aidedItemArr)) {
            convert2StdAidedItem(aidedItemArr);
        }
        //规整小蜜蜂敬民模板
        int[] templateOfXiaomifengJM = isTemplateOfXiaomifengJM();
        if (!isIllegal(templateOfXiaomifengJM)) {
            convert2StdDatePzNo(templateOfXiaomifengJM);
        }
    }

    String dpzDateValPrevious = null;
    String pzNoValPrevious = null;
    @Override
    public void postHandleOnEachRowRead(Row curRow, HashMap<String, String> curRowData) {
        //2,dpz_date
        String dpzDateVal = getCellString(curRow.getCell(columns[2]));
        if (!StringUtils.isEmpty(dpzDateVal)) {
            dpzDateValPrevious = dpzDateVal;
        } else {
            curRowData.put(attrs[2], dpzDateValPrevious);
        }
        String pzNoVal = getCellString(curRow.getCell(columns[3]));
        //3,pz_no
        if (!StringUtils.isEmpty(pzNoVal)) {
            pzNoValPrevious = pzNoVal;
        } else {
            curRowData.put(attrs[3], pzNoValPrevious);
        }
    }

    @Override
    public boolean extractDataColumns() {
        this.columns = new int[this.attrs.length];
        Arrays.fill(this.columns, -1);
        this.columns[0] = this.subjectCodeColumn;
        this.columns[1] = this.subjectNameColumn;

        for (int i=headerRowFirstNum; i<=headerRowLastNum; i++) {
            Row curRow = sheet.getRow(i);
            short lastCellNum = curRow.getLastCellNum();
            for (int j=0; j<=lastCellNum; j++) {
                Cell curCell = curRow.getCell(j);
                String cellStringZh = getCellStringZh(curCell);
                if (DATE_FUZZY_SET.contains(cellStringZh)) {
                    this.columns[2] = j;
                } else if (PZ_NO_FUZZY_SET.contains(cellStringZh)) {
                    this.columns[3] = j;
                } else if (Objects.equals("摘要", cellStringZh)) {
                    this.columns[4] = j;
                } else if (JF_MNY_FUNZZY_SET.contains(cellStringZh)) {
                    this.columns[5] = j;
                } else if (DF_MNY_FUNZZY_SET.contains(cellStringZh)) {
                    this.columns[6] = j;
                } else if (Objects.equals("数量", cellStringZh)) {
                    this.columns[7] = j;
                } else if (Objects.equals("价格", cellStringZh)) {
                    this.columns[8] = j;
                } else if (Objects.equals("辅助项", cellStringZh)) {
                    this.columns[9] = j;
                } else if (Objects.equals("辅助项目", cellStringZh)) {
                    this.columns[10] = j;
                } else if (Objects.equals("辅助名称", cellStringZh)) {
                    this.columns[11] = j;
                }
            }

        }
        //除了最后两列其他均不可为空
        for (int i=0; i<=6; i++) {
            if (this.columns[i] == -1) {
                log.error("表头匹配失败，{} 匹配失败", this.attrs[i]);
                return false;
            }
        }
        return true;
    }

    private void convert2StdAidedItem(int[] aidedItemArr) {
        Row aidedItemRow = sheet.getRow(aidedItemArr[0]);
        int fzxmColumn = ++lastCellColumn;
        int fzmcColumn = ++lastCellColumn;
        aidedItemRow.createCell(fzxmColumn).setCellValue("辅助项目");
        aidedItemRow.createCell(fzmcColumn).setCellValue("辅助名称");
        String fzxmVal = getCellString(aidedItemRow.getCell(aidedItemArr[1])) + "&" + getCellString(aidedItemRow.getCell(aidedItemArr[2]));

        int lastRowNum = sheet.getLastRowNum();
        for (int i=aidedItemArr[0]+1; i<=lastRowNum; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            curRow.createCell(fzxmColumn).setCellValue(fzxmVal);
            String fzmcValPart1 = getCellString(curRow.getCell(aidedItemArr[1]));
            String fzmcValPart2 = getCellString(curRow.getCell(aidedItemArr[2]));
            if (fzmcValPart1.isEmpty() && fzmcValPart2.isEmpty()) {
                continue;
            }
            curRow.createCell(fzmcColumn).setCellValue(fzmcValPart1 + "\\" + fzmcValPart2);
        }

    }

    private int[] acquireAidedItem() {
        //客户,供应商,职员,项目,部门,存货
        //找到对应列和对应行
        List<Integer> aidedItemList = new LinkedList<>();
        int aidedItemRowNum = -1;
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow==null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            for (int j=0; j<=lastCellNum; j++) {
                String cellVal = getCellVal(curRow.getCell(j));
                switch (cellVal) {
                    case "客户":
                    case "供应商":
                    case "职员":
                    case "项目":
                    case "部门":
                    case "存货":
                        aidedItemList.add(j);
                        aidedItemRowNum = i;
                        break;
                    default:
                        break;
                }
            }
        }
        if (aidedItemList.isEmpty()) {
            return new int[]{-1};
        }
        //往下读5行，找到有值的列
        Set<Integer> aidedItemSet = new HashSet<>();
        for (int i=aidedItemRowNum; i<aidedItemRowNum+5;i++) {
            Row curRow = sheet.getRow(i);
            if (curRow==null) {
                continue;
            }
            aidedItemList.forEach(e-> {
                String cellString = getCellString(curRow.getCell(e));
                if (!cellString.isEmpty()) {
                    aidedItemSet.add(e);
                }
            });
        }
        if (aidedItemSet.isEmpty()) {
            return new int[]{-1};
        }
        //一列的情况后续的处理就不用了
        if (aidedItemSet.size()==1) {
            return new int[]{-1};
        }
        //返回三项内容：标头定位行标、两个辅助项的列标
        Iterator<Integer> aidedItemIterator = aidedItemSet.iterator();
        return new int[]{aidedItemRowNum, aidedItemIterator.next(), aidedItemIterator.next()};
    }


    private void convert2StdDatePzNo(int[] xiaomifengJM) {
        //追加两列日期、凭证号
        //[yearVal, monthColumn, dayColumn, pzNoPart1Column, pzNoPart2Column, pzNoPart3Column]
        //添加表头
        Row headerRow = sheet.getRow(headerRowFirstNum);
        int dpzDateColumn = ++lastCellColumn;
        int pzNoColumn = ++lastCellColumn;
        headerRow.createCell(dpzDateColumn).setCellValue("制单日期");
        headerRow.createCell(pzNoColumn).setCellValue("凭证号数");
        //写数据
        int year = xiaomifengJM[0];
        int monthColumn = xiaomifengJM[1];
        int dayColumn = xiaomifengJM[2];
        int lastRowNum = sheet.getLastRowNum();
        int pzNoPart1Column = xiaomifengJM[3];
        int pzNoPart2Column = xiaomifengJM[4];
        int pzNoPart3Column = xiaomifengJM[5];
        for (int i=headerRowFirstNum+1; i<=lastRowNum; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            String monthVal = getCellString(curRow.getCell(monthColumn));
            String dayVal = getCellString(curRow.getCell(dayColumn));
            if (monthVal.isEmpty()||dayVal.isEmpty()){
                continue;
            }
            curRow.createCell(dpzDateColumn).setCellValue(String.format("%s-%s-%s", year, monthVal, dayVal));
            String paNoValPart1 = getCellString(curRow.getCell(pzNoPart1Column));
            String paNoValPart2 = getCellString(curRow.getCell(pzNoPart2Column));
            String paNoValPart3 = getCellString(curRow.getCell(pzNoPart3Column));
            if (paNoValPart1.isEmpty()||paNoValPart2.isEmpty()||paNoValPart3.isEmpty()) {
                continue;
            }
            curRow.createCell(pzNoColumn).setCellValue(paNoValPart1+paNoValPart2+paNoValPart3);
        }

    }


    /**
     * 小蜜蜂模板
     * ---------------------------
     *  2017年  ｜  凭证
     * ---------------------------
     * 月 ｜ 日 ｜ 期间 ｜ 字 ｜ 号
     * ---------------------------
     * 12 ｜ 31 ｜ 记 ｜ 12 ｜ 1
     * ---------------------------
     *
     * @return int[]
     * @author shaogz 2024/8/9 10:52
     */
    private int[] isTemplateOfXiaomifengJM() {
        //小蜜蜂敬民模板匹配
        //第一次尝试匹配
        boolean match1 = false;
        boolean match2 = false;
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();

            for (int j = 0; j<=lastCellNum; j++) {
                String cellVal = getCellString(curRow.getCell(j));
                if (Objects.equals("原币金额借方", cellVal)) {
                    match1 = true;
                } else if (Objects.equals("原币金额贷方", cellVal)) {
                    match2 = true;
                }
            }
            if (match1 && match2) {
                break;
            }
            match1 = false;
            match2 = false;
        }
        //Condition '!match2' is always 'true' when reached
        //保留警告，保证代码的可阅读性
        if (!match1 && !match2) {
            return new int[]{-1};
        }
        //第二次匹配，同时返回后续处理需要的字段
        //返回数据[yearVal, monthColumn, dayColumn, pzNoPart1Column, pzNoPart2Column, pzNoPart3Column]
        int yearColumn = -1;
        int pzNoColumn1 = -1;
        int yearVal = -1;
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            for (int j = 0; j<=lastCellNum; j++) {
                String cellVal = getCellString(curRow.getCell(j));
                //精准匹配2017年
                String cellValTmp1 = cellVal.replace("年", "");
                if (cellVal.length()-cellValTmp1.length()==1 && isPureNumber(cellValTmp1)) {
                    yearColumn = j;
                    yearVal = Integer.parseInt(cellValTmp1);
                } else if (Objects.equals("凭证", cellVal)) {
                    pzNoColumn1 = j;
                }
            }
            if (yearColumn!=-1 && pzNoColumn1!=-1) {
                break;
            }
        }
        if (yearColumn == pzNoColumn1) {
            //都还是原来的-1
            return new int[]{-1};
        }
        int monthColumn = -1;
        int dayColumn = -1;
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            if (Objects.equals("月", getCellString(curRow.getCell(yearColumn))) &&
                    Objects.equals("日", getCellString(curRow.getCell(yearColumn+1)))) {
                monthColumn = yearColumn;
                dayColumn = yearColumn+1;
                break;
            }
        }
        if (monthColumn == dayColumn) {
            //都还是原来的-1
            return new int[]{-1};
        }
        int pzNoColumn2 = -1;
        int pzNoColumn3 = -1;
        int pzNoColumn4 = -1;
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            if (Objects.equals("期间", getCellString(curRow.getCell(pzNoColumn1))) &&
                    Objects.equals("字", getCellString(curRow.getCell(pzNoColumn1+1))) &&
                    Objects.equals("号", getCellString(curRow.getCell(pzNoColumn1+2)))) {
                pzNoColumn2 = pzNoColumn1;
                pzNoColumn3 = pzNoColumn1 + 1;
                pzNoColumn4 = pzNoColumn1 + 2;
                break;
            }
        }
        if (pzNoColumn2 == pzNoColumn3) {
            //都还是原来的-1
            return new int[]{-1};
        }

        return new int[]{yearVal, monthColumn, dayColumn, pzNoColumn2, pzNoColumn3, pzNoColumn4};
    }

    private void mergePzNo(int[] splitPzNo) {
        //创建表头
        Row headerRow = sheet.getRow(splitPzNo[0]);
        short lastCellNumInHeaderRow = headerRow.getLastCellNum();
        headerRow.createCell(lastCellNumInHeaderRow+1).setCellValue("凭证号数");
        //读取所有数据行，添加一列和并列
        int dataStartRow = splitPzNo[0] + 1;
        int lastRowNum = sheet.getLastRowNum();
        for (int i=dataStartRow; i<=lastRowNum; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            StringBuilder mergedValNew = new StringBuilder();
            for (int j=1; j<splitPzNo.length; j++) {
                String cellString = getCellString(curRow.getCell(splitPzNo[j]));
                mergedValNew.append(cellString);
            }
            //这里不需要把该列提前存起来。复用后面读表头的逻辑即可。
            short lastCellNum = curRow.getLastCellNum();
            Cell pzNoCellNew = curRow.createCell(lastCellNum + 1);
            pzNoCellNew.setCellValue(mergedValNew.toString());
        }
    }

    private boolean isIllegal(int[] arr) {
        for (int each : arr) {
            if (each == -1) {
                return true;
            }
        }
        return false;
    }

    private int[] acquireSplitPaNo() {
        //这里虽然是首行往下读5行，但是读到就结束了
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            LinkedList<Integer> splitColumns = new LinkedList<>();
            for (int j = 0; j<=lastCellNum; j++) {
                if (PZ_NO_FUZZY_SET.contains(getCellString(curRow.getCell(j)))) {
                    splitColumns.add(j);
                }
            }
            if (!splitColumns.isEmpty()&&splitColumns.size()>=2) {
                return new int[]{i, splitColumns.get(0), splitColumns.get(1)};
            }
        }
        return new int[]{-1, -1, -1};
    }

    private void splitSubjectCodeAndName(int[] mergedSubject) {
        //创建表头
        Row headerRow = sheet.getRow(mergedSubject[0]);
        short lastCellNum = headerRow.getLastCellNum();
        this.subjectCodeColumn = lastCellNum+1;
        this.subjectNameColumn = lastCellNum+2;
        headerRow.createCell(this.subjectCodeColumn).setCellValue("科目编码");
        headerRow.createCell(this.subjectNameColumn).setCellValue("科目名称");

        //读取所有数据行，新增两列标准科目编码、科目名称列
        int dataStartRow = mergedSubject[0] + 1;
        int mergedSubjectColumn = mergedSubject[1];
        int lastRowNum = sheet.getLastRowNum();
        for (int i=dataStartRow; i<=lastRowNum; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            //因为通用的getCellString已经去掉空格了，所以这里从左往右，左边数字算科目编码，其他算科目名称
            String mergedSubjectVal = getCellString(curRow.getCell(mergedSubjectColumn));
            StringBuilder subjectCodeBuilder = new StringBuilder();
            StringBuilder subjectNameBuilder = new StringBuilder();
            for (int j=0; j<mergedSubjectVal.length(); j++) {
                char eachChar = mergedSubjectVal.charAt(j);
                if (isPureNumber(eachChar)) {
                    subjectCodeBuilder.append(eachChar);
                } else {
                    subjectNameBuilder.append(mergedSubjectVal.substring(j));
                    break;
                }
            }
            //create subjectCode
            Cell subjectCodeCell = curRow.createCell(this.subjectCodeColumn);
            subjectCodeCell.setCellValue(subjectCodeBuilder.toString());
            //create subjectName
            Cell subjectNameCell = curRow.createCell(this.subjectNameColumn);
            subjectNameCell.setCellValue(subjectNameBuilder.toString());
        }
    }

    private int[] acquireSubjectCodeAndNameMerged() {
        //这里虽然是首行往下读5行，但是读到就结束了
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            for (int j = 0; j<=lastCellNum; j++) {
                if (Objects.equals("科目", getCellString(curRow.getCell(j)))) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    private void convert2StdDfJfMny(int[] mnyAndDirection) {
        //创建表头
        Row headerRow = sheet.getRow(mnyAndDirection[0]);
        short lastCellNumInHeaderRow = headerRow.getLastCellNum();
        int jfMnyColumn = lastCellNumInHeaderRow + 1;
        Cell jfMnyCell = headerRow.createCell(jfMnyColumn);
        jfMnyCell.setCellValue("借方金额");
        int dfMnyColumn = lastCellNumInHeaderRow + 2;
        Cell dfMnyCell = headerRow.createCell(dfMnyColumn);
        dfMnyCell.setCellValue("贷方金额");
        //读取所有数据行，添加两列
        int dataStartRow = mnyAndDirection[0] + 1;
        int mnyColumn = mnyAndDirection[1];
        int directionColumn = mnyAndDirection[2];
        int lastRowNum = sheet.getLastRowNum();
        for (int i=dataStartRow; i<=lastRowNum; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }

            String mnyVal = getCellString(curRow.getCell(mnyColumn));
            String directionVal = getCellString(curRow.getCell(directionColumn));
            if (Objects.equals("借", directionVal)) {
                curRow.createCell(jfMnyColumn).setCellValue(mnyVal);
            } else if (Objects.equals("贷", directionVal)) {
                curRow.createCell(dfMnyColumn).setCellValue(mnyVal);
            }
        }
    }

    private int[] acquireMnyAndDirection() {
        //这里虽然是首行往下读5行，但是读到就结束了
        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
            Row curRow = sheet.getRow(i);
            if (curRow == null) {
                continue;
            }
            short lastCellNum = curRow.getLastCellNum();
            int mnyColumn = -1;
            int directionColumn = -1;
            for (int j = 0; j<=lastCellNum; j++) {
                String cellVal = getCellString(curRow.getCell(j));
                if (Objects.equals("金额", cellVal)) {
                    mnyColumn = j;
                } else if (Objects.equals("方向", cellVal)) {
                    directionColumn = j;
                }
            }
            if (directionColumn!=-1 && mnyColumn!=-1) {
                return new int[]{i, mnyColumn, directionColumn};
            }
        }
        return new int[]{-1, -1, -1};
    }

}
