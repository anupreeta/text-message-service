package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.model.Status;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.entity.MessageEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MessageServiceTest {

    private MessageService target;

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        target = new MessageService(messageRepository, modelMapper);
        initData();
    }

    @AfterEach
    void deleteAll() {
        messageRepository.deleteAll();
    }

    @Test
    void shouldCreateNewMessage() {
        Message message = new Message();
        String recipient = "anu";
        message.setRecipient(recipient);
        String content = "Hi again Anu!";
        message.setContent(content);

        Message result = target.createMessage(message);

        assertThat(result.getContent(), is(content));
        assertThat(result.getRecipient(), is(recipient));
        assertThat(result.getStatus(), is(Status.NEW));
        assertThat(result.getDateSent(), is(notNullValue()));
    }

    @Test
    void shouldFetchMessagesAndUpdateMessageStatus() {
        List<Message> messages = new ArrayList<>(target.fetchMessagesByStatus("erika", "NEW"));

        assertThat(messages.size(), is(2));

        //when the message is fetched, the status changes to FETCHED
        messages.forEach(message -> {
            assertThat(message.getStatus(), is(Status.FETCHED));
        });

        //the second time the messages is fetched with status NEW, the size should be zero
        messages = target.fetchMessagesByStatus("erika", "NEW");
        assertThat(messages.size(), is(0));
    }

    @Test
    void shouldFetchAllMessagesBasedOnTimeStartAndStopIndex() throws ParseException {
        MessageEntity firstEntity = messageEntity("peter", "hello Peter");
        firstEntity.setDateSent(new SimpleDateFormat("HH:mm").parse("00:05"));
        MessageEntity secondEntity = messageEntity("alice", "hello alice");
        secondEntity.setDateSent(new SimpleDateFormat("HH:mm").parse("00:10"));
        messageRepository.save(firstEntity);
        messageRepository.save(secondEntity);

        List<Message> results = target.findAllMessagesBetweenDateSent("00:01", "00:15");
        assertThat(results.size(), is(2));
    }

    @Test
    void shouldDeleteAllMessages() {
        assertEquals(messageRepository.count(), 4L);

        target.deleteMessages(null);

        assertEquals(messageRepository.count(), 0L);
    }

    @Test
    void shouldDeleteMessageById() {

    }

    void initData() {
        MessageEntity messageEntity1 = messageEntity("gaurav", "Hello Gaurav!");
        MessageEntity messageEntity2 = messageEntity("anu", "Hej, Anu!");
        MessageEntity messageEntity3 = messageEntity("erika", "Good morning, Erika!");
        MessageEntity messageEntity4 = messageEntity("erika", "Good morning again, Erika!");
        messageRepository.save(messageEntity1);
        messageRepository.save(messageEntity2);
        messageRepository.save(messageEntity3);
        messageRepository.save(messageEntity4);
    }

    private MessageEntity messageEntity(String recipient, String content) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setRecipient(recipient);
        messageEntity.setContent(content);
        messageEntity.setDateSent(new Date());
        messageEntity.setStatus(Status.NEW);
        return messageEntity;
    }

}