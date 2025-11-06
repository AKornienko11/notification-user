package org.example.user_service.emailmessage;

import javax.imageio.spi.ServiceRegistry;

public class EmailMessage {
    OperationType operationType;
     String email;


    @Override
    public String toString() {
        return "EmailMessage{" +
                "operationType=" + operationType +
                ", email='" + email + '\'' +
                '}';
    }

    public EmailMessage(OperationType operationType, String email) {
        this.operationType = operationType;
        this.email = email;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

