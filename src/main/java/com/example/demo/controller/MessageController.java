package com.example.demo.controller;

import com.example.demo.model.Message;
import com.example.demo.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import sun.tools.jconsole.Plotter;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.List;

@RestController
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return new ResponseEntity<>(createdMessage, HttpStatus.OK);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessagesByRecipientAndStatus(
            @RequestParam("recipient") @Nullable String recipient,
            @RequestParam("status")  @Nullable String status,
            @RequestParam("startTime") @Nullable String startTime, //format HH:mm
            @RequestParam("stopTime") @Nullable String stopTime   // format HH:mm
            ) throws ParseException {
        checkParams(recipient, status,  startTime, stopTime);
        if(recipient != null && status != null) {
            return new ResponseEntity<>(messageService.fetchMessagesByStatus(recipient, status), HttpStatus.OK);
        } else if(startTime != null && stopTime != null) {
            return new ResponseEntity<>(messageService.findAllMessagesBetweenDateSent(startTime, stopTime), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/messages")
    public ResponseEntity<Plotter.Unit> deleteMessages(@RequestParam @Nullable Long messageId) {
        messageService.deleteMessages(messageId);

        return ResponseEntity.noContent().build();
    }

    public void checkParams(String recipient, String status, String startTime, String stopTime) {
        if(recipient != null && status != null && startTime != null && stopTime != null) {
            throw new IllegalArgumentException("Invalid request parameters combination");
        }
    }
}
