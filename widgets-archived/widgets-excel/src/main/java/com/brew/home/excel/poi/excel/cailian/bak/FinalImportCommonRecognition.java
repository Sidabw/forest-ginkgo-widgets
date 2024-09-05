package com.brew.home.excel.poi.excel.cailian.bak;//package ynt.ui.ynt.imp.tools;
//
///**
// * @author shaogz
// * @since 2024/8/5 14:55
// */
//public class FinalImportCommonRecognition {
//
//    private static Logger log = Logger.getLogger(FinalImportCommonRecognition.class);
//
//    private static final Set<String> HEADER_ROW_FIRST_SET = new HashSet<>(Arrays.asList("科目编码", "科目编号", "科目代码",
//            "科目名称", "借方累计", "贷方累计", "期初余额", "期末余额", "本年累计发生额"));
//
//    private static final Set<String> SUBJECT_CODE_FUZZY_SET = new HashSet<>(Arrays.asList("科目编码", "科目编号", "科目代码"));
//
//    private static final Set<String> JF_QCMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("期初余额借方", "期初借方", "期初借方余额", "期初余额(借方)"));
//
//    private static final Set<String> DF_QCMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("期初余额贷方", "期初贷方", "期初贷方余额", "期初余额(贷方)"));
//
//    private static final Set<String> BN_JF_LJMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("本年借方累计", "本年累计发生额(借方)", "本年累计发生额借方"));
//
//    private static final Set<String> BN_DF_LJMNY_FUNZZY_SET = new HashSet<>(Arrays.asList("本年贷方累计", "本年累计发生额(贷方)", "本年累计发生额贷方"));
//
//    public SuperVO[] getFinalByExcel(Sheet sheet, String voClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//        //找首行、末行
//        //模板判断...模板1/2/../通用模板
//        //结果数据：科目编码、科目名称、期初余额借方、期初余额贷方、本年累计借方、本年累计贷方
//        //找表头首行
//        int headerRowFirstNum = acquireHeaderRowFirstNum(sheet);
//        if (headerRowFirstNum == -1) {
//            log.error("表头匹配失败, 行首匹配失败");
//            return new SuperVO[0];
//        }
//        //找表头科目编码、科目名称列
//        int[] subjectColumnsTmp = acquireSubjectCodeAndName(sheet, headerRowFirstNum);
//        if (subjectColumnsTmp[0] == -1 || subjectColumnsTmp[1] == -1) {
//            log.error("表头匹配失败，科目编码/名称匹配失败");
//            return new SuperVO[0];
//        }
//        int subjectCodeColumn = subjectColumnsTmp[0];
//        int subjectNameColumn = subjectColumnsTmp[1];
//        //找表头末行
//        int headerRowLastNum = acquireHeaderRowLastNum(sheet, headerRowFirstNum, subjectCodeColumn);
//        if (headerRowLastNum == -1) {
//            log.error("表头匹配失败, 行尾匹配失败");
//            return new SuperVO[0];
//        }
//        //区分是否特殊模板
//        int dataRowFirstNum = headerRowLastNum + 1;
//        if (isTemplate1(sheet, headerRowFirstNum, headerRowLastNum)) {
//            return readDataInTemplate1(sheet, headerRowFirstNum, subjectCodeColumn, subjectNameColumn, dataRowFirstNum, voClassName);
//        }
//        //进入通用模板逻辑
//        //找期初借方、期初贷方、本年累计借方、本年累计贷方的列
//        boolean singleHeaderRow = (headerRowLastNum - headerRowFirstNum) == 0;
//        int[] mnyColumns = acquireMnyColumns(sheet, headerRowFirstNum, singleHeaderRow);
//        if (mnyColumns[0] == -1 || mnyColumns[1] == -1) {
//            log.error("表头匹配失败，期初余额借方/贷方匹配失败");
//            return new SuperVO[0];
//        }
//        int jfqcmnyColumn = mnyColumns[0];
//        int dfqcmnyColumn = mnyColumns[1];
//        int jfbnljColumn = mnyColumns[2];
//        int dfbnljColumn = mnyColumns[3];
//
//        //read data
//        List<SuperVO<?>> clist = new LinkedList<>();
//        int lastRowNum = sheet.getLastRowNum();
//        String subjectCodeValPrevious = null;
//        for (int i = dataRowFirstNum; i<lastRowNum; i++) {
//
//            Cell subjectCodeCell = sheet.getRow(i) == null ? null : sheet.getRow(i).getCell(subjectCodeColumn);
//            Cell subjectNameCell = sheet.getRow(i) == null ? null : sheet.getRow(i).getCell(subjectNameColumn);
//            if (isIllegalSubjectItems(subjectCodeCell, subjectNameCell)) {
//                continue;
//            }
//
//            String subjectCodeVal = getSubjectCodeVal(subjectCodeCell);
//            boolean aidedItemExist = false;
//            if (!StringUtils.isEmpty(subjectCodeVal)) {
//                subjectCodeValPrevious = subjectCodeVal;
//            } else {
//                subjectCodeVal = subjectCodeValPrevious;
//                aidedItemExist = true;
//            }
//            Row curRow = sheet.getRow(i);
//            SuperVO vo = (SuperVO) Class.forName(voClassName).newInstance();
//            vo.setAttributeValue("subj_code", subjectCodeVal);
//            vo.setAttributeValue("subj_name", getCellString(subjectNameCell));
//            vo.setAttributeValue("jf_qcmny", getCellString(curRow.getCell(jfqcmnyColumn)));
//            vo.setAttributeValue("df_qcmny", getCellString(curRow.getCell(dfqcmnyColumn)));
//
//            if (jfbnljColumn!=-1 && dfbnljColumn!=-1) {
//                vo.setAttributeValue("jf_ljmny", getCellString(curRow.getCell(jfbnljColumn)));
//                vo.setAttributeValue("df_ljmny", getCellString(curRow.getCell(dfbnljColumn)));
//            }
//            //辅助项编码
//            if (aidedItemExist) {
//                String aidedItemCode = getAidedItemSubjectCode(subjectNameCell);
//                vo.setAttributeValue("fzcode", aidedItemCode);
//            }
//            clist.add(vo);
//        }
//        return buildRes(voClassName, clist);
//    }
//
//    private SuperVO[] buildRes(String voClassName, List<SuperVO<?>> clist) throws ClassNotFoundException {
//        Class c = Class.forName(voClassName);
//        SuperVO[] vos = (SuperVO[]) java.lang.reflect.Array
//                .newInstance(c, clist.size());
//        return clist.toArray(vos);
//    }
//
//    private SuperVO[] readDataInTemplate1(Sheet sheet, int headerRowFirstNum, int subjectCodeColumn, int subjectNameColumn,
//                                          int dataRowFirstNum, String voClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//        Row tableHeaderFirst = sheet.getRow(headerRowFirstNum);
//        short tableHeaderFirstCellNum = tableHeaderFirst.getLastCellNum();
//        //找到 '余额方向'
//        int yefx = acquireYefxInTemplate1(tableHeaderFirst, tableHeaderFirstCellNum);
//        //找到期初余额、本年累计发生额借方、本年累计发生额贷方
//        int[] mnyColumns = acquireMnyColumnsInTemplate1(sheet, headerRowFirstNum);
//        int columnQcMny = mnyColumns[0];
//        if (columnQcMny == -1) {
//            log.error("表头匹配失败，期初余额匹配失败");
//            return new SuperVO[0];
//        }
//        int jfbnljColumn = mnyColumns[1];
//        int dfbnljColumn = mnyColumns[2];
//        //read data
//        List<SuperVO<?>> clist = new LinkedList<>();
//        int lastRowNum = sheet.getLastRowNum();
//        for (int i = dataRowFirstNum; i<lastRowNum; i++) {
//            //项目编码必须是纯数字或空
//            Cell subjectCodeCell = sheet.getRow(i) == null? null : sheet.getRow(i).getCell(subjectCodeColumn);
//            Cell subjectNameCell = sheet.getRow(i) == null? null : sheet.getRow(i).getCell(subjectNameColumn);
//            if (isIllegalSubjectItems(subjectCodeCell, subjectNameCell)) {
//                continue;
//            }
//            Row curRow = sheet.getRow(i);
//            SuperVO vo = (SuperVO) Class.forName(voClassName).newInstance();
//            vo.setAttributeValue("subj_code", getCellString(subjectCodeCell));
//            vo.setAttributeValue("subj_name", getCellString(curRow.getCell(subjectNameColumn)));
//
//            if (Objects.equals("借", getCellVal(curRow.getCell(yefx)))) {
//                vo.setAttributeValue("jf_qcmny", getCellString(curRow.getCell(columnQcMny)));
//            } else if (Objects.equals("贷", getCellVal(curRow.getCell(yefx)))) {
//                vo.setAttributeValue("df_qcmny", getCellString(curRow.getCell(columnQcMny)));
//            }
//            if (jfbnljColumn!=-1&&dfbnljColumn!=-1) {
//                vo.setAttributeValue("jf_ljmny", getCellString(curRow.getCell(jfbnljColumn)));
//                vo.setAttributeValue("df_ljmny", getCellString(curRow.getCell(dfbnljColumn)));
//            }
//            clist.add(vo);
//        }
//        return buildRes(voClassName, clist);
//    }
//
//    private int acquireYefxInTemplate1(Row tableHeaderFirst, short tableHeaderFirstCellNum) {
//        int yefx = -1;
//        for (int i = 0; i<= tableHeaderFirstCellNum; i++) {
//            String cellStringZh = getCellStringZh(tableHeaderFirst.getCell(i));
//            if (Objects.equals("余额方向", cellStringZh)) {
//                yefx = i;
//                break;
//            }
//        }
//        return yefx;
//    }
//
//    private int[] acquireMnyColumnsInTemplate1(Sheet sheet, int headerRowFirstNum) {
//        int[] res = new int[]{-1, -1, -1};
//        Row headerRowFirst = sheet.getRow(headerRowFirstNum);
//        short rowFirstLastCellNum = headerRowFirst.getLastCellNum();
//        //期初余额
//        int qcye = -1;
//        //本年累计
//        int bnlj = -1;
//        for (int i=0; i<rowFirstLastCellNum; i++) {
//            Cell curCell = headerRowFirst.getCell(i);
//            String cellStringZh = getCellStringZh(curCell);
//            if(Objects.equals("期初余额", cellStringZh)) {
//                qcye = i;
//            }else if (Objects.equals("本年累计发生额", cellStringZh)) {
//                bnlj = i;
//            }
//        }
//        res[0] = qcye;
//
//        Row headerRowSec = sheet.getRow(headerRowFirstNum + 1);
//        if (Objects.equals("借方", getCellStringZh(headerRowSec.getCell(bnlj))) &&
//                Objects.equals("贷方", getCellStringZh(headerRowSec.getCell(bnlj+1)))) {
//            res[1] = bnlj;
//            res[2] = bnlj+1;
//        }
//        return res;
//    }
//
//    private boolean isTemplate1(Sheet sheet, int headerRowFirstNum, int headerRowLastNum) {
//        //表头里有'余额方向'，且往下只有借/贷，则匹配成功
//        boolean match1 = false;
//        int yefxColumnNum = -1;
//        for (int i=headerRowFirstNum; i<=headerRowLastNum; i++) {
//            Row curRow = sheet.getRow(i);
//            if (curRow==null||match1) {
//                continue;
//            }
//            short lastCellNum = curRow.getLastCellNum();
//            for (int j=0; j<=lastCellNum; j++) {
//                Cell curCell = curRow.getCell(j);
//                String cellStringZh = getCellStringZh(curCell);
//                if (Objects.equals("余额方向", cellStringZh)) {
//                    match1 = true;
//                    yefxColumnNum = j;
//                    break;
//                }
//            }
//        }
//        if (!match1) {
//            return false;
//        }
//        //往下读三行
//        for (int i=headerRowLastNum+1; i<headerRowLastNum+4; i++) {
//            Row curRow = sheet.getRow(i);
//            if (curRow==null) {
//                continue;
//            }
//            String cellStringZh = getCellStringZh(curRow.getCell(yefxColumnNum));
//            if (!Objects.equals("借", cellStringZh) && !Objects.equals("贷", cellStringZh) && !StringUtils.isEmpty(cellStringZh)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private int[] acquireMnyColumnsSingleHeader(Sheet sheet, int headerRowFirstNum) {
//        int[] res = new int[]{-1, -1, -1, -1};
//        Row curRow = sheet.getRow(headerRowFirstNum);
//        short lastCellNum = curRow.getLastCellNum();
//        for (int i=0; i<=lastCellNum; i++) {
//            Cell curCell = curRow.getCell(i);
//            String cellStringZh = getCellStringZh(curCell);
//            if (JF_QCMNY_FUNZZY_SET.contains(cellStringZh)) {
//                res[0] = i;
//            } else if (DF_QCMNY_FUNZZY_SET.contains(cellStringZh)) {
//                res[1] = i;
//            } else if (BN_JF_LJMNY_FUNZZY_SET.contains(cellStringZh)) {
//                res[2] = i;
//            } else if (BN_DF_LJMNY_FUNZZY_SET.contains(cellStringZh)) {
//                res[3] = i;
//            }
//        }
//        return res;
//    }
//
//    private void acquireMnyColumnsMultiHeaderQc(Sheet sheet, int headerRowFirstNum, Row rowFirst, short rowFirstLastCellNum, int[] res) {
//        //期初
//        int qcmnyColumn = -1;
//        for (int i=0; i<=rowFirstLastCellNum;i++){
//            String cellStringZh = getCellStringZh(rowFirst.getCell(i));
//            if (Objects.equals("期初", cellStringZh) || Objects.equals("期初余额", cellStringZh)) {
//                qcmnyColumn = i;
//                break;
//            }
//        }
//        String cellStr1 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(qcmnyColumn));
//        String cellStr2 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(qcmnyColumn+1));
//        if ((Objects.equals("借方", cellStr1)&& Objects.equals("贷方", cellStr2))) {
//            res[0] = qcmnyColumn;
//            res[1] = qcmnyColumn+1;
//        }
//    }
//
//    private int[] acquireMnyColumnsMultiHeader(Sheet sheet, int headerRowFirstNum) {
//        int[] res = new int[]{-1, -1, -1, -1};
//        //part-1:多行表头的找：期初借方、期初贷方
//        Row rowFirst = sheet.getRow(headerRowFirstNum);
//        short rowFirstLastCellNum = rowFirst.getLastCellNum();
//        acquireMnyColumnsMultiHeaderQc(sheet, headerRowFirstNum, rowFirst, rowFirstLastCellNum, res);
//        //part-2:多行表头的找：本年累计借方、本年累计贷方
//        for (int i=0; i<=rowFirstLastCellNum;i++){
//            String cellStringZh = getCellStringZh(rowFirst.getCell(i));
//            if (Objects.equals("借方累计", cellStringZh)) {
//                res[2] = i;
//            } else if (Objects.equals("贷方累计", cellStringZh)) {
//                res[3] = i;
//            }
//        }
//        if (res[2] != -1 && res[3] != -1) {
//            return res;
//        }
//        //本年累计
//        int bnljmnyColumn = -1;
//        for (int i=0; i<=rowFirstLastCellNum;i++){
//            String cellStringZh = getCellStringZh(rowFirst.getCell(i));
//            if (Objects.equals("本年累计", cellStringZh) || Objects.equals("本年累计发生额", cellStringZh)) {
//                bnljmnyColumn = i;
//                break;
//            }
//        }
//        if (bnljmnyColumn==-1) {
//            return res;
//        }
//        String cellStr3 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(bnljmnyColumn));
//        String cellStr4 = getCellVal(sheet.getRow(headerRowFirstNum + 1).getCell(bnljmnyColumn+1));
//        if ((Objects.equals("借方", cellStr3)&& Objects.equals("贷方", cellStr4))) {
//            res[2] = bnljmnyColumn;
//            res[3] = bnljmnyColumn+1;
//        }
//        return res;
//    }
//
//    private String getAidedItemSubjectCode(Cell aidedItemName) {
//        String cellString = getCellStringNullable(aidedItemName);
//        if (StringUtils.isEmpty(cellString)) {
//            return null;
//        }
//        Deque<Character> deque1 = new ArrayDeque<>();
//        Deque<Character> deque2 = new ArrayDeque<>();
//        StringBuilder res = new StringBuilder();
//        for (char each: cellString.toCharArray()) {
//            if (each=='【' || each=='（' || each == '[' || each=='(') {
//                deque1.push(each);
//            } else if (each == '】' && Objects.equals(deque1.peek(), '【')) {
//                deque1.pop();
//            } else if (each == '）' && Objects.equals(deque1.peek(), '（')) {
//                deque1.pop();
//            } else if (each == ']' && Objects.equals(deque1.peek(), '[')) {
//                deque1.pop();
//            } else if (each == ')' && Objects.equals(deque1.peek(), '(')) {
//                deque1.pop();
//            } else if (!deque1.isEmpty()){
//                deque2.push(each);
//            }
//        }
//        if (deque1.isEmpty()) {
//            Iterator<Character> characterIterator = deque2.descendingIterator();
//            while (characterIterator.hasNext()) {
//                res.append(characterIterator.next());
//            }
//            return res.toString();
//        }
//        return null;
//    }
//
//    private int[] acquireMnyColumns(Sheet sheet, int headerRowFirstNum, boolean singleHeaderRow) {
//        //int[] = 期初借方，期初贷方，本年累计借方，本年累计贷方
//        if (singleHeaderRow) {
//            return acquireMnyColumnsSingleHeader(sheet, headerRowFirstNum);
//        } else {
//            return acquireMnyColumnsMultiHeader(sheet, headerRowFirstNum);
//        }
//    }
//
//    private int acquireHeaderRowFirstNum(Sheet sheet) {
//        for (int i = 0; i<10; i++) {
//            Row curRow = sheet.getRow(i);
//            if (curRow == null) {
//                continue;
//            }
//            short lastCellNum = curRow.getLastCellNum();
//            for (int j = 0; j<=lastCellNum; j++) {
//                Cell curCell = curRow.getCell(j);
//                String curCellVal = getCellString(curCell);
//                if (HEADER_ROW_FIRST_SET.contains(curCellVal)) {
//                    return i;
//                }
//            }
//        }
//        return -1;
//    }
//
//    private int[] acquireSubjectCodeAndName(Sheet sheet, int headerRowFirstNum) {
//        //res[0] subjectCodeColumn
//        //res[1] subjectNameColumn
//        int[] res = new int[]{-1, -1};
//        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
//            Row curRow = sheet.getRow(i);
//            if (curRow == null) {
//                continue;
//            }
//            short lastCellNum = curRow.getLastCellNum();
//            for (int j = 0; j<=lastCellNum; j++) {
//                Cell curCell = curRow.getCell(j);
//                String curCellVal = getCellString(curCell);
//                if (SUBJECT_CODE_FUZZY_SET.contains(curCellVal)) {
//                    res[0] = j;
//                } else if (Objects.equals("科目名称", curCellVal)) {
//                    res[1] = j;
//                }
//            }
//        }
//        return res;
//    }
//
//
//    private int acquireHeaderRowLastNum(Sheet sheet, int headerRowFirstNum, int subjectCodeColumn) {
//        for (int i=headerRowFirstNum; i<headerRowFirstNum+5; i++) {
//            Row curRow = sheet.getRow(i);
//            if (curRow == null) {
//                continue;
//            }
//            Cell curCell = curRow.getCell(subjectCodeColumn);
//            String subjectCodeVal = getSubjectCodeVal(curCell);
//            if (isPureNumber(subjectCodeVal)) {
//                return i-1;
//            }
//        }
//        return -1;
//    }
//
//    private boolean isIllegalSubjectItems(Cell subjectCodeCell, Cell subjectNameCell) {
//        //项目编码和项目名称不可同时为空
//        boolean subjectCodeJudge = isEmpty(subjectCodeCell);
//        boolean subjectNameJudge = isEmpty(subjectNameCell);
//        //都为true，则为true
//        if (subjectCodeJudge && subjectNameJudge) {
//            return true;
//        }
//        //项目编码必须是纯数字或空；
//        if(!isSubjectCodePureNumberOrEmpty(subjectCodeCell)) {
//            return true;
//        }
//        //项目编码为空的情况下，项目名称不可是小计、合计等
//        String subjectNameVal = getCellVal(subjectNameCell);
//        return subjectNameVal.contains("小计") || subjectNameVal.contains("合计");
//    }
//
//    private boolean isEmpty(Cell cell) {
//        if(cell==null) {
//            return true;
//        }
//        String cellStr = getCellVal(cell);
//        return StringUtils.isEmpty(cellStr);
//    }
//
//    private boolean isSubjectCodePureNumberOrEmpty(Cell subjectCodeCell) {
//        String subjectCodeVal = getSubjectCodeVal(subjectCodeCell);
//        if (StringUtils.isEmpty(subjectCodeVal)) {
//            return true;
//        }
//        for (char each : subjectCodeVal.toCharArray()) {
//            if (!(each>=48&&each<=57)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private String getCellVal(Cell cell) {
//        String formatCellValue = new DataFormatter().formatCellValue(cell);
//        return rmBlank(formatCellValue);
//    }
//
//    private boolean isPureNumber(String cellStr) {
//        if (StringUtils.isEmpty(cellStr)) {
//            return false;
//        }
//        for (char each : cellStr.toCharArray()) {
//            if (!(each>=48&&each<=57)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private String getCellStringZh(Cell curCell) {
//        String original = getCellString(curCell);
//        StringBuilder res = new StringBuilder();
//        for (char curChar : original.toCharArray()) {
//            if (curChar>127) {
//                res.append(curChar);
//            }
//        }
//        return res.toString();
//    }
//
//    private String getCellString(Cell curCell) {
//        return getCellString(curCell, "");
//    }
//
//    private String getCellStringNullable(Cell curCell) {
//        return getCellString(curCell, null);
//    }
//
//    private String getSubjectCodeVal(Cell curCell) {
//        String cellVal = getCellString(curCell, "");
//        //对应金蝶k3/金蝶kis/及其它的科目编码特殊处理
//        //测试文件：
//        //金蝶k3前缀
//        //金蝶kis前缀
//        //辅助发生额及余额表_2018-07-20(1).xls
//        return cellVal.replace(".", "").replace("_", "");
//    }
//
//    private String getCellString(Cell curCell, String defaultVal) {
//        if (curCell == null) {
//            return defaultVal;
//        }
//        String formatCellValue = new DataFormatter().formatCellValue(curCell);
//        return rmBlank(formatCellValue);
//    }
//
//    private String rmBlank(String original) {
//        return original.replace("　","").replace(" ", "");
//    }
//

//}
