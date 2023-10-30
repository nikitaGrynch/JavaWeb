package step.learning.dto.entities;

import com.google.gson.JsonObject;

import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Pattern;

public class CallMe {
    private String id;
    private String name;
    private String phone;
    private Date moment;
    private Date callMoment;
    private Date deleteMoment;



    public CallMe() {
    }
    public CallMe(JsonObject jsonObject) throws IllegalArgumentException {
        try{
            if(jsonObject.has("id")){
                setId(jsonObject.get("id").getAsString());
            }
            setName(jsonObject.get("name").getAsString());
            setPhone(jsonObject.get("phone").getAsString());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(jsonObject.has("moment")){
                setMoment(dateFormat.parse(jsonObject.get("moment").getAsString()));
            }
            if(jsonObject.has("call_moment")){
                setCallMoment(dateFormat.parse(jsonObject.get("call_moment").getAsString()));
            }
        }
        catch(IllegalArgumentException ex){
            throw ex;
        }
        catch (DateTimeParseException ex){
            throw new InvalidParameterException("Invalid date format");
        }
        catch (Exception ex){
            throw new IllegalArgumentException("JSON object must have non-null 'name' and 'phone' fields");
        }
    }

    public CallMe(ResultSet resultSet) throws IllegalArgumentException {
        try {
            setId(resultSet.getString("id"));
            setName(resultSet.getString("name"));
            setPhone(resultSet.getString("phone"));
            setMoment(new Date(resultSet.getTimestamp("moment").getTime()));
            Timestamp callMomentTimestamp = resultSet.getTimestamp("call_moment");
            if(callMomentTimestamp != null){
                setCallMoment(new Date(callMomentTimestamp.getTime()));
            }
            Timestamp delMomentTimestamp = resultSet.getTimestamp("delete_moment");
            if(delMomentTimestamp != null){
                setDeleteMoment(new Date(delMomentTimestamp.getTime()));
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    // region getters/setters


    public Date getDeleteMoment() {
        return deleteMoment;
    }

    public void setDeleteMoment(Date deleteMoment) {
        this.deleteMoment = deleteMoment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if(Pattern.matches("^\\+380([\\s-]?\\d){9}$", phone)){
            this.phone = phone.replaceAll("[\\s-]+", "");
        }
        else{
            throw new IllegalArgumentException("'phone' must match '+380XXXXXXXXX' pattern");
        }
    }

    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    public Date getCallMoment() {
        return callMoment;
    }

    public void setCallMoment(Date callMoment) {
        this.callMoment = callMoment;
    }
    // endregion
}
