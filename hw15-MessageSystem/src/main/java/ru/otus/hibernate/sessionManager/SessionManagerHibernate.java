package ru.otus.hibernate.sessionManager;

import lombok.experimental.NonFinal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.api.sessionManager.SessionManager;
import ru.otus.api.sessionManager.SessionManagerException;

public class SessionManagerHibernate implements SessionManager {

    @NonFinal
    private DataBaseSessionHibernate dataBaseSession;

    private final SessionFactory sessionFactory;

    public SessionManagerHibernate(SessionFactory sessionFactory) {
        if (sessionFactory == null)
            throw new SessionManagerException("SessionFactory is null");
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void beginSession() {
        try{
            dataBaseSession = new DataBaseSessionHibernate(sessionFactory.openSession());
        } catch (Exception ex){
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public void commitSession() {
        checkSessionAndTransaction();
        try{
            dataBaseSession.getTransaction().commit();
            dataBaseSession.getSession().close();
        } catch (Exception ex){
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public void rollbackSession() {
        checkSessionAndTransaction();
        try {
            dataBaseSession.getTransaction().rollback();
            dataBaseSession.getSession().close();
        } catch (Exception ex) {
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public void close() {
        if (dataBaseSession == null) {
            return;
        }
        Session session = dataBaseSession.getSession();
        if (session == null || !session.isConnected()) {
            return;
        }

        Transaction transaction = dataBaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            return;
        }

        try {
            dataBaseSession.close();
            dataBaseSession = null;
        } catch (Exception ex) {
            throw new SessionManagerException(ex);
        }
    }

    @Override
    public DataBaseSessionHibernate getCurrentSession() {
        checkSessionAndTransaction();
        return dataBaseSession;
    }

    private void checkSessionAndTransaction(){
        if (dataBaseSession == null)
            throw new SessionManagerException("DataSession not opened");

        Session session = dataBaseSession.getSession();
        if (session == null || !session.isConnected())
            throw new SessionManagerException("Session not opened");

        Transaction transaction = dataBaseSession.getTransaction();
        if (transaction == null || !transaction.isActive())
            throw new SessionManagerException("Session not opened");
    }
}
