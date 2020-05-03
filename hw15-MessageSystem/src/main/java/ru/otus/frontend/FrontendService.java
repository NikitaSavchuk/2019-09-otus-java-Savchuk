package ru.otus.frontend;

import ru.otus.controllers.UserPackage;
import ru.otus.domain.User;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {
    void authUser(User user, Consumer<UserPackage> dataConsumer);

    void addUser(User user, Consumer<UserPackage> dataConsumer);

    void delUser(User user, Consumer<UserPackage> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}
