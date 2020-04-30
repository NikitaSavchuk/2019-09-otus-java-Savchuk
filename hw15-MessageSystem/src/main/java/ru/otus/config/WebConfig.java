package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.frontend.FrontendService;
import ru.otus.frontend.FrontendServiceImpl;
import ru.otus.frontend.handlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig {
    private final MessageSystem messageSystem;

    @Bean
    public FrontendService frontendService(){

        MsClient frontendMsClient = new MsClientImpl(MsClientName.FRONTEND.getName(), messageSystem);

        FrontendService  frontendService = new FrontendServiceImpl(frontendMsClient, MsClientName.DATABASE.getName());

        frontendMsClient.addHandler(MessageType.AUTH_USER, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.ADD_USER, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.DEL_USER, new GetUserDataResponseHandler(frontendService));

        messageSystem.addClient(frontendMsClient);

        return frontendService;
    }
}
