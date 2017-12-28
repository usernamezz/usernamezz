package com.example.util;

import com.example.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {

    public static Map<String, Object> getResponseMap(ResultCode code, String msg){
        return getResponseMap(code.getCode(), msg);
    }

    public static Map<String, Object> getResponseMap(ResultCode code, String msg, Object data){
        return getResponseMap(code.getCode(), msg, data);
    }

    public static Map<String, Object> getResponseMap(int code, String msg){
        return getResponseMap(code, msg, null);
    }

    public static Map<String, Object> getResponseMap(int code, String msg, Object data){
        HashMap<String, Object> res = new HashMap<>();
        res.put("code", code);
        res.put("msg", msg);
        if (data != null)
            res.put("data", data);
        return res;
    }

    public static List<Map<String, Object>> getResponseMsgList(List<Message> msgList, Boolean showAnonymous){
        List<Map<String, Object>> res = new ArrayList<>();
        for (Message msg : msgList) {
            Map<String, Object> resMsg = new HashMap<>();
            resMsg.put("msg_id", msg.getId());
            if (msg.getStatus() == 100 || msg.getStatus() == 0) {
                resMsg.put("author", msg.getAuthor().getUsername());
                resMsg.put("author_id", msg.getAuthor().getId());
            } else if (msg.getStatus() == 101 || msg.getStatus() == 1) {
                String authorName = showAnonymous ? msg.getAuthor().getUsername() + "(Anonymous)" : "Anonymous";
                resMsg.put("author", authorName);
                resMsg.put("author_id", 0);
            }
            resMsg.put("content", msg.getContent());
            resMsg.put("post_time", msg.getPostTime().getTime());
            if (msg.getReplyTo() != null)
                resMsg.put("reply_to", msg.getReplyTo().getId());
            res.add(resMsg);
        }
        return res;
    }
}
