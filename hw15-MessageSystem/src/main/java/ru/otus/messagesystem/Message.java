package ru.otus.messagesystem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ToString
@Getter
@Setter
public class Message {

    static final Message VOID_MESSAGE = new Message();

    private final UUID id = UUID.randomUUID();
    private final String from;
    private final String to;
    private final Optional<UUID> sourceMessageId;
    private final String type;
    private final int payloadLength;
    private final byte[] payload;

    public Message() {
        this.from = null;
        this.to = null;
        this.sourceMessageId = Optional.empty();
        this.type = "voidTechnicalMessage";
        this.payload = new byte[1];
        this.payloadLength = this.payload.length;
    }

    public Message(String from, String to, Optional<UUID> sourceMessageId, String type, byte[] payload) {
        this.from = from;
        this.to = to;
        this.sourceMessageId = sourceMessageId;
        this.type = type;
        this.payload = payload;
        this.payloadLength = this.payload.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
