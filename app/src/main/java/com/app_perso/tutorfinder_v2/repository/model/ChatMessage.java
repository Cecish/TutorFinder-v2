package com.app_perso.tutorfinder_v2.repository.model;

import com.app_perso.tutorfinder_v2.util.StringUtils;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private long messageTime;
    private String senderUsername;
    private String senderId;
    private String targetUsername;
    private String fromTo;

    public ChatMessage(String messageText, String senderId, String targetId, String senderUsername, String targetUsername) {
        this.messageText = messageText;
        fromTo = StringUtils.appendStringsAlphabetically(senderId, targetId);
        this.senderUsername = senderUsername;
        this.targetUsername = targetUsername;
        this.senderId = senderId;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){ }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getFromTo() {
        return fromTo;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}