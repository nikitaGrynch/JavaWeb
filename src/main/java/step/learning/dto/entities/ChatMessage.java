package step.learning.dto.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ChatMessage {
    private String id;
    private String senderId;
    private String message;
    private Date moment;

    public ChatMessage(ResultSet resultSet) throws SQLException {
        setId(resultSet.getString("id"));
        setSenderId(senderId = resultSet.getString("sender_id"));
        setMessage(message = resultSet.getString("message"));
        setMoment(moment = new Date(resultSet.getTimestamp("moment").getTime()));
    }

    public ChatMessage(String senderId, String message){
        setSenderId(senderId);
        setMessage(message);
    }

    public ChatMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }
}
