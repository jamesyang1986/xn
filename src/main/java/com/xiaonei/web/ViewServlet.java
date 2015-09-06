package com.xiaonei.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiaonei.db.utils.JsonUtils;
import com.xiaonei.pojo.MatchResult;
import com.xiaonei.rec.service.UserRecService;

public class ViewServlet extends HttpServlet {
    private UserRecService service = new UserRecService();

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String uid = request.getParameter("uid");
        String mids = request.getParameter("mids");
        
        String[] ids = mids.split(",");
        
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String result = "ok";
        Map<String, Object> resMap = new HashMap<String, Object>();
        if (result != null) {
            resMap.put("result", result);
        } else {
            resMap.put("es", 500);
            resMap.put("ec", " the match result is null ");
        }
        
        String value = JsonUtils.toJSON(resMap);
        out.println(value.toString());
        out.close();
    }
}
