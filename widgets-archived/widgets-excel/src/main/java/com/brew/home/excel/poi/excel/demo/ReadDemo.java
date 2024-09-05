package com.brew.home.excel.poi.excel.demo;

import com.brew.home.excel.poi.excel.cailian.common.AbstractCommonRecognition;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.Set;

/**
 * @author shaogz
 * @since 2024/7/30 16:29
 */
public class ReadDemo extends AbstractCommonRecognition {

    public static void main(String[] args) throws IOException {
//        String filePath = "/Users/feiyi/Downloads/导账包需求/丰庆燕春/科目余额表 (3).xlsx";
//        String filePath = "/Users/feiyi/Downloads/导账包需求/26-财政资金20240604/3-准备公司的近3年及本年的科目期初余额表/202401-4月发生额及余额表.xlsx";
//        String filePath = "/Users/feiyi/Downloads/导账包需求/测试文件-整理后-凭证/云代账-1544418342凭证列表#2018年01期-2018年10期.xls";
        String filePath = "/Users/feiyi/Downloads/导账包需求/测试文件-整理后-凭证/小蜜蜂敬民-2017.12凭证.xlsx";
        Sheet sheet = AbstractCommonRecognition.createExcelSheet(filePath);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i < lastRowNum; i++) {
            System.out.println("行号：" + i);
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            //测试结论：对于同一个单元格而言，通过迭代器和普通for循环获取到的下标是不一样的
            for (int j=0; j<=row.getLastCellNum();j++) {
                System.out.println("列号：" + (j) + " 值：" + row.getCell(j));
            }


//            int j = 0;
//            Iterator<Cell> cellIterator = row.iterator();
//            while (cellIterator.hasNext()) {
//                System.out.println("列号：" + (j) + " 值：" + cellIterator.next());
//                j++;
//            }

            System.out.println();
            System.out.println();
        }


        System.out.println("---------------------------------------------");
        Row tmp1 = sheet.getRow(7);
        for (int x = 0; x < tmp1.getLastCellNum(); x++) {
            System.out.println("列号：" + (x) + " 值：" + tmp1.getCell(x));
        }

    }

    protected ReadDemo(Sheet sheet, String[] attrs) {
        super(sheet, attrs);
    }

    @Override
    public Set<String> getHeaderRowFirstLocateSet() {
        return null;
    }

    @Override
    public boolean extractDataColumns() {
        return false;
    }

}
