package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.api.sessionmanager.SessionManager;
import ru.otus.h2.DataSourceH2;
import ru.otus.helpers.db.DbUtils;
import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.metadata.ObjectMetaDataHolder;
import ru.otus.model.Account;
import ru.otus.model.User;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;

@DisplayName("Jdbc Test")
public class jdbcTest {

    @Test
    @DisplayName("CreateTable/create users/update user/create account/update account")
    public void Test() throws SQLException {
        DataSource dataSource = new DataSourceH2();
        SessionManager sessionManager = new SessionManagerJdbc(dataSource);

        DbUtils.createTable(dataSource, "create table user(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3));");
        DbExecutor<User> userDbExecutor = new DbExecutor<>(sessionManager, new ObjectMetaDataHolder(User.class));
        userDbExecutor.create(new User("Vasya", 22));
        userDbExecutor.create(new User("Petya", 13));
        DbUtils.selectAllRecords(dataSource, "user");
        userDbExecutor.update(new User(2, "Snake", 21));
        DbUtils.selectAllRecords(dataSource, "user");

        DbUtils.createTable(dataSource, "create table account(no bigint(20) NOT NULL auto_increment, type varchar(255), rest number);");
        DbExecutor<Account> accountDbExecutor = new DbExecutor<>(sessionManager, new ObjectMetaDataHolder(Account.class));
        accountDbExecutor.create(new Account("First", new BigDecimal(100)));
        accountDbExecutor.create(new Account("Second", new BigDecimal(200)));
        DbUtils.selectAllRecords(dataSource, "account");
        accountDbExecutor.update(new Account(2, "account_closed", new BigDecimal(0)));
        DbUtils.selectAllRecords(dataSource, "account");

        Account account = accountDbExecutor.load(2, Account.class);
        System.out.println("account: " + account);

        User user = userDbExecutor.load(2, User.class);
        System.out.println("user: " + user);
    }
}
