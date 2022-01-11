/**
 * Copyright (C), 2018-2021, bokecc.com FileName: FirstServlet2 Author:   shaogz Date:     2021/4/27 11:17 AM
 * Description: History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.brew.home.prework;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 〈一句话功能简述〉:
 * 〈需要在web.xml中配置ServletName、 ServletMapping〉
 *
 * @author shaogz
 * @create 2021/4/27
 * @since 1.0.0
 */
public class FirstServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("GET2 SUCCESS~~~");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("POST2 SUCCESS~~~");
    }

}
