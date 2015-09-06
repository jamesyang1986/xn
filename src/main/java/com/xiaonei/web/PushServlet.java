package com.xiaonei.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.xiaonei.pojo.PlatformType;
import com.xiaonei.pojo.PushCommand;
import com.xiaonei.rec.service.PushService;

public class PushServlet extends HttpServlet {
    private PushService service = new PushService();

    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        JSONObject result = new JSONObject();
        try {
            JSONObject json = new JSONObject(readJSONString(request));
            String uid = json.getString("uid");
            String type = json.getString("type");
            String push_content = json.getString("push_content");

            Map<String, String> options = new HashMap<String, String>();

            if (json.has("extra_params")) {
                JSONObject extra_params = json.getJSONObject("extra_params");
                Iterator<String> keys = extra_params.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = extra_params.getString("key");
                    options.put(key, value);
                }
            }

            PushCommand command = new PushCommand();
            command.setContent(push_content);
            command.setUid(uid);
            command.setType(type);
            command.setPlatformType(PlatformType.all);
            command.setAlias(uid);

            if (options.size() > 0) {
                command.setOptions(options);
            }
            service.pushUserNotice(command);
            result.put("ec", "200");
        } catch (Exception e) {
            result.put("ec", "500");
            result.put("em", "fail to send..." + e.getMessage());
        }
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result.toString());
        out.close();
    }

    private String readJSONString(HttpServletRequest request) {
        StringBuffer json = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return json.toString();
    }
}
