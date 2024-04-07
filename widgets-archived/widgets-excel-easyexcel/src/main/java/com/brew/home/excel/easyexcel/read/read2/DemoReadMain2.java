package com.brew.home.excel.easyexcel.read.read2;

import com.alibaba.excel.EasyExcel;

/**
 * @author shaogz
 */
public class DemoReadMain2 {

    public static void main(String[] args) {
        String fileName = "/Users/feiyi/Documents/git-project-sidabw/forest-ginkgo-widgets/widgets-excel/src/main/resources/userverifywhitelist.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData2.class, new DemoDataListener2()).sheet().doRead();
    }

}
