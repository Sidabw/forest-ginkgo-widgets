/**
 * Copyright (C), 2018-2021, bokecc.com FileName: RepeatServletTest2 Author:   shaogz Date:     2021/4/27 12:01 PM
 * Description: History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.brew.home.prework.repeat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 〈一句话功能简述〉:
 * 〈〉
 *
 * @author shaogz
 * @since 2021/4/27
 */
public class RepeatServletTest2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("GET SUCCESS~~~ 2");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("POST SUCCESS~~~ 2");
    }
}
