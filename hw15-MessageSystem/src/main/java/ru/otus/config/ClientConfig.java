package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.service.DBServiceUser;
import ru.otus.db.handlers.GetAddUserRequestHandler;
import ru.otus.db.handlers.GetAuthUserRequestHandler;
import ru.otus.db.handlers.GetDelUserRequestHandler;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClientImpl;
import ru.otus.messagesystem.MsClientName;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    private final DBServiceUser dbService;
    private final MessageSystem messageSystem;

    @Bean
    public MsClientImpl databaseMsClient(){
        MsClientImpl databaseMsClient = new MsClientImpl(MsClientName.DATABASE.getName(), messageSystem);

        databaseMsClient.addHandler(MessageType.AUTH_USER, new GetAuthUserRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.ADD_USER, new GetAddUserRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.DEL_USER, new GetDelUserRequestHandler(dbService));

        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }
}
