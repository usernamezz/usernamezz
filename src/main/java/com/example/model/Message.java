package com.example.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="msg_id")
    private Long id;
    @Column(name="content")
    private String content;
    @Column(name="post_time", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date postTime;
    @Column(name="status", columnDefinition="INTEGER DEFAULT 0")
    private int status;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH })
    @JoinColumn(name = "reply_to")
    private Message replyTo;
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional=false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public Message(){}

    public Message(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Override
    public String toString(){
        return String.format("Message(id=%s, author=%s, content=%s, post_time=%s, reply_to=%s)",
                id, author, content, postTime, replyTo);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
