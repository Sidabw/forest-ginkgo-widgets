/**
 * Copyright (C), 2018-2019, zenki.ai
 * FileName: Demo
 * Author:   feiyi
 * Date:     2019/5/16 3:27 PM
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.brew.home.csv;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 〈一句话功能简述〉:
 * 〈〉
 *
 * @author feiyi
 * @create 2019/5/16
 * @since 1.0.0
 */
public class DemoRead {

    //CSV文件分隔符
    private static final String NEW_LINE_SEPARATOR = "\n";
    //文件后缀
//    private static final String suffix = ".csv";
    private static final String suffix = ".zenki";
    //文件路径
    private static final String filePath = "/Users/feiyi/Desktop/csvdir/1" + suffix;



    //读操作
    public static void main(String[] args) throws IOException {

        String filePath = "/Users/feiyi/Desktop/csvdir/1" + suffix;
        CSVFormat formator = CSVFormat.DEFAULT;
        FileReader fileReader = new FileReader(filePath);
        //创建CSVParser对象
        CSVParser parser = new CSVParser(fileReader, formator);
        List<CSVRecord> records = parser.getRecords();
        CSVRecord headersRecord = records.get(0);
        Iterator<String> iterator = headersRecord.iterator();
        ArrayList<String> headers = new ArrayList<>();
        while (iterator.hasNext()) {
            String next = iterator.next();
            headers.add(next);
        }
        JSONArray result = new JSONArray();
        for (int i = 1; i < records.size(); i++) {
            JSONObject each = new JSONObject();
            Iterator<String> iteratorIn = records.get(i).iterator();
//            ArrayList<String> eachContent = new ArrayList<>();
//            while (iteratorIn.hasNext()) {
//                eachContent.add(iteratorIn.next());
//            }
            headers.stream().forEach(e -> {
                each.put(e, iteratorIn.hasNext() ? iteratorIn.next() : "");
            });
            result.add(each);
        }
        System.out.println(result.size());
        System.out.println(result.getJSONObject(0).getString("name"));
        parser.close();
        fileReader.close();
    }
}
