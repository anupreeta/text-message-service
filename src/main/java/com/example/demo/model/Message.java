package com.example.demo.model;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class Message {

    @NotEmpty(message = "recipient cannot be null or empty")
    private String recipient;
    @NotEmpty(message = "content cannot be null or empty")
    private String content;
    private Status status;
    private Date dateSent;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }
}
