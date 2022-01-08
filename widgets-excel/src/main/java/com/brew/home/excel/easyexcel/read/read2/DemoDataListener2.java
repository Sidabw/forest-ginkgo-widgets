package com.brew.home.excel.easyexcel.read.read2;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shaogz
 */
@Slf4j
public class DemoDataListener2 implements ReadListener<DemoData2> {

    @Override
    public void invoke(DemoData2 data, AnalysisContext context) {
        //rowIndex 从0开始
        Integer rowIndex = context.readRowHolder().getRowIndex();
        log.error("rowIndex:{}, 解析到一条数据:{}", rowIndex, data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
