package ru.otus.hibernate.handlers;

import com.google.gson.Gson;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.domain.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.common.Serializers;

import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {
    private final DBServiceCachedUser dbService;
    private final Gson gson = new Gson();

    public GetUserDataRequestHandler(DBServiceCachedUser dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        String jsonUserData = Serializers.deserialize(msg.getPayload(), String.class);

        //Получив сообщение, мы сохраняем пользователя, создав объект User из JSON строки
        User newUser = gson.fromJson(jsonUserData, User.class);
        long savedUserID = dbService.saveUser(newUser);

        //После сохранения user-у нас есть его ID, по котому мы получим данные из кеша
        User cachedUser = dbService.getUser(savedUserID).get();

        //Формируем новый JSON и сообщение для отправки во фронт-сервис
        String savedUserData = gson.toJson(cachedUser);

        return Optional.of(new Message(msg.getTo(), msg.getFrom(), Optional.of(msg.getId()),
                MessageType.USER_DATA.getValue(), Serializers.serialize(savedUserData)));
    }
}
