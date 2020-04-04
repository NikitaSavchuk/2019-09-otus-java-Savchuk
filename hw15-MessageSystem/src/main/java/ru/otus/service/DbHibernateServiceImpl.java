package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.backend.executor.DbExecutorHibernate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbHibernateServiceImpl<T> implements DBService<T> {
    private final DbExecutorHibernate<T> dbExecutorHibernate;

    @Override
    public T save(T entity) {
        return dbExecutorHibernate.create(entity);
    }

    @Override
    public Optional<T> get(long id, Class<T> clazz) {
        return Optional.of(dbExecutorHibernate.load(id, clazz));
    }

    @Override
    public T update(T entity) {
        return dbExecutorHibernate.update(entity);
    }

    @Override
    public List<T> getAll(Class<T> clazz) {
        return dbExecutorHibernate.loadAll(clazz);
    }
}
