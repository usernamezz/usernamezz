package com.example.service;

import com.example.model.Message;
import com.example.model.User;

import java.util.List;

public interface MessageService {
    List<Message> findMessagesByAuthor(User author);
    void saveMessage(Message message);
    void updateMessage(Message message);
    List<Message> getAllCheckedMessages();
    List<Message> getAllUncheckedMessages();
    Message getMessageById(Long id);
}
