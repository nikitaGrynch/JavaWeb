package step.learning.services.db;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

@Singleton
public class PlanetDbProvider implements DbProvider{
    private Connection connection;
    @Override
    public Connection getConnection() {
        if(connection == null){
            JsonObject dbConfig;
            try(Reader reader = new InputStreamReader(
                    Objects.requireNonNull(this.getClass().getClassLoader()
                            .getResourceAsStream("db_config.json")))
            ) {
                dbConfig = JsonParser.parseReader(reader).getAsJsonObject();
            }
            catch(IOException ex){
                throw new RuntimeException(ex);
            }
            catch (NullPointerException ex){
                throw new RuntimeException("Resource not found");
            }

            try{
                JsonObject planetConfig = dbConfig
                        .get("DataProviders")
                                .getAsJsonObject()
                                        .get("PlanetScale")
                                                .getAsJsonObject();
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver() );
                connection = DriverManager.getConnection(
                        planetConfig.get("url").getAsString(),
                        planetConfig.get("user").getAsString(),
                        planetConfig.get("password").getAsString()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}
