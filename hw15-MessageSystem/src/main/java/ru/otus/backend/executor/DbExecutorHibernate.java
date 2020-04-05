package ru.otus.backend.executor;

import java.util.List;

public interface DbExecutorHibernate<T> {
    T create(T objectData);

    T update(T objectData);

    T createOrUpdate(T objectData);

    T load(long id, Class<T> clazz);

    List<T> loadAll(Class<T> clazz);
}
