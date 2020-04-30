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

public class GetAuthUserRequestHandler implements ReqRespHandler {
    private final DBServiceUser dbServiceUser;

    public GetAuthUserRequestHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message message) {
        UserPackage data = new UserPackage();
        User receivedUser = Serializers.deserialize(message.getPayload(), User.class);

        String login = receivedUser.getLogin().trim();
        String password = receivedUser.getPassword().trim();

        List<User> users = dbServiceUser.loadRecord(login);

        if (users.size() > 0 ){
            User user = users.get(0);
            if (user.getPassword().equals(password)){
                if (user.isAdmin()) {
                    data.setUsers(dbServiceUser.loadAll());
                    data.setStatus("admin");
                } else {
                    user.setId(0);
                    user.setPassword("");
                    data.getUsers().add(user);
                    data.setStatus("user");
                }
            } else {
                data.setStatus("Wrong Login and/or Password");
            }
        } else {
            data.setStatus("Wrong Login and/or Password");
        }

        return Optional.of(
                new Message(
                        message.getTo(), message.getFrom(), Optional.of(message.getId()),
                        MessageType.AUTH_USER.getValue(), Serializers.serialize(data)
                )
        );
    }
}
