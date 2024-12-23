package org.bukkit.configuration.file;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Various settings for controlling the input and output of a {@link
 * FileConfiguration}
 *
 * @since 1.0.0
 */
public class FileConfigurationOptions extends MemoryConfigurationOptions {
    private List<String> header = Collections.emptyList();
    private List<String> footer = Collections.emptyList();
    // Paper start - add system prop for comment parsing
    private static final boolean PAPER_PARSE_COMMENTS_BY_DEFAULT = Boolean.parseBoolean(System.getProperty("Paper.parseYamlCommentsByDefault", "true"));
    private boolean parseComments = PAPER_PARSE_COMMENTS_BY_DEFAULT;
    // Paper end

    protected FileConfigurationOptions(@NotNull MemoryConfiguration configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public FileConfiguration configuration() {
        return (FileConfiguration) super.configuration();
    }

    @NotNull
    @Override
    public FileConfigurationOptions copyDefaults(boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @NotNull
    @Override
    public FileConfigurationOptions pathSeparator(char value) {
        super.pathSeparator(value);
        return this;
    }

    /**
     * Gets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link FileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @return Unmodifiable header, every entry represents one line.
     * @since 1.18.1
     */
    @NotNull
    public List<String> getHeader() {
        return header;
    }

    /**
     * @return The string header.
     *
     * @deprecated use getHeader() instead.
     */
    @NotNull
    @Deprecated(since = "1.18.1")
    public String header() {
        StringBuilder stringHeader = new StringBuilder();
        for (String line : header) {
            stringHeader.append(line == null ? "\n" : line + "\n");
        }
        return stringHeader.toString();
    }

    /**
     * Sets the header that will be applied to the top of the saved output.
     * <p>
     * This header will be commented out and applied directly at the top of
     * the generated output of the {@link FileConfiguration}. It is not
     * required to include a newline at the end of the header as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param value New header, every entry represents one line.
     * @return This object, for chaining
     * @since 1.18.1
     */
    @NotNull
    public FileConfigurationOptions setHeader(@Nullable List<String> value) {
        this.header = (value == null) ? Collections.emptyList() : Collections.unmodifiableList(value);
        return this;
    }

    /**
     * @param value The string header.
     * @return This object, for chaining.
     *
     * @deprecated use setHeader() instead
     */
    @NotNull
    @Deprecated(since = "1.18.1")
    public FileConfigurationOptions header(@Nullable String value) {
        this.header = (value == null) ? Collections.emptyList() : Collections.unmodifiableList(Arrays.asList(value.split("\\n")));
        return this;
    }

    /**
     * Gets the footer that will be applied to the bottom of the saved output.
     * <p>
     * This footer will be commented out and applied directly at the bottom of
     * the generated output of the {@link FileConfiguration}. It is not required
     * to include a newline at the beginning of the footer as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @return Unmodifiable footer, every entry represents one line.
     * @since 1.18.1
     */
    @NotNull
    public List<String> getFooter() {
        return footer;
    }

    /**
     * Sets the footer that will be applied to the bottom of the saved output.
     * <p>
     * This footer will be commented out and applied directly at the bottom of
     * the generated output of the {@link FileConfiguration}. It is not required
     * to include a newline at the beginning of the footer as it will
     * automatically be applied, but you may include one if you wish for extra
     * spacing.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param value New footer, every entry represents one line.
     * @return This object, for chaining
     * @since 1.18.1
     */
    @NotNull
    public FileConfigurationOptions setFooter(@Nullable List<String> value) {
        this.footer = (value == null) ? Collections.emptyList() : Collections.unmodifiableList(value);
        return this;
    }

    /**
     * Gets whether or not comments should be loaded and saved.
     * <p>
     * Defaults to true.
     *
     * @return Whether or not comments are parsed.
     * @since 1.18.1
     */
    public boolean parseComments() {
        return parseComments;
    }

    /**
     * Sets whether or not comments should be loaded and saved.
     * <p>
     * Defaults to true.
     *
     * @param value Whether or not comments are parsed.
     * @return This object, for chaining
     * @since 1.18.1
     */
    @NotNull
    public MemoryConfigurationOptions parseComments(boolean value) {
        parseComments = value;
        return this;
    }

    /**
     * @return Whether or not comments are parsed.
     *
     * @deprecated Call {@link #parseComments()} instead.
     */
    @Deprecated(since = "1.18.1")
    public boolean copyHeader() {
        return parseComments;
    }

    /**
     * @param value Should comments be parsed.
     * @return This object, for chaining
     *
     * @deprecated Call {@link #parseComments(boolean)} instead.
     */
    @NotNull
    @Deprecated(since = "1.18.1")
    public FileConfigurationOptions copyHeader(boolean value) {
        parseComments = value;
        return this;
    }
}
