package org.example.user_service.messageproducer;

import org.example.user_service.emailmessage.EmailMessage;
import org.example.user_service.emailmessage.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class MessageProducer {
    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.user.topic:user-topic}")
    private String userTopic;

    @Autowired
    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(OperationType operationType, String email) {
        log.info("MessageProducer. Попытка отправить уведомление об операции на электронную почту");
        try {
            EmailMessage emailMessage = new EmailMessage(operationType, email);
            String message = objectMapper.writeValueAsString(emailMessage);

            kafkaTemplate.send(userTopic, message);
            log.info("Уведомление об операции: {} был успешно отправлен на email: {}", operationType, email);
        } catch (Exception e) {
            log.error("Не удалось отправить уведомление об операции: {}  email: {}", operationType, email, e);
        }
    }
}
