package step.learning.dao;

import com.google.inject.Inject;
import step.learning.services.db.DbProvider;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoBase {
    private final Logger logger;
    private final DbProvider dbProvider;

    @Inject
    public DaoBase(Logger logger, DbProvider dbProvider) {
        this.logger = logger;
        this.dbProvider = dbProvider;
    }

    protected String getDbIdentity() {
        try(Statement statement = dbProvider.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT UUID_SHORT()");
            resultSet.next();
            return resultSet.getString(1);
        }
        catch (Exception ex){
            logger.log(Level.WARNING, ex.getMessage());
        }
        return  null;
    }

    protected Timestamp getDbTimestamp() {
        try(Statement statement = dbProvider.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT CURRENT_TIMESTAMP");
            resultSet.next();
            return resultSet.getTimestamp(1);
        }
        catch (Exception ex){
            logger.log(Level.WARNING, ex.getMessage());
        }
        return  null;
    }
}
