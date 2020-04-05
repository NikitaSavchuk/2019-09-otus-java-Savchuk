package ru.otus.frontend;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.otus.domain.User;
import ru.otus.messagesystem.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FrontendClientImpl implements FrontendClient {
    private final MessageSystemContext messageSystemContext;
    private final Address address = new Address("Front");
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void initialize() {
        messageSystemContext.getMessageSystem().addAddressee(this);
        messageSystemContext.setFrontAddress(address);
    }

    @Override
    public void createUser(User user) {
        MessageSystem messageSystem = messageSystemContext.getMessageSystem();
        Message message = new Message<DBClient>(address, messageSystemContext.getDbAddress()) {
            @Override
            public void exec(DBClient dbClient) {
                dbClient.createUser(user);
            }
        };
        messageSystem.sendMessage(message);
    }

    @Override
    public void showAllUserList() {
        MessageSystem messageSystem = messageSystemContext.getMessageSystem();
        Message message = new Message<DBClient>(address, messageSystemContext.getDbAddress()) {
            @Override
            public void exec(DBClient dbClient) {
                dbClient.getAllUserList();
            }
        };
        messageSystem.sendMessage(message);
    }


    @Override
    public void returnUserList(List<User> userList) {
        messagingTemplate.convertAndSend("/topic/response", userList);
    }

    @Override
    public Address getAddress() {
        return address;
    }
}

