/**
 * Copyright (C), 2018-2021, bokecc.com FileName: FirstServlet Author:   shaogz Date:     2021/4/27 11:07 AM
 * Description: History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.brew.home.prework;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 〈一句话功能简述〉:
 * 〈
 *
 *
 * 〉
 *
 * @author shaogz
 * @create 2021/4/27
 * @since 1.0.0
 */

//这里说的是可以/test访问 也可以/get访问。。
//用这个就不需要配置web.xml了
@WebServlet({"/test","/get"})
public class FirstServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("GET SUCCESS~~~");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("POST SUCCESS~~~");
    }
}
