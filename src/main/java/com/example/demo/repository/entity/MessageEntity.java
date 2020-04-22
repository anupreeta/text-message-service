package com.example.demo.repository.entity;

import com.example.demo.model.Status;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipient;
    private String content;
    @Temporal(TemporalType.TIME)
    private Date dateSent;
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
