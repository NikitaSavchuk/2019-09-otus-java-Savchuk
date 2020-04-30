package ru.otus.hibernate.dao;

import lombok.RequiredArgsConstructor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.api.dao.UserDao;
import ru.otus.api.dao.UserDaoException;
import ru.otus.api.sessionManager.SessionManager;
import ru.otus.domain.User;
import ru.otus.hibernate.sessionManager.DataBaseSessionHibernate;
import ru.otus.hibernate.sessionManager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoHibernate implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    @Override
    public Optional<User> loadRecord(long id) {
        DataBaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try{
            return Optional.ofNullable(
                    currentSession.getSession().find(User.class, id)
            );
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }

        return Optional.empty();
    }

    @Override
    public List<User> loadRecord(String login) {

        DataBaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try{
            Criteria criteria = currentSession.getSession().createCriteria(User.class);
            List records = criteria.add(Restrictions.eq("login", login)).list();

            List<User> retList = new ArrayList<>();
            for (Object record : records) {
                retList.add((User) record);
            }

            return retList;
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }

        return new ArrayList<>();
    }

    @Override
    public List<User> loadAll() {
        DataBaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try{
            List<User> retList = new ArrayList<>();

            Criteria criteria = currentSession.getSession().createCriteria(User.class);
            List onlineUsers = criteria.list();

            for (Object user : onlineUsers) {
                retList.add((User) user);
            }
            return retList;
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }

        return new ArrayList<>();
    }

    @Override
    public boolean createRecord(User user) {
        DataBaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try{
            if (user.getId() == 0){
                currentSession.getSession().persist(user);
                return true;
            }

            logger.error("This record already exist");
            return false;
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            throw new UserDaoException(ex);
        }
    }

    @Override
    public boolean updateRecord(User user) {
        DataBaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try{
            if (user.getId() > 0){
                currentSession.getSession().merge(user);
                return true;
            }

            logger.error("The record doesn't exist");
            return false;
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            throw new UserDaoException(ex);
        }
    }

    @Override
    public boolean deleteRecord(String login) {
        DataBaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try{
            List<User> records = loadRecord(login);
            if (records.size() > 0){
                for (User record : records) {
                    currentSession.getSession().delete(record);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            throw new UserDaoException(ex);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
