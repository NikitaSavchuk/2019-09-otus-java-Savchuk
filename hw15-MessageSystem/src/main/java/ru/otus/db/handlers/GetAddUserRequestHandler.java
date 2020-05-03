package ru.otus.db.handlers;

import ru.otus.api.service.DBServiceUser;
import ru.otus.app.common.Serializers;
import ru.otus.controllers.UserPackage;
import ru.otus.domain.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.ReqRespHandler;

import java.util.List;
import java.util.Optional;

public class GetAddUserRequestHandler implements ReqRespHandler {
    private final DBServiceUser dbServiceUser;

    public GetAddUserRequestHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message message) {

        UserPackage data = new UserPackage();
        User receivedUserData = Serializers.deserialize(message.getPayload(), User.class);
        String login = receivedUserData.getLogin().trim();
        String password = receivedUserData.getPassword().trim();

        if (!login.equals("") && !password.equals("")){
            List<User> loadedUsers = dbServiceUser.loadRecord(login);
            if (loadedUsers.size() == 0){
                dbServiceUser.createRecord(new User(0, login, password, false));
                data.setStatus("User '" + login + "' was add.");
            } else {
                data.setStatus("User '"+login+"' already exists.");
            }
        } else {
            data.setStatus("Login or/and password is empty.");
        }

        data.setUsers(dbServiceUser.loadAll());

        return Optional.of(
                new Message(
                        message.getTo(), message.getFrom(), Optional.of(message.getId()),
                        MessageType.ADD_USER.getValue(), Serializers.serialize(data)
                )
        );
    }
}
