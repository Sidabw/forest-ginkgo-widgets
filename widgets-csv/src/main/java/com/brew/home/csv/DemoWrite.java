package com.brew.home.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author shaogz
 */
public class DemoWrite {


    //CSV文件分隔符
    private static final String NEW_LINE_SEPARATOR = "\n";
    //文件后缀
    //    private static final String suffix = ".csv";
    private static final String suffix = ".zenki";
    //文件路径
    private static final String filePath = "/Users/feiyi/Desktop/csvdir/1" + suffix;


    public static void main(String[] args) throws IOException {
        //初始化csvformat
        CSVFormat formator = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        //创建FileWriter对象,filePathcsv文件路径
        FileWriter fileWriter = new FileWriter(filePath);
        //创建CSVPrinter对象
        CSVPrinter printer = new CSVPrinter(fileWriter, formator);
        String[] headers = new String[]{"name", "content"};
        List<String[]> data = Arrays.asList(
            new String[]{"header--1", "content--内容--1"},
            new String[]{"header--2", "content--内容--2"},
            new String[]{"header--3", "content--内容--3"},
            new String[]{"header--4", "content--内容--4"},
            new String[]{"header--5", "content--内容--5"},
            new String[]{"header--6", "content--内容--6"},
            new String[]{"header--7", "content--内容--7"},
            new String[]{"header--8", "content--内容--8"},
            new String[]{"header--9", "content--内容--9"}
        );
        //写入头数据
        printer.printRecord(headers);
        //写入内容数据
        for (String[] dataEach : data) {
            printer.printRecord(dataEach);
        }
        printer.close();
    }
}
