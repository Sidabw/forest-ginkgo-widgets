package com.brew.home.excel.easyexcel.write;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.brew.home.excel.easyexcel.write.DemoWriteMain.data;

/**
 * @author shaogz
 */
public class DemoWriteMain2 {

    public static void main(String[] args) {
        //动态表头练习：https://www.yuque.com/easyexcel/doc/easyexcel

        String fileName = "/Users/feiyi/Downloads/dy-head-test.xlsx";
        EasyExcel.write(fileName).head(buildHead()).sheet("模板").doWrite(data());
    }

    private static List<List<String>> buildHead() {
        ArrayList<List<String>> res = new ArrayList<>();
        res.add(Collections.singletonList("a"));
        res.add(Collections.singletonList("b"));
        res.add(Collections.singletonList("c"));


        //不知道为什么api要设计成上面这样。反正下面这种操作会失败。
        // res.add(Arrays.asList("aa", "cc"));
        return res;
    }
}
