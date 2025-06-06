package io.papermc.paper.dialog;

public interface InputData {
    String getString(String key);
    Float getNumber(String key);
    Boolean getBoolean(String key);

    InputData put(String key, String value);
    InputData put(String key, float value);
    InputData put(String key, boolean value);
}
