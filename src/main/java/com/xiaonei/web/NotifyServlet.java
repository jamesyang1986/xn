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

public class NotifyServlet extends HttpServlet {

    private Logger logger = Logger.getLogger(PushServlet.class);
    private PushService service = new PushService();
    private UserDao userDao = new UserDao();

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        Map<String, Object> result = new HashMap<String, Object>();

        String uid = request.getParameter("uid");
        String nids = request.getParameter("nids");

        genNotify(result, uid, nids);
        String value = JsonUtils.toJSON(result);

        logger.info("finish notify request: the uid is:" + uid + " nids is"
                + nids + " the result is:" + value);
        out.println(value.toString());
        out.close();
    }

    public void genNotify(Map<String, Object> result, String uid, String nids) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("id", uid);

        List<XnUser> users = userDao.getCondition(paramMap);
        if (users == null || users.size() == 0) {
            result.put("ec", 500);
            result.put("em", "the user is not exists:" + uid);
        } else {
            XnUser originUser = users.get(0);
            String push_content = "提示：" + originUser.getNickname() + "上线了";

            Map<String, String> options = new HashMap<String, String>();
            options.put("nid", uid);
            options.put("type", "1");

            Map<String, String> result1 = new HashMap<String, String>();
            if (!StringUtils.isEmpty(uid) && !StringUtils.isEmpty(nids)) {
                String[] ids = nids.split(",");
                for (String nid : ids) {
                    try {
                        options.put("userid", nid);
                        sendNotify(push_content, options, result1, nid);
                    } catch (Exception e) {
                        result1.put(nid, "0");
                    }
                }
            }
            result.put("result", result1);
        }
    }

    private void sendNotify(String push_content, Map<String, String> options,
            Map<String, String> result1, String uid) throws Exception {
        PushCommand command = new PushCommand();
        command.setContent(push_content);
        command.setUid(uid);
        command.setType("1");
        command.setPlatformType(PlatformType.all);
        command.setAlias(uid);

        if (options.size() > 0) {
            command.setOptions(options);
        }
        service.pushUserNotice(command);
        result1.put(uid, "1");
    }

    public static void main(String[] args) {
        NotifyServlet notify = new NotifyServlet();
        notify.genNotify(new HashMap<String, Object>(), "17", "23,16");
    }

}
