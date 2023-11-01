package step.learning.dto.entities;

import com.google.gson.JsonParser;
import step.learning.dao.AuthTokenDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;

public class AuthToken {
    private String jti;     // JWT ID instead of regular id
    private String sub;     // user-id
    private Date iat;       // created-moment
    private Date exp;       // expires-moment
    private String nik;     // getter only ~ navigation

    public AuthToken(){

    }

    public AuthToken(ResultSet resultSet) throws SQLException {
        this.setJti(resultSet.getString("jti"));
        this.setSub(resultSet.getString("sub"));
        this.setIat(new Date(resultSet.getTimestamp("iat").getTime()));
        this.setExp(new Date(resultSet.getTimestamp("exp").getTime()));
        try {
            this.nik = resultSet.getString("nik");
        }
        catch (Exception ignored){}
    }

    public String getNik() {
        return nik;
    }

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Date getIat() {
        return iat;
    }

    public void setIat(Date iat) {
        this.iat = iat;
    }
}
