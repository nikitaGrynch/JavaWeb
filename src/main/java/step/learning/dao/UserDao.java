package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import step.learning.dto.entities.User;
import step.learning.dto.models.SignupFormModel;
import step.learning.services.db.DbProvider;
import step.learning.services.hash.HashService;
import step.learning.services.kdf.KdfService;
import step.learning.services.random.RandomService;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;
    private final RandomService randomService;
    private final KdfService kdfService;

    @Inject
    public UserDao(DbProvider dbProvider,
                   @Named("db-prefix") String dbPrefix,
                   Logger logger,
                   RandomService randomService,
                   KdfService kdfService) {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
        this.randomService = randomService;
        this.kdfService = kdfService;
    }

    public User getUserByCredentials(String login, String password){
        if(login == null || password == null){
            return null;
        }

        String sql = "SELECT u.* FROM " + dbPrefix + "users u WHERE u. `login` = ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)){
            prep.setString(1, login);
            ResultSet resultSet = prep.executeQuery();
            if(resultSet.next()){
                User user = new User(resultSet);
                String passDk = kdfService.getDerivedKey( password, user.getSalt());
                if(passDk.equals(user.getPassDk())){
                    return user;
                }
            }
        }
          catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
        }
        return null;
    }

    public boolean addFromForm(SignupFormModel model ){
        String sql = "INSERT INTO " + dbPrefix + "users (" +
                "`login`, `real_name`, `email`, `salt`, `pass_dk`, `birthday`, `avatar_url`)" +
                " VALUES(?,?,?,?,?,?,?)";
        String salt = randomService.randomHex(16);
        String passDk = kdfService.getDerivedKey( model.getPassword(), salt);
        try(PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)){
            prep.setString(1, model.getLogin());
            prep.setString(2, model.getName());
            prep.setString(3, model.getEmail());
            prep.setString(4, salt);
            prep.setString(5, passDk);
            java.util.Date date = model.getBirthdate();
            prep.setDate(6, date == null ? null : new java.sql.Date(date.getTime()));
            prep.setString(7, model.getAvatar());
            prep.executeUpdate();
            return true;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
        }
            return false;
    }

    public boolean install() {
        String sql = "CREATE TABLE " + dbPrefix + "users (" +
                "`id`               BIGINT UNSIGNED PRIMARY KEY     DEFAULT (UUID_SHORT()), " +
                "`login`            VARCHAR(64) NOT NULL UNIQUE, " +
                "`real_name`        VARCHAR(64) NOT NULL, " +
                "`email`            VARCHAR(96) NOT NULL, " +
                "`salt`             CHAR(16) NOT NULL, " +
                "`pass_dk`          CHAR(32) NOT NULL COMMENT 'RFC 2898 DK - derived key', " +
                "`birthday`         DATE NULL, " +
                "`avatar_url`       VARCHAR(96) NULL, " +
                "`register_moment`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                "`delete_moment`    DATETIME NULL" +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_unicode_ci";
        try(Statement statement = dbProvider.getConnection().createStatement()) {
            statement.executeUpdate(sql);
            return true;
        }
        catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
            return false;
        }
    }
}
