package com.example.demo.repository;

import com.example.demo.model.Status;
import com.example.demo.repository.entity.MessageEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface MessageRepository extends CrudRepository<MessageEntity, Long> {

    List<MessageEntity> getMessagesByRecipientAndStatus(String recipient, Status status);

    List<MessageEntity> findAllMessagesByDateSentBetween(Date dateSentStart, Date dateSentEnd);
}
