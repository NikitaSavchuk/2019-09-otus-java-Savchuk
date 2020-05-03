package ru.otus.messagesystem;

public enum MessageType {
    AUTH_USER("AuthUser"),
    ADD_USER("AddUser"),
    DEL_USER("DelUser");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
