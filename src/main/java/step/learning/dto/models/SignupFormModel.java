package step.learning.dto.models;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.InvalidFileNameException;
import step.learning.services.formparse.FormParseResult;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class SignupFormModel {
    private static final SimpleDateFormat formDateFormat =
            new SimpleDateFormat("yyyy-MM-dd");

//    public SignupFormModel(HttpServletRequest req) throws ParseException {
//        this.setLogin( req.getParameter("reg-login") );
//        this.setName( req.getParameter("reg-name") );
//        this.setPassword( req.getParameter("reg-password") );
//        this.setRepeat( req.getParameter("reg-repeat") );
//        this.setEmail( req.getParameter("reg-email") );
//        this.setAgree( req.getParameter("reg-agree") );
//        this.setBirthdate( req.getParameter("reg-birthdate") );
//    }

    public SignupFormModel (FormParseResult formParseResult) throws ParseException {
        Map<String, String> fields = formParseResult.getFields();
        this.setLogin( fields.get("reg-login") );
        this.setName( fields.get("reg-name") );
        this.setPassword( fields.get("reg-password") );
        this.setRepeat( fields.get("reg-repeat") );
        this.setEmail( fields.get("reg-email") );
        this.setAgree( fields.get("reg-agree") );
        this.setBirthdate( fields.get("reg-birthdate") );

        this.setAvatar(formParseResult);
    }

    /**
     * Each field validation and formation errors notifications
     * Empty answer - validation successful
     * @return dictionary "field name" - "error notification"
     */
    public Map<String, String> getValidationErrorMessages(){
        String loginRegex = "^[a-zA-Z0-9_-]+$";
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        String nameRegex = "^[A-Za-z -]+$";
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$";

        Map<String, String> result = new HashMap<>();
        if(login == null || login.isEmpty()){
            result.put("login", "signup_login_empty");
        }
        else if(login.length() < 2) {
            result.put("login", "signup_login_too_short");
        }
        else if(!Pattern.matches("^[a-zA-Z0-9_-]+$", login)){
            result.put("login", "signup_pattern_mismatch");
        }


        if(email == null || email.isEmpty()){
            result.put("email", "Email cannot be empty");
        }
        else if(!Pattern.compile(emailRegex).matcher(email).matches()){
            result.put("email", "Invalid email");
        }
        if(name == null || name.isEmpty()){
            result.put("name", "Name cannot be empty");
        }
        else if(!Pattern.compile(nameRegex).matcher(name).matches()){
            result.put("name", "Invalid name");
        }
        if(birthdate != null){
            Date currentDate = new Date();
            Date eighteenYearsAgo = new Date(currentDate.getTime() - 18L * 365 * 24 * 60 * 60 * 1000);
            Date oneHundredYearsAgo = new Date(currentDate.getTime() - 100L * 365 * 24 * 60 * 60 * 1000);
            if(!(birthdate.before(eighteenYearsAgo) && birthdate.after(oneHundredYearsAgo))){
                result.put("birthdate", "Age must be from 18 to 100");
            }
        }
        if(password == null || password.isEmpty()){
            result.put("password", "Password cannot be empty");
        }
        else if(!Pattern.compile(passwordRegex).matcher(password).matches()){
            result.put("password", "Invalid password ( " +
                    "at least:" +
                    " 6 characters long, " +
                    "one lowercase letter, " +
                    "one uppercase letter, " +
                    "one digit )");
        }
        else if(!(repeat.equals(password))){
            result.put("repeat", "Passwords must be equal");
        }

        return result;
    }

    // region fields
    private String login;
    private String name;
    private String password;
    private String repeat;
    private String email;
    private Date birthdate;
    private Boolean isAgree;
    private String avatar;  // filename or URL
    // endregion

    // region accessors
    public void setAvatar(FormParseResult formParseResult){  // download and set name
        Map<String, FileItem> files = formParseResult.getFiles();
        if(!files.containsKey("reg-avatar")){
            this.avatar = null;
            return;
        }
        FileItem fileItem = files.get("reg-avatar");
        String uploadedFilename = fileItem.getName();
        int dotIndex = uploadedFilename.lastIndexOf('.');
        String ext = uploadedFilename.substring(dotIndex);
        String[] extensions = {".jpg", ".jpeg", ".png", ".ico", ".gif"}; // valid extensions
        if(!Arrays.asList(extensions).contains(ext)){
            // throw exception if extension is invalid
            throw new RuntimeException("Invalid file extension");
        }
        // determine file saving dir
        String uploadDir = formParseResult.getRequest()
                .getServletContext()  // context - servlet environment (actual location)
                .getRealPath("./upload/avatar/");

        // create random filename to save it
        String savedFilename;
        File savedFile;
        do{
            savedFilename = UUID.randomUUID().toString().substring(0, 8) + ext;
            savedFile = new File(uploadDir, savedFilename);
        } while(savedFile.exists());
        try{
            fileItem.write(savedFile);
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
        this.setAvatar(savedFilename);
    }
    public void setBirthdate(String birthdate) throws ParseException {
        if( birthdate == null || birthdate.isEmpty() ) {
            this.birthdate = null;
        }
        else{
            setBirthdate(formDateFormat.parse(birthdate));
        }
    }
    public void setAgree(String input) {
        this.setAgree(
                "on".equalsIgnoreCase(input) || "true".equalsIgnoreCase(input)
        );
    }
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getAgree() {
        return isAgree;
    }

    public void setAgree(Boolean agree) {
        isAgree = agree;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    // endregion
}
