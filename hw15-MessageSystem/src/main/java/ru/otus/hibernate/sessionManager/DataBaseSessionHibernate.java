package ru.otus.hibernate.sessionManager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.otus.api.sessionManager.DataBaseSession;

public class DataBaseSessionHibernate implements DataBaseSession {

    private final Session session;

    private final Transaction transaction;

    DataBaseSessionHibernate(Session session) {
        this.session = session;
        this.transaction = session.beginTransaction();
    }

    public Session getSession() {
        return session;
    }

    Transaction getTransaction() {
        return transaction;
    }

    void close() {
        if (transaction.isActive())
            transaction.commit();
        session.close();
    }
}
