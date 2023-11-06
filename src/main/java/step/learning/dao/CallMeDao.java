package step.learning.dao;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.mysql.cj.protocol.x.StatementExecuteOkFactory;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.DbProvider;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CallMeDao {
    private final DbProvider dbProvider;
    private final String dbPrefix;
    private final Logger logger;

    @Inject
    public CallMeDao(DbProvider dbProvider, @Named("db-prefix") String dbPrefix, Logger logger) {
        this.dbProvider = dbProvider;
        this.dbPrefix = dbPrefix;
        this.logger = logger;
    }

    public void install() throws SQLException {
        String sql = "CREATE TABLE " + dbPrefix + "call_me (" +
                "id         BIGINT UNSIGNED     PRIMARY KEY DEFAULT (UUID_SHORT()), " +
                "name       VARCHAR(64)         NULL, " +
                "phone      CHAR(13)            NOT NULL COMMENT '+380XXXXXXXXX', " +
                "moment     DATETIME            DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE = InnoDB DEFAULT CHARSET = UTF8";
        try (Statement statement = dbProvider.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new SQLException(ex);
        }
    }

    public void add(CallMe item) throws IllegalArgumentException {
        String sql = "INSERT INTO " + dbPrefix + "call_me (`id`, `name`, `phone`, `moment`, `call_moment`) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, item.getId() != null ? item.getId() : "UUID_SHORT()");
            prep.setString(2, item.getName());
            prep.setString(3, item.getPhone());
            if(item.getMoment() != null){
                prep.setDate(4, new java.sql.Date(item.getMoment().getTime()));
            }
            else{
                prep.setTimestamp(4, new Timestamp((new Date()).getTime()));
            }
            prep.setDate(5, item.getCallMoment() != null ? new java.sql.Date(item.getCallMoment().getTime()) : null);
            prep.execute();
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
            throw new IllegalArgumentException(ex);
        }
    }

    public List<CallMe> getAll(){
        return getAll(false);
    }

    public List<CallMe> getAllWithDeleted(){
        return getAll(true);
    }

    public List<CallMe> getAll(boolean includeDeleted) {
        List<CallMe> result = new ArrayList<>();
        String sql = "SELECT * FROM " + dbPrefix + "call_me";
        if(!includeDeleted){
            sql += " WHERE delete_moment IS NULL";
        }
        try (Statement statement = dbProvider.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(new CallMe(resultSet));
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
        }

        return result;
    }

    public CallMe getById(String id){
        return getById(id, true);
    }

    public CallMe getById(String id, boolean includeDeleted) {
        CallMe res = null;
        String sql = "SELECT * FROM " + dbPrefix + "call_me WHERE id = ?";
        if(!includeDeleted) {
            sql += " AND delete_moment IS NULL";
        }
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, id);
            ResultSet resultSet = prep.executeQuery();
            if (resultSet.next()) {
                return new CallMe(resultSet);
            }
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
        }
        return null;
    }

    public boolean restoreById(String id){
        String sql = "UPDATE " + dbPrefix + "call_me SET delete_moment = NULL WHERE id = ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, id);
            prep.execute();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
        }
        return false;
    }

    public boolean setCallMoment(CallMe item) {
        String sql = "SELECT CURRENT_TIMESTAMP";
        Timestamp timestamp;
        try(Statement statement = dbProvider.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            timestamp = resultSet.getTimestamp(1);
            item.setCallMoment(
                    new Date(timestamp.getTime())
            );
        }
        catch (Exception ex){
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
            return false;
        }

        sql = "UPDATE " + dbPrefix + "call_me SET call_moment = ? WHERE id = ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setTimestamp(1, timestamp);
            prep.setString(2, item.getId());
            prep.execute();
            return true;
        } catch (SQLException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
        }
        return false;
    }

    public boolean delete(CallMe item){
        return delete(item, false);
    }

    public boolean delete(CallMe item, boolean hardDelete) {
        if(item == null || item.getId() == null){
            return false;
        }
        String sql = hardDelete
                ? "DELETE FROM " + dbPrefix + "call_me WHERE id = ?"
                : "UPDATE " + dbPrefix + "call_me SET delete_moment = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement prep = dbProvider.getConnection().prepareStatement(sql)) {
            prep.setString(1, item.getId());
            prep.executeUpdate();
            return true;
        } catch (Exception ex) {
            logger.log(Level.WARNING, ex.getMessage() + " -- " + sql);
            return false;
        }
    }
}
