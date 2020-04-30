package ru.otus.api.sessionManager;

import ru.otus.hibernate.sessionManager.DataBaseSessionHibernate;

public interface SessionManager extends AutoCloseable {

    void beginSession();

    void commitSession();

    void rollbackSession();

    void close();

    DataBaseSessionHibernate getCurrentSession();
}
