package com.xiaonei.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.xiaonei.dao.UserShowDao;
import com.xiaonei.db.utils.JsonUtils;
import com.xiaonei.pojo.MatchResult;
import com.xiaonei.rec.service.UserRecService;

public class UserMatchServlet extends HttpServlet {
    private UserRecService service = new UserRecService();
    private UserShowDao showDao = new UserShowDao();

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
       
        Map<String, Object> result = null;
        try {
            result = new HashMap<String, Object>();
            String uid = request.getParameter("uid");
            String num = request.getParameter("num");
            int total = 20;
            if (StringUtils.isEmpty(num)) {
                total = Integer.parseInt(num);
            }
          
            List<MatchResult> results = service.genRecUserResult(uid, total);
            result.put("result", results);
            result.put("num", results.size());

            List<String> ids = new ArrayList<String>();
            for (MatchResult mr : results) {
                ids.add(mr.getTargetId() + "");
            }
            if (ids.size() > 0) {
                showDao.show(uid, ids);
            }

        } catch (Exception e) {
            result.put("ec", 500);
            result.put("em", "the match result is error:" + e.getMessage());
        }

        String value = JsonUtils.toJSON(result);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(value.toString());
        out.close();
    }

}
