package com.xiaonei.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.xiaonei.db.utils.JsonUtils;
import com.xiaonei.pojo.MatchResult;
import com.xiaonei.rec.service.UserRecService;

public class UserRecServlet extends HttpServlet {
    private Logger logger = Logger.getLogger(UserRecServlet.class);
    private UserRecService service = new UserRecService();

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> result = null;
        String uid = "";
        String matchid = "";
        try {
            result = new HashMap<String, Object>();
            uid = request.getParameter("uid");
            matchid = request.getParameter("tid");
            if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(matchid)) {
                result.put("ec", 500);
                result.put("em", "the uid or matchid is null");
            } else {
                MatchResult mresult = service.calUserSim(uid, matchid);
                result.put("result", mresult);
            }

        } catch (Exception e) {
            result.put("ec", 500);
            result.put("em", "the match result is error:" + e.getMessage());
        }

        String value = JsonUtils.toJSON(result);

        logger.info("send response for the userid:" + uid + " matchid:"
                + matchid + " value:" + value);
        out.println(value.toString());
        out.close();
    }

}
