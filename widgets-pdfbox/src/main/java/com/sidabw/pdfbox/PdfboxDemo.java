package com.sidabw.pdfbox;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * @author shaogz
 * @since 2024/11/23 10:49
 */
public class PdfboxDemo {

    public static void main(String[] args) throws IOException {

        //12页的pdf拆分，590ms
        long t1 = System.currentTimeMillis();
        new PdfboxDemo().t1();
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

    }

    public void t1() throws IOException {
        //Client-side-caching-in-Redis-Docs.pdf
        String path = this.getClass().getClassLoader().getResource("").getPath();
        System.out.println(path);

//
        File file = new File(path + "/Client-side-caching-in-Redis-Docs.pdf");
//        File file = new File(path + "/荣亿工程_241109_141941.pdf");
        // load pdf file
        PDDocument document = PDDocument.load(file);

        // instantiating Splitter
        Splitter splitter = new Splitter();

        // split the pages of a PDF document
        List<PDDocument> Pages = splitter.split(document);

        // Creating an iterator
        Iterator<PDDocument> iterator = Pages.listIterator();

        // saving splits as pdf
        int i = 0;
        while(iterator.hasNext()) {
            PDDocument pd = iterator.next();
            // provide destination path to the PDF split
            pd.save(path + "/Client-side-caching-in-Redis-Docs-"+ ++i +".pdf");
            System.out.println("Saved /home/tk/pdfs/sample_part_"+ i +".pdf");
        }
        System.out.println("Provided PDF has been split into multiple.");
        document.close();
    }
}
