/*
 * Copied from GeyserMC's Configurate fork:
 * https://github.com/GeyserMC/Configurate/blob/f6aa72e6099f956e78b638a2a817429007c6898a/format/yaml/src/main/java/org/spongepowered/configurate/yaml/YamlConstructor.java
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

import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

class YamlConstructor extends Constructor {

    static final char BLANK_LINE_COMMENT_INDICATOR = '\0';
    private static final Pattern LINE_BREAK_PATTERN = Pattern.compile("\\R");

    private final ConfigurationOptions options;
    private final BlankLineStyle blankLineStyle;

    YamlConstructor(final LoaderOptions loadingConfig, final ConfigurationOptions options, final BlankLineStyle blankLineStyle) {
        super(loadingConfig);
        this.options = options;
        this.blankLineStyle = blankLineStyle;
    }

    @Override
    @EnsuresNonNull("options")
    public Object getSingleData(final Class<?> type) {
        return super.getSingleData(type);
    }

    @Override
    protected Object constructObjectNoCheck(final Node yamlNode) {
        final CommentedConfigurationNode node = CommentedConfigurationNode.root(this.options);

        // alright, let's first check some interesting behaviour.
        // When you have a file with only empty lines (has to be at least two),
        // a Tag of the type COMMENT will be executed.
        // It'll have a blockComment of the type empty line.
        // This is unlike the behaviour of an empty file and a file with only comments,
        // because both won't be seen as an object and will instead return null by Yaml#load.
        // Let's filter out the multi-empty-lines behaviour and return an empty node instead.
        if (yamlNode.getTag() == Tag.COMMENT) {
            return node;
        }

        if (yamlNode.getNodeId() == NodeId.mapping) {
            // make sure to mark it as a map type, even if the map itself is empty
            node.raw(Collections.emptyMap());
            final MappingNode mapping = (MappingNode) yamlNode;
            if (mapping.getFlowStyle() != null) {
                node.hint(YamlConfigurationLoader.NODE_STYLE, NodeStyle.fromSnakeYaml(mapping.getFlowStyle()));
            }

            for (final NodeTuple tuple : mapping.getValue()) {
                final ConfigurationNode keyNode = (ConfigurationNode) this.constructObject(tuple.getKeyNode());
                final Node valueNode = tuple.getValueNode();

                // comments are on the key, not the value
                node.node(keyNode.raw())
                    .from((ConfigurationNode) constructObject(valueNode))
                    .comment(commentFor(tuple.getKeyNode().getBlockComments()));
            }

            return node.comment(commentFor(yamlNode.getBlockComments()));
        }

        final Object raw = super.constructObjectNoCheck(yamlNode);
        if (raw instanceof Collection<?>) {
            // make sure to mark it as a list type, even if the collection itself is empty
            node.raw(Collections.emptyList());
            if (((SequenceNode) yamlNode).getFlowStyle() != null) {
                node.hint(YamlConfigurationLoader.NODE_STYLE, NodeStyle.fromSnakeYaml(((SequenceNode) yamlNode).getFlowStyle()));
            }

            for (final Object value : (Collection<?>) raw) {
                node.appendListNode().from((ConfigurationNode) value);
            }
        } else {
            if (yamlNode instanceof ScalarNode) {
                node.hint(YamlConfigurationLoader.SCALAR_STYLE, ScalarStyle.fromSnakeYaml(((ScalarNode) yamlNode).getScalarStyle()));
            }
            node.raw(raw);
        }

        return node.comment(commentFor(yamlNode.getBlockComments()));
    }

    private @Nullable String commentFor(final @Nullable List<CommentLine> commentLines) {
        if (commentLines == null || commentLines.isEmpty()) {
            return null;
        }

        final StringBuilder outputBuilder = new StringBuilder();
        boolean first = true;

        for (final CommentLine line : commentLines) {
            final boolean blank = line.getCommentType() == CommentType.BLANK_LINE;
            // Blank lines are considered formatting, and thus will not result in a comment.
            // Users can instead configure blank lines using the BLANK_LINE_STYLE_OVERRIDE.
            // Unless it's keep, as explained by its Javadoc.
            if (blank && this.blankLineStyle != BlankLineStyle.KEEP) {
                continue;
            }
            if (!first) {
                outputBuilder.append(AbstractConfigurationLoader.CONFIGURATE_LINE_SEPARATOR);
            }
            first = false;
            if (blank) {
                outputBuilder.append(BLANK_LINE_COMMENT_INDICATOR);
                continue;
            }

            final String lineStripped = removeLineBreaksForLine(line.getValue());
            if (!lineStripped.isEmpty() && lineStripped.charAt(0) == ' ') {
                outputBuilder.append(lineStripped, 1, lineStripped.length());
            } else {
                outputBuilder.append(lineStripped);
            }
        }

        return !first ? outputBuilder.toString() : null;
    }

    private static String removeLineBreaksForLine(final String line) {
        return LINE_BREAK_PATTERN.matcher(line).replaceAll("");
    }

}
