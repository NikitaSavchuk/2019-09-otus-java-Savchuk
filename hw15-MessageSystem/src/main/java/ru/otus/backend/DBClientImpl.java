package ru.otus.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.domain.User;
import ru.otus.messagesystem.*;
import ru.otus.service.DBService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DBClientImpl implements DBClient {
    private final MessageSystemContext messageSystemContext;
    private final Address address = new Address("DB");
    private final DBService<User> dbService;

    @PostConstruct
    public void initialize() {
        messageSystemContext.getMessageSystem().addAddressee(this);
        messageSystemContext.setDbAddress(address);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void createUser(User entity) {
        dbService.save(entity);
        List<User> all = dbService.getAll(User.class);
        returnResult(all);
    }

    private void returnResult(List<User> all) {
        MessageSystem messageSystem = messageSystemContext.getMessageSystem();
        Message message = new Message<FrontendClient>(address, messageSystemContext.getFrontAddress()) {
            @Override
            public void exec(FrontendClient frontendClient) {
                frontendClient.returnUserList(all);
            }
        };
        messageSystem.sendMessage(message);
    }

    @Override
    public void getAllUserList() {
        List<User> all = dbService.getAll(User.class);
        returnResult(all);
    }
}
