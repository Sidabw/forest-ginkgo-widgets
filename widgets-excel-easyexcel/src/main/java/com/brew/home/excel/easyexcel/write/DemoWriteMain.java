package com.brew.home.excel.easyexcel.write;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.brew.home.excel.easyexcel.read.DemoData;

import java.util.Date;
import java.util.List;

/**
 * @author shaogz
 */
public class DemoWriteMain {

    public static void main(String[] args) {

        //https://www.yuque.com/easyexcel/doc/write

        String fileName = "/Users/feiyi/Downloads/直播观看时长详情（2021-11-01至2021-11-12） (3).xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());
    }

    public static List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

}
