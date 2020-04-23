package com.example.demo.service;

import com.example.demo.model.Message;
import com.example.demo.model.Status;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.entity.MessageEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private ModelMapper modelMapper;
    public MessageService(MessageRepository messageRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }

    public Message createMessage(Message message) {
        MessageEntity messageEntity = toMessageEntity(message);
        messageEntity.setStatus(Status.NEW);
        messageEntity.setDateSent(new Date());
        MessageEntity saved = messageRepository.save(messageEntity);
        return toMessage(saved);
    }

    public List<Message> fetchMessagesByStatus(String recipient, String status){
        Status messageStatus = Status.valueOf(status);
        List<MessageEntity> messages = updateMessagesStatus(recipient, messageStatus);
        return messages.stream()
                .map(this::toMessage)
                .collect(toList());
    }

    public List<Message> findAllMessagesBetweenDateSent(String startHourIndex, String stopHourIndex) throws ParseException {
        Date dateStartIndex = new SimpleDateFormat("HH:mm").parse(startHourIndex);
        Date dateStopIndex = new SimpleDateFormat("HH:mm").parse(stopHourIndex);
        List<MessageEntity> messageEntities = messageRepository.findAllMessagesByDateSentBetween(dateStartIndex, dateStopIndex);
        return messageEntities.stream()
                .map(this::toMessage)
                .collect(toList());
    }

    public void deleteMessages(Long messageId) {
        if(messageId != null && !messageRepository.existsById(messageId)) {
            throw new EntityNotFoundException("Message id does not exist");
        }
        if(messageId != null) {
            messageRepository.deleteById(messageId);
        } else {
            messageRepository.deleteAll();
        }
    }

    private List<MessageEntity> updateMessagesStatus(String recipient, Status messageStatus) {
        List<MessageEntity> messagesByStatus = messageRepository.getMessagesByRecipientAndStatus(recipient, messageStatus);
        messagesByStatus.forEach(m -> {
            m.setStatus(Status.FETCHED);
            messageRepository.save(m);
        });
        return messagesByStatus;
    }

    private MessageEntity toMessageEntity(Message messsage) {
        return modelMapper.map(messsage, MessageEntity.class);
    }

    private Message toMessage(MessageEntity messageEntity) {
        return modelMapper.map(messageEntity, Message.class);
    }
}
