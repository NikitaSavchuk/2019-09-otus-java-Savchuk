package ru.otus.backend.executor;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.domain.AddressDataSet;
import ru.otus.domain.PhoneDataSet;
import ru.otus.domain.User;

import java.util.List;
import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class DbHibernateExecutorHibernateImpl<T> implements DbExecutorHibernate<T> {
    private final SessionFactory sessionFactory;

    public DbHibernateExecutorHibernateImpl() {
        var configuration = new Configuration()
                .configure("hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    @Override
    public T create(T objectData) {
        return makeActionWithinHibernateSessionTransaction(objectData, Session::save);
    }

    @Override
    public T update(T objectData) {
        return makeActionWithinHibernateSessionTransaction(objectData, Session::merge);
    }

    @Override
    public T createOrUpdate(T objectData) {
        return makeActionWithinHibernateSessionTransaction(objectData, Session::saveOrUpdate);
    }

    @Override
    public T load(long id, Class<T> clazz) {
        T result;
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                result = session.get(clazz, id);
            } catch (Exception e) {
                session.getTransaction().rollback();
                return null;
            }
        }
        return result;
    }

    @Override
    public List<T> loadAll(Class<T> clazz) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            return session.createQuery("SELECT entity FROM " + clazz.getSimpleName() + " entity", clazz).getResultList();
        }
    }

    private T makeActionWithinHibernateSessionTransaction(T objectData, BiConsumer<Session, T> action) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                action.accept(session, objectData);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return objectData;
    }
}
