package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.frontend.FrontendService;
import ru.otus.frontend.FrontendServiceImpl;
import ru.otus.frontend.handlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.*;

@Configuration
@RequiredArgsConstructor
public class FrontendServiceConfig {

    private final MessageSystem messageSystem;

    @Bean("frontendMsClient")
    public MsClient msClient() {
        return new MsClientImpl(MsClientName.FRONTEND.getName(), messageSystem);
    }

    @Bean
    public FrontendService frontendService(
            @Qualifier("frontendMsClient") MsClient frontendMsClient) {

        FrontendService frontendService = new FrontendServiceImpl(frontendMsClient, MsClientName.DATABASE.getName());

        frontendMsClient.addHandler(MessageType.AUTH_USER, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.ADD_USER, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.DEL_USER, new GetUserDataResponseHandler(frontendService));

        messageSystem.addClient(frontendMsClient);

        return frontendService;
    }
}
