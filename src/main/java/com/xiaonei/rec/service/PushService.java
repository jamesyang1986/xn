package com.xiaonei.rec.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.xiaonei.db.utils.JedisPoolUtils;
import com.xiaonei.db.utils.JsonUtils;
import com.xiaonei.pojo.PlatformType;
import com.xiaonei.pojo.PushCommand;

public class PushService {

    protected static final Logger LOG = LoggerFactory
            .getLogger(PushService.class);

    private static final String PUSH_REDIS_KEY = "push_redis_queue";

    // private static final String appKey = "33d3a0c8c5285da05087c843";
    // private static final String masterSecret = "4c9c55426cea113a6c3b2b08";
    // //
    private static final String appKey = "729f76d7421fd6013cbf9c3c";
    private static final String masterSecret = "9ec9e2b7a6a73d87b15eb274";

    public static final String ALERT = "小内给你推荐, 您有新的推荐好友啦!";
    public static final String REGISTRATION_ID = "0900e8d85ef";

    public static final String TAG = "tag_api";
    private static PushClient client = new PushClient(masterSecret, appKey, 3);

    public static void main(String[] args) {
        PushCommand command = new PushCommand();
        command.setAlias("16");
        command.setContent(ALERT);
        command.setPlatformType(PlatformType.all);
        command.setTitle("有8个妹子看过你哦");
        command.setType("2");
        PushService pushService = new PushService();

        try {
            pushService.pushUserNotice(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PushResult pushUserMsg(PushCommand command) throws Exception {
        PushPayload payload = buildPushMsg(command);
        try {
            PushResult result = client.sendPush(payload);
            LOG.info("Got result - from uid:" + command.getUid() + result
                    + " the command is:" + JsonUtils.toJSON(command));
            return result;
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            throw e;
        } catch (APIRequestException e) {
            LOG.error(
                    "Error response from JPush server. Should review and fix it. ",
                    e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            throw e;
        }
    }

    private PushPayload buildPushMsg(PushCommand command) {
        Builder builder = PushPayload.newBuilder();

        if (command != null) {
            switch (command.getPlatformType()) {
            case android:
                builder.setPlatform(Platform.android());
                break;
            case ios:
                builder.setPlatform(Platform.ios());
                break;
            default:
                builder.setPlatform(Platform.android_ios());
                break;
            }

            cn.jpush.api.push.model.audience.Audience.Builder andienceBuilder = Audience
                    .newBuilder();
            if (!StringUtils.isEmpty(command.getTag())) {
                andienceBuilder.addAudienceTarget(AudienceTarget.tag(command
                        .getTag().trim().split("#")));
            }

            if (!StringUtils.isEmpty(command.getAlias())) {
                andienceBuilder.addAudienceTarget(AudienceTarget.alias(command
                        .getAlias().trim().split("#")));
            }

            builder.setAudience(andienceBuilder.build());
            Map<String, String> params = command.getOptions();
            params.put("userid", command.getUid());
            params.put("type", command.getType());
            params.putAll(command.getOptions());

            builder.setMessage(
                    Message.newBuilder().setMsgContent(command.getContent())
                            .addExtras(params).build()).build();
        }
        return builder.build();
    }

    public PushResult pushUserNotice(PushCommand command) throws Exception {
        PushPayload payload = buildPushCommand(command);

        try {
            PushResult result = client.sendPush(payload);
            LOG.info("Got result - " + result);
            return result;
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
            throw e;
        } catch (APIRequestException e) {
            LOG.error(
                    "Error response from JPush server. Should review and fix it. ",
                    e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
            throw e;
        }
    }

    private PushPayload buildPushCommand(PushCommand command) {
        Builder builder = PushPayload.newBuilder();

        if (command != null) {
            switch (command.getPlatformType()) {
            case android:
                builder.setPlatform(Platform.android());
                break;
            case ios:
                builder.setPlatform(Platform.ios());
                break;
            default:
                builder.setPlatform(Platform.android_ios());
                break;
            }

            cn.jpush.api.push.model.audience.Audience.Builder andienceBuilder = Audience
                    .newBuilder();
            if (!StringUtils.isEmpty(command.getTag())) {
                andienceBuilder.addAudienceTarget(AudienceTarget.tag(command
                        .getTag().trim().split("#")));
            }

            if (!StringUtils.isEmpty(command.getAlias())) {
                andienceBuilder.addAudienceTarget(AudienceTarget.alias(command
                        .getAlias().trim().split("#")));
            }

            builder.setAudience(andienceBuilder.build());
            // builder.setMessage(Message.content(command.getContent()));

            Map<String, String> params = command.getOptions();
            params.put("userid", command.getUid());
            params.put("type", command.getType());

            Notification notification = Notification
                    .newBuilder()
                    .setAlert(command.getContent())
                    .addPlatformNotification(
                            AndroidNotification.newBuilder()
                                    .setTitle(command.getTitle())
                                    .addExtra("userid", command.getUid())
                                    .addExtra("type", command.getType())
                                    .addExtras(params).build())
                    .addPlatformNotification(
                            IosNotification.newBuilder()
                                    .setAlert(command.getTitle()).incrBadge(1)
                                    .addExtra("userid", command.getUid())
                                    .addExtra("type", command.getType())
                                    .addExtras(params).build()).build();

            builder.setNotification(notification);
        }
        return builder.build();
    }

    private void pushNotice(String content) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            jedis.lpush(PUSH_REDIS_KEY, content);
        } catch (Exception e) {
            if (jedis != null) {
                JedisPoolUtils.returnBrokenRes(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnRes(jedis);
            }
        }
    }

    private String popNotice() {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            return jedis.rpop(PUSH_REDIS_KEY);
        } catch (Exception e) {
            if (jedis != null) {
                JedisPoolUtils.returnBrokenRes(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                JedisPoolUtils.returnRes(jedis);
            }
        }
        return null;
    }
}
