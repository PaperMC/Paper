/*
 * Copied from GeyserMC's Configurate fork:
 * https://github.com/GeyserMC/Configurate/blob/f6aa72e6099f956e78b638a2a817429007c6898a/format/yaml/src/main/java/org/spongepowered/configurate/yaml/YamlConfigurationLoader.java
 *
 * Configurate
 * Copyright (C) zml and Configurate contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.papermc.paper.configuration.yaml;

import net.kyori.option.Option;
import net.kyori.option.OptionSchema;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.RepresentationHint;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.CommentHandler;
import org.spongepowered.configurate.loader.CommentHandlers;
import org.spongepowered.configurate.util.UnmodifiableCollections;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

/**
 * A loader for YAML-formatted configurations, using the SnakeYAML library for
 * parsing and generation.
 *
 * @since 4.0.0
 */
public final class YamlConfigurationLoader extends AbstractConfigurationLoader<CommentedConfigurationNode> {

    /**
     * YAML native types from <a href="https://yaml.org/type/">YAML 1.1 Global tags</a>.
     *
     * <p>using SnakeYaml representation: https://bitbucket.org/snakeyaml/snakeyaml/wiki/Documentation#markdown-header-yaml-tags-and-java-types
     */
    private static final Set<Class<?>> NATIVE_TYPES = UnmodifiableCollections.toSet(
            Boolean.class, Integer.class, Long.class, BigInteger.class, Double.class, // numeric
            byte[].class, String.class, Date.class, java.sql.Date.class, Timestamp.class); // complex types

    /**
     * The YAML scalar style this node should attempt to use.
     *
     * <p>If the chosen scalar style would produce syntactically invalid YAML, a
     * valid one will replace it.</p>
     *
     * @since 4.3.0
     */
    public static final RepresentationHint<ScalarStyle> SCALAR_STYLE = RepresentationHint.of("configurate:yaml/scalar_style", ScalarStyle.class);

    /**
     * The YAML node style to use for collection nodes. A {@code null} value
     * will instruct the emitter to fall back to the
     * {@link Builder#nodeStyle()} setting.
     *
     * @since 4.3.0
     */
    public static final RepresentationHint<NodeStyle> NODE_STYLE = RepresentationHint.of("configurate:yaml/node_style", NodeStyle.class);

    /**
     * The amount of blank lines that a specific node should have.
     * Absent is to inherit the {@link BlankLineStyle} of the document.
     *
     * @since 4.3.0
     */
    public static final RepresentationHint<Integer> BLANK_LINE_STYLE_OVERRIDE
        = RepresentationHint.of("configurate:yaml/blank_line_style_override", Integer.class);

    /**
     * Creates a new {@link YamlConfigurationLoader} builder.
     *
     * @return a new builder
     * @since 4.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds a {@link YamlConfigurationLoader}.
     *
     * <p>This builder supports the following options:</p>
     * <dl>
     *     <dt>&lt;prefix&gt;.yaml.node-style</dt>
     *     <dd>Equivalent to {@link #nodeStyle(NodeStyle)}</dd>
     *     <dt>&lt;prefix&gt;.yaml.comments-enabled</dt>
     *     <dd>Equivalent to {@link #commentsEnabled(boolean)}</dd>
     *     <dt>&lt;prefix&gt;.yaml.line-length</dt>
     *     <dd>Equivalent to {@link #lineLength(int)}</dd>
     * </dl>
     *
     * @since 4.0.0
     */
    public static final class Builder extends AbstractConfigurationLoader.Builder<Builder, YamlConfigurationLoader> {

        private static final OptionSchema.Mutable UNSAFE_SCHEMA = OptionSchema.childSchema(AbstractConfigurationLoader.Builder.SCHEMA);

        /**
         * A schema of options available to configure the YAML loader.
         *
         * @since 4.2.0
         */
        public static final OptionSchema SCHEMA = UNSAFE_SCHEMA.frozenView();

        /**
         * The collection node style to use globally when emitting with
         * this loader.
         *
         * @see #nodeStyle(NodeStyle)
         * @since 4.2.0
         */
        public static final Option<NodeStyle> NODE_STYLE = UNSAFE_SCHEMA.enumOption("yaml:node_style", NodeStyle.class, null);

        /**
         * The indent size (in spaces) to use for documents emitted by
         * the created loader.
         *
         * @see #indent(int)
         * @since 4.2.0
         */
        public static final Option<Integer> INDENT = UNSAFE_SCHEMA.intOption("yaml:indent", 4);

        /**
         * Whether comments are being read and written.
         *
         * @see #commentsEnabled(boolean)
         * @since 4.3.0
         */
        public static final Option<Boolean> COMMENTS_ENABLED = UNSAFE_SCHEMA.booleanOption("yaml:comments_enabled", true);

        /**
         * The line length before the scalar representation is split into multiple lines.
         *
         * @see #lineLength(int)
         * @since 4.3.0
         */
        public static final Option<Integer> LINE_LENGTH = UNSAFE_SCHEMA.intOption("yaml:line_length", 150);

        /**
         * The blank line style for writing YAML documents.
         *
         * @see #blankLineStyle(BlankLineStyle)
         * @since 4.3.0
         */
        public static final Option<BlankLineStyle> BLANK_LINE_STYLE = UNSAFE_SCHEMA.enumOption("yaml:blank_line_style", BlankLineStyle.class, null);

        private final DumperOptions options = new DumperOptions();

        Builder() {
            this.defaultOptions(o -> o.nativeTypes(NATIVE_TYPES));
        }

        @Override
        protected OptionSchema optionSchema() {
            return SCHEMA;
        }

        /**
         * Sets the level of indentation the resultant loader should use.
         *
         * @param indent the indent level
         * @return this builder (for chaining)
         * @since 4.0.0
         */
        public Builder indent(final int indent) {
            this.optionStateBuilder().value(INDENT, indent);
            return this;
        }

        /**
         * Gets the level of indentation to be used by the resultant loader.
         *
         * @return the indent level
         * @since 4.0.0
         */
        public int indent() {
            return this.optionState().value(INDENT);
        }

        /**
         * Sets the node style the built loader should use.
         *
         * <dl><dt>Flow</dt>
         * <dd>the compact, json-like representation.<br>
         * Example: <code>
         *     {value: [list, of, elements], another: value}
         * </code></dd>
         *
         * <dt>Block</dt>
         * <dd>expanded, traditional YAML<br>
         * Example: <code>
         *     value:
         *     - list
         *     - of
         *     - elements
         *     another: value
         * </code></dd>
         * </dl>
         *
         * <p>A {@code null} value will tell the loader to pick a value
         * automatically based on the contents of each non-scalar node.</p>
         *
         * @param style the node style to use
         * @return this builder (for chaining)
         * @since 4.0.0
         */
        public Builder nodeStyle(final @Nullable NodeStyle style) {
            this.optionStateBuilder().value(NODE_STYLE, style);
            return this;
        }

        /**
         * Gets the node style to be used by the resultant loader.
         *
         * @return the node style
         * @since 4.0.0
         */
        public @Nullable NodeStyle nodeStyle() {
            return this.optionState().value(NODE_STYLE);
        }

        /**
         * Sets the blank line style handling for all written files.
         *
         * @param style the style to apply
         * @return this builder
         * @see BlankLineStyle
         * @since 4.3.0
         */
        public Builder blankLineStyle(final @Nullable BlankLineStyle style) {
            this.optionStateBuilder().value(BLANK_LINE_STYLE, style);
            return this;
        }

        /**
         * Gets the explicitly applied current blank line styling.
         * This doesn't return the implicit fallback used when not setting it explicitly.
         *
         * @return the blank line style
         * @since 4.3.0
         */
        public @Nullable BlankLineStyle blankLineStyle() {
            return this.optionState().value(BLANK_LINE_STYLE);
        }

        /**
         * Set whether comment handling is enabled on this loader.
         *
         * <p>When comment handling is enabled, comments will be read from files
         * and written back to files where possible.</p>
         *
         * <p>The default value is {@code true}</p>
         *
         * @param enableComments whether comment handling should be enabled
         * @return this builder (for chaining)
         * @since 4.3.0
         */
        public Builder commentsEnabled(final boolean enableComments) {
            this.optionStateBuilder().value(COMMENTS_ENABLED, enableComments);
            return this;
        }

        /**
         * Get whether comment handling is enabled.
         *
         * @return whether comment handling is enabled
         * @see #commentsEnabled(boolean) for details on comment handling
         * @since 4.3.0
         */
        public boolean commentsEnabled() {
            return this.optionState().value(COMMENTS_ENABLED);
        }

        /**
         * Set the maximum length of a configuration line.
         *
         * <p>The default value is {@code 150}</p>
         *
         * @param lineLength the maximum length of a configuration line
         * @return this builder (for chaining)
         * @since 4.3.0
         */
        public Builder lineLength(final int lineLength) {
            this.optionStateBuilder().value(LINE_LENGTH, lineLength);
            return this;
        }

        /**
         * Get the maximum length of a configuration line.
         *
         * @return the maximum length of a configuration line
         * @see #lineLength(int) for details on the line length
         * @since 4.3.0
         */
        public int lineLength() {
            return this.optionState().value(LINE_LENGTH);
        }

        @Override
        public YamlConfigurationLoader build() {
            return new YamlConfigurationLoader(this);
        }
    }

    private final LoaderOptions loaderOpts;
    private final DumperOptions dumperOpts;
    private final BlankLineStyle blankLineStyle;

    private YamlConfigurationLoader(final Builder builder) {
        super(builder, new CommentHandler[] {CommentHandlers.HASH});
        this.loaderOpts = new LoaderOptions()
                // .setAcceptTabs(true) - requires Configurate's patched snakeyaml
                .setProcessComments(builder.commentsEnabled());
        this.loaderOpts.setCodePointLimit(Integer.MAX_VALUE);

        this.dumperOpts = builder.options;
        this.dumperOpts.setDefaultFlowStyle(NodeStyle.asSnakeYaml(builder.optionState().value(Builder.NODE_STYLE)));
        this.dumperOpts.setIndent(builder.optionState().value(Builder.INDENT));
        this.dumperOpts.setProcessComments(builder.optionState().value(Builder.COMMENTS_ENABLED));
        this.dumperOpts.setWidth(builder.optionState().value(Builder.LINE_LENGTH));
        this.dumperOpts.setIndicatorIndent(builder.optionState().value(Builder.INDENT));
        this.dumperOpts.setIndentWithIndicator(true);

        final @Nullable BlankLineStyle configuredBlankLineStyle = builder.optionState().value(Builder.BLANK_LINE_STYLE);
        this.blankLineStyle = configuredBlankLineStyle != null ? configuredBlankLineStyle : BlankLineStyle.NONE;
    }

    @Override
    protected void loadInternal(final CommentedConfigurationNode node, final BufferedReader reader) {
        final Constructor constructor = new YamlConstructor(this.loaderOpts, node.options(), this.blankLineStyle);
        final Yaml yaml = new Yaml(constructor, new YamlRepresenter(this.blankLineStyle, true, this.dumperOpts), this.dumperOpts, this.loaderOpts);

        @Nullable CommentedConfigurationNode loaded = yaml.load(reader);
        // when a file exists but is empty (or if the file only exists of comments), the first event will be StreamEnd.
        // getSingleNode will return null, getSingleData uses the Constructor of Tag Null, which just returns null.
        // So we have to map null to an empty root node.
        if (loaded == null) {
            loaded = CommentedConfigurationNode.root(node.options());
        }
        node.from(loaded);
    }

    @Override
    protected void saveInternal(final ConfigurationNode node, final Writer writer) {
        final Constructor constructor = new YamlConstructor(this.loaderOpts, node.options(), this.blankLineStyle);
        final Yaml yaml = new Yaml(constructor, new YamlRepresenter(this.blankLineStyle, true, this.dumperOpts), this.dumperOpts, this.loaderOpts);
        yaml.dump(node, writer);
    }

    @Override
    public CommentedConfigurationNode createNode(final ConfigurationOptions options) {
        return CommentedConfigurationNode.root(options);
    }

}
