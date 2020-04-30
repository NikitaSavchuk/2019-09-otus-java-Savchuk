package ru.otus.frontend.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.common.Serializers;
import ru.otus.frontend.FrontendService;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.ReqRespHandler;

import java.util.Optional;
import java.util.UUID;

public class GetUserDataResponseHandler implements ReqRespHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserDataResponseHandler.class);

    private final FrontendService frontendService;

    public GetUserDataResponseHandler(FrontendService frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message message) {
        logger.info("new message : {}", message);
        try{
            Object userData = Serializers.deserialize(message.getPayload(), Object.class);
            UUID sourceMessageId = message.getSourceMessageId().orElseThrow(
                    () -> new RuntimeException("Not found sourceMessage for message : {}" + message.getId())
            );

            frontendService.takeConsumer(sourceMessageId, Object.class).ifPresent(
                    consumer -> consumer.accept(userData)
            );
        } catch(Exception ex){
            logger.error("message : {}, {}", message, ex);
        }

        return Optional.empty();
    }
}
