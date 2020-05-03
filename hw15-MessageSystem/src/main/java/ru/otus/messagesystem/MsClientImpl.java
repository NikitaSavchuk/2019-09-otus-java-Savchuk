package ru.otus.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MsClientImpl implements MsClient {

    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    private final String name;
    private final MessageSystem messageSystem;
    private final Map<String, ReqRespHandler> handlers = new ConcurrentHashMap<>();

    public MsClientImpl(String name, MessageSystem messageSystem) {
        this.name = name;
        this.messageSystem = messageSystem;
    }

    @Override
    public void addHandler(MessageType type, ReqRespHandler reqRespHandler) {
        handlers.put(type.getValue(), reqRespHandler);
    }

    @Override
    public boolean sendMessage(Message message) {
        boolean result = messageSystem.newMessage(message);
        if (!result){
            logger.error("The last message was reject : {}", message);
        }
        return result;
    }

    @Override
    public void handle(Message message) {
        logger.info("New message : {}", message);
        try{
            ReqRespHandler reqRespHandler = handlers.get(message.getType());
            if (reqRespHandler != null){
                //< ?????
                reqRespHandler.handle(message).ifPresent(this::sendMessage);
            } else {
                logger.error("Handler not found for the message type : {}", message.getType());
            }
        } catch (Exception ex){
            logger.error("Message : {}, {}", message, ex);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType messageType) {
        return new Message(name, to, null, messageType.getValue(), Serializers.serialize(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
