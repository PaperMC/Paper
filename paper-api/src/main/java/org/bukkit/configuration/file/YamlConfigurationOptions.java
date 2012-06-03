package org.bukkit.configuration.file;

import org.apache.commons.lang.Validate;

/**
 * Various settings for controlling the input and output of a {@link YamlConfiguration}
 */
public class YamlConfigurationOptions extends FileConfigurationOptions {
    private int indent = 2;

    protected YamlConfigurationOptions(YamlConfiguration configuration) {
        super(configuration);
    }

    @Override
    public YamlConfiguration configuration() {
        return (YamlConfiguration) super.configuration();
    }

    @Override
    public YamlConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions header(String value) {
        super.header(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions copyHeader(boolean value) {
        super.copyHeader(value);
        return this;
    }

    /**
     * Gets how much spaces should be used to indent each line.
     * <p />
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @return How much to indent by
     */
    public int indent() {
        return indent;
    }

    /**
     * Sets how much spaces should be used to indent each line.
     * <p />
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @param value New indent
     * @return This object, for chaining
     */
    public YamlConfigurationOptions indent(int value) {
        Validate.isTrue(value >= 2, "Indent must be at least 2 characters");
        Validate.isTrue(value <= 9, "Indent cannot be greater than 9 characters");

        this.indent = value;
        return this;
    }
}
