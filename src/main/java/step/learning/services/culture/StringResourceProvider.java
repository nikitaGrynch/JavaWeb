package step.learning.services.culture;

import com.google.inject.Singleton;

@Singleton
public class StringResourceProvider implements ResourceProvider{

    private String defaultCulture;
    @Override
    public String getString(String name, String culture) {
        switch (name){
            case "signup_login_too_short":
                switch(culture){
                    default: return "Login must be at least 2 characters long";
                }
            case "signup_login_empty":
                switch (culture){
                    default: return "Login cannot be empty";
                }
            case "signup_pattern_mismatch":
                switch (culture){
                    default: return "Login must contains only letters, numbers, '_', '-'";
                }
        }
        return null;
    }

    @Override
    public String getString(String name) {
        return getString(name, defaultCulture);
    }

    @Override
    public void setCulture(String culture) {
        defaultCulture = culture;
    }

    @Override
    public String getCulture() {
        return defaultCulture;
    }
}
