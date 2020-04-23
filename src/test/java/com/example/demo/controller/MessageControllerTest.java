package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.model.Status;
import com.example.demo.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @Test
    void shouldSendMessage() throws Exception {
        Message message = getMessage();

        String requestBody = requestBody();

        when(messageService.createMessage(any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipient", is("anu")))
                .andExpect(jsonPath("$.content", is("Hi again Anu!")))
                .andExpect(jsonPath("$.status", is("NEW")));
    }

    @Test
    void shouldGetMessagesByRecipientAndStatus() throws Exception {
        Message message = getMessage();
        message.setStatus(Status.FETCHED);

        when(messageService.fetchMessagesByStatus(any(String.class), any(String.class))).thenReturn(Collections.singletonList(message));

        mockMvc.perform(get("/messages?recipient=anu&status=NEW")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipient", is("anu")))
                .andExpect(jsonPath("$[0].content", is("Hi again Anu!")))
                .andExpect(jsonPath("$[0].status", is("FETCHED")));
    }

    @Test
    void shouldGetAllMessagesByTime() throws Exception {
        Message message = getMessage();
        message.setStatus(Status.FETCHED);

        when(messageService.findAllMessagesBetweenDateSent(any(String.class), any(String.class))).thenReturn(Collections.singletonList(message));

        mockMvc.perform(get("/messages?startTime=00:01&stopTime=23:00")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipient", is("anu")))
                .andExpect(jsonPath("$[0].content", is("Hi again Anu!")))
                .andExpect(jsonPath("$[0].status", is("FETCHED")));
    }

    @Test
    void deleteAllMessages() throws Exception {
        mockMvc.perform(delete("/messages"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteMessageById() throws Exception {
        mockMvc.perform(delete("/messages?messageId=1"))
                .andExpect(status().isNoContent());
    }

    private String requestBody() throws JsonProcessingException {
        Message message = getMessage();
        return new ObjectMapper().writeValueAsString(message);
    }

    private Message getMessage() {
        Message message = new Message();
        String recipient = "anu";
        message.setRecipient(recipient);
        String content = "Hi again Anu!";
        message.setContent(content);
        message.setStatus(Status.NEW);
        message.setDateSent(new Date());
        return message;
    }
}