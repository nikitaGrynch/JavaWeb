package step.learning.dto.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class User {
    private String id;
    private String login;
    private String realName;
    private String email;
    private String passDk;  // RFC 2898 DK - derived key
    private String salt;
    private Date birthday;
    private String avatarUrl;
    private Date registerMoment;
    private Date deleteMoment;

    public User (ResultSet resultSet) throws SQLException {
        this.setId(resultSet.getString("id"));
        this.setLogin(resultSet.getString("login"));
        this.setRealName(resultSet.getString("real_name"));
        this.setEmail(resultSet.getString("email"));
        this.setSalt(resultSet.getString("salt"));
        this.setPassDk(resultSet.getString("pass_dk"));
        this.setBirthday(resultSet.getDate("birthday"));
        this.setAvatarUrl(resultSet.getString("avatar_url"));

        Timestamp timestamp = resultSet.getTimestamp("register_moment");
        if (timestamp != null) {
            this.setRegisterMoment(new Date(timestamp.getTime()));
        }
        timestamp = resultSet.getTimestamp("delete_moment");
        if (timestamp != null) {
            this.setDeleteMoment(new Date(timestamp.getTime()));
        }
    }

    public User() {

    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassDk() {
        return passDk;
    }

    public void setPassDk(String passDk) {
        this.passDk = passDk;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Date getRegisterMoment() {
        return registerMoment;
    }

    public void setRegisterMoment(Date registerMoment) {
        this.registerMoment = registerMoment;
    }

    public Date getDeleteMoment() {
        return deleteMoment;
    }

    public void setDeleteMoment(Date deleteMoment) {
        this.deleteMoment = deleteMoment;
    }
}
