package step.learning.services.culture;

public interface ResourceProvider {
    String getString(String name, String culture);
    String getString(String name);

    void setCulture(String culture);
    String getCulture();
}
