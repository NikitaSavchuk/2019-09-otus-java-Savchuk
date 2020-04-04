package ru.otus.service;

import java.util.List;
import java.util.Optional;

public interface DBService<T> {
    T save(T entity);

    Optional<T> get(long id, Class<T> clazz);

    T update(T entity);

    List<T> getAll(Class<T> clazz);
}
