package com.alisawalter.flashchatnewfirebase;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class Message {
    private String messageText;
    private String messageAuthor;
    private Date messageTime;

    public Message() {
    }

    public Message(String messageText, String messageAuthor, Date messageTime) {
        this.messageText = messageText;
        this.messageAuthor = messageAuthor;
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageAuthor() {
        return messageAuthor;
    }

    public Date getMessageTime() {
        return messageTime;
    }
}
