package ru.otus.api.service;

import org.springframework.stereotype.Service;
import ru.otus.api.dao.UserDao;
import ru.otus.api.sessionManager.SessionManager;
import ru.otus.domain.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DBServiceUserImpl implements DBServiceUser {
    private final static Logger logger = LoggerFactory.getLogger(DBServiceUserImpl.class);
    private final UserDao dao;

    @Override
    public boolean createRecord(User user) {
        try(SessionManager sessionManager = dao.getSessionManager()){
            sessionManager.beginSession();
            try{
                boolean success = dao.createRecord(user);
                if (success){
                    sessionManager.commitSession();
                    logger.info("The record was create");
                } else {
                    sessionManager.rollbackSession();
                    logger.error("The record wasn't create");
                }

                return success;
            } catch(Exception ex){
                logger.error(ex.getMessage(), ex);
                sessionManager.rollbackSession();
                throw new DBServiceException(ex);
            }
        }
    }

    @Override
    public boolean updateRecord(User user) {
        try(SessionManager sessionManager = dao.getSessionManager()){
            sessionManager.beginSession();
            try{
                boolean success = dao.updateRecord(user);
                if (success){
                    sessionManager.commitSession();
                    logger.info("The record was update");
                } else {
                    sessionManager.rollbackSession();
                    logger.info("The record wasn't update");
                }
                return success;
            } catch(Exception ex){
                logger.error(ex.getMessage(), ex);
                sessionManager.rollbackSession();
                throw new DBServiceException(ex);
            }
        }
    }

    @Override
    public Optional<User> loadRecord(long id) {
        try(SessionManager sessionManager = dao.getSessionManager()){
            sessionManager.beginSession();
            try{
                Optional<User> record = dao.loadRecord(id);
                logger.info("loaded record : {}", record.orElse(null));
                return record;
            } catch (Exception ex){
                logger.error(ex.getMessage(), ex);
                sessionManager.rollbackSession();
            }

            return Optional.empty();
        }
    }

    @Override
    public List<User> loadRecord(String login) {
        try(SessionManager sessionManager = dao.getSessionManager()){
            sessionManager.beginSession();
            try{
                List<User> records = dao.loadRecord(login);
                logger.info("loaded records : " + records);
                return records;
            } catch (Exception ex){
                logger.error(ex.getMessage(), ex);
                sessionManager.rollbackSession();
            }

            return new ArrayList<>();
        }
    }

    @Override
    public List<User> loadAll() {
        try(SessionManager sessionManager = dao.getSessionManager()){
            sessionManager.beginSession();
            try{
                List<User> records = dao.loadAll();
                logger.info("loaded records : " + records);
                return records;
            } catch (Exception ex){
                logger.error(ex.getMessage(), ex);
                sessionManager.rollbackSession();
            }

            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteRecord(String login) {
        try(SessionManager sessionManager = dao.getSessionManager()){
            sessionManager.beginSession();
            try{
                boolean success = dao.deleteRecord(login);
                if (success){
                    sessionManager.commitSession();
                    logger.info("The record was create");
                } else {
                    sessionManager.rollbackSession();
                    logger.info("The record wasn't create");
                }

                return success;
            } catch(Exception ex){
                logger.error(ex.getMessage(), ex);
                sessionManager.rollbackSession();
                throw new DBServiceException(ex);
            }
        }
    }
}
