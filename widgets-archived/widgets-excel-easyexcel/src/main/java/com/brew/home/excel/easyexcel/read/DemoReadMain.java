package com.brew.home.excel.easyexcel.read;

import com.alibaba.excel.EasyExcel;

import java.io.IOException;

/**
 * @author shaogz
 */
public class DemoReadMain {

    public static void main(String[] args) throws IOException {
        String fileName = "/Users/feiyi/Documents/git-project-sidabw/forest-ginkgo-widgets/widgets-excel/src/main/resources/直播观看时长详情（2021-11-01至2021-11-12）.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

}
