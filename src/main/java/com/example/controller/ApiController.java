package com.example.controller;


import com.example.model.Message;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.service.MessageService;
import com.example.service.UserService;
import com.example.util.DataUtil;
import com.example.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Qualifier("roleRepository")
    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/api/message/add")
    public Map<String, Object> addMessage(@RequestBody Map<String, Object> data){
        Message message = new Message();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User author = userService.findUserByEmail(auth.getName());
        if (author != null){
            message.setAuthor(author);
        } else {
            return DataUtil.getResponseMap(ResultCode.NEED_LOGIN, "Please login first!");
        }
        message.setContent((String)data.get("content"));

        try {
            Integer replyId = (Integer) data.get("reply_to");
            if (replyId != null) {
                message.setReplyTo(new Message(replyId));
            }
        } catch (Exception ignored){}

        try {
            Boolean anonymous = (Boolean) data.getOrDefault("anonymous", false);
            message.setStatus(anonymous ? 1 : 0);
        } catch (Exception ignored){}

        messageService.saveMessage(message);
        return DataUtil.getResponseMap(ResultCode.SUCCESS, "success");
    }

    @RequestMapping("/api/message/all")
    public Map<String, Object> getAllCheckedMessage(){
        List<Message> msgList = messageService.getAllCheckedMessages();
        List<Map<String, Object>> res = DataUtil.getResponseMsgList(msgList, false);
        return DataUtil.getResponseMap(ResultCode.SUCCESS, "success", res);
    }

    @RequestMapping("/api/admin/message/unchecked/all")
    public Map<String, Object> getAllUncheckedMessage(){
        if (!userCheck("ADMIN"))
            return DataUtil.getResponseMap(ResultCode.AUTHORIZED_PEOPLE_ONLY,
                    "Only authorized people can use this api!");

        List<Message> msgList = messageService.getAllUncheckedMessages();
        List<Map<String, Object>> res = DataUtil.getResponseMsgList(msgList, true);
        return DataUtil.getResponseMap(ResultCode.SUCCESS, "success", res);
    }

    @RequestMapping("/api/admin/message/check/{id}")
    public Map<String, Object> setMessageChecked(@PathVariable Long id){
        if (!userCheck("ADMIN"))
            return DataUtil.getResponseMap(ResultCode.AUTHORIZED_PEOPLE_ONLY,
                    "Only authorized people can use this api!");

        Message msg = messageService.getMessageById(id);
        int status = msg.getStatus();
        if (status < 100) msg.setStatus(status + 100);

        messageService.updateMessage(msg);
        return DataUtil.getResponseMap(ResultCode.SUCCESS, "success");
    }

    @RequestMapping("/api/admin/message/delete/{id}")
    public Map<String, Object> setMessageDeleted(@PathVariable Long id){
        if (!userCheck("ADMIN"))
            return DataUtil.getResponseMap(ResultCode.AUTHORIZED_PEOPLE_ONLY,
                    "Only authorized people can use this api!");

        Message msg = messageService.getMessageById(id);
        int status = msg.getStatus();
        if (status < 100) msg.setStatus(status + 10);

        messageService.updateMessage(msg);
        return DataUtil.getResponseMap(ResultCode.SUCCESS, "success");
    }

    private Boolean userCheck(Role role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        return userCheck(user, role);
    }

    private Boolean userCheck(String userRole){
        Role role = roleRepository.findByRole(userRole);
        return userCheck(role);
    }

    private Boolean userCheck(User user, Role role) {
        return (!(null == user) && user.getRoles().contains(role));
    }

    private Boolean userCheck(User user, String userRole) {
        Role role = roleRepository.findByRole(userRole);
        return userCheck(user, role);
    }
}
