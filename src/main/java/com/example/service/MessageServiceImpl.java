package com.example.service;

import com.example.model.Message;
import com.example.model.User;
import com.example.repository.MessageRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
    @Qualifier("messageRepository")
    @Autowired
    MessageRepository messageRepository;

    @Qualifier("userRepository")
    @Autowired
    UserRepository userRepository;

    @Override
    public List<Message> findMessagesByAuthor(User author) {
        return messageRepository.findMessagesByAuthor(author);
    }

    @Override
    public void saveMessage(Message message) {
        message.setId(null);
        messageRepository.save(message);
    }

    @Override
    public void updateMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<Message> getAllCheckedMessages() {
        return messageRepository.findAllByStatusIn(Arrays.asList(100, 101));
    }

    @Override
    public List<Message> getAllUncheckedMessages() {
        return messageRepository.findAllByStatusIn(Arrays.asList(0, 1));
    }

    @Override
    public Message getMessageById(Long id) {
        return messageRepository.findById(id);
    }

}
