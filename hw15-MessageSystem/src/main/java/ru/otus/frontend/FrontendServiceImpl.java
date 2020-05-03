package ru.otus.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.controllers.UserPackage;
import ru.otus.domain.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FrontendServiceImpl implements FrontendService {
    private static final Logger logger = LoggerFactory.getLogger(FrontendService.class);

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void authUser(User user, Consumer<UserPackage> dataConsumer) {
        sendUserMessage(user, dataConsumer, MessageType.AUTH_USER);
    }

    @Override
    public void addUser(User user, Consumer<UserPackage> dataConsumer) {
        sendUserMessage(user, dataConsumer, MessageType.ADD_USER);
    }

    @Override
    public void delUser(User user, Consumer<UserPackage> dataConsumer) {
        sendUserMessage(user, dataConsumer, MessageType.DEL_USER);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null){
            logger.warn("consumer not found for : {}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }

    private void sendUserMessage(User user, Consumer<UserPackage> dataConsumer, MessageType messageType){
        Message outMessage = msClient.produceMessage(databaseServiceClientName, user, messageType);
        consumerMap.put(outMessage.getId(), dataConsumer);
        msClient.sendMessage(outMessage);
    }
}
