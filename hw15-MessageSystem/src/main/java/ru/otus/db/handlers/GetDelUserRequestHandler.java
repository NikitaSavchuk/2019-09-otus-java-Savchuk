package ru.otus.db.handlers;

import ru.otus.api.service.DBServiceUser;
import ru.otus.app.common.Serializers;
import ru.otus.controllers.UserPackage;
import ru.otus.domain.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.ReqRespHandler;

import java.util.Optional;

public class GetDelUserRequestHandler implements ReqRespHandler {
    private final DBServiceUser dbServiceUser;

    public GetDelUserRequestHandler(DBServiceUser dbServiceUser) {
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    public Optional<Message> handle(Message message) {
        UserPackage data = new UserPackage();

        User receivedUserData = Serializers.deserialize(message.getPayload(), User.class);

        String login = receivedUserData.getLogin().trim();
        if (login.equals("")){
            data.setStatus("Login is empty.");
        } else if (login.equals("admin")){
            data.setStatus("Couldn't delete admin");
        } else {
            boolean success = dbServiceUser.deleteRecord(login);
            data.setStatus(
                    success
                            ? "User '" + login + "' was delete"
                            : "User '" + login + "' doesn't exist"
            );
        }
        data.setUsers(dbServiceUser.loadAll());

        return Optional.of(
                new Message(
                        message.getTo(), message.getFrom(), Optional.of(message.getId()),
                        MessageType.DEL_USER.getValue(), Serializers.serialize(data)
                )
        );
    }
}
