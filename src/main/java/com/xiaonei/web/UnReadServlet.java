package com.xiaonei.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.xiaonei.dao.UserDao;
import com.xiaonei.dao.UserShowDao;
import com.xiaonei.db.utils.JsonUtils;
import com.xiaonei.pojo.PlatformType;
import com.xiaonei.pojo.PushCommand;
import com.xiaonei.pojo.XnUser;
import com.xiaonei.rec.service.PushService;

public class UnReadServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(PushServlet.class);
    private PushService service = new PushService();

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
       
        try {
            String uid = request.getParameter("uid");
            String cnum = request.getParameter("cnum");
            String znum = request.getParameter("znum");

            PushCommand command = new PushCommand();
            command.setContent("new comments or zan...");
            command.setUid(uid);
            command.setType("3");
            command.setPlatformType(PlatformType.all);
            command.setAlias(uid);

            Map<String, String> options = new HashMap<String, String>();
            options.put("cnum", cnum);
            options.put("znum", znum);
            options.put("type", command.getType());

            if (options.size() > 0) {
                command.setOptions(options);
            }
            
            service.pushUserMsg(command);
            logger.info("finish send unread notify: the uid is:" + uid + ""
                    + JsonUtils.toJSON(options));
            out.println("ok");
        } catch (Exception e) {
            out.println("error:" + e.getMessage());
        }
        out.close();
    }

    public static void main(String[] args) {
        UnReadServlet notify = new UnReadServlet();
    }

}
