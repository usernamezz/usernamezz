package com.example.repository;


import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.model.Message;

import java.util.List;

@Repository("messageRepository")
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessagesByAuthor(User author);
    List<Message> findAllByStatusIn(List<Integer> status);
    Message findById(Long id);
}
