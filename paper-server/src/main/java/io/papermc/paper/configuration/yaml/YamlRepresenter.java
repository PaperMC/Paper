/*
 * Copied from GeyserMC's Configurate fork:
 * https://github.com/GeyserMC/Configurate/blob/f6aa72e6099f956e78b638a2a817429007c6898a/format/yaml/src/main/java/org/spongepowered/configurate/yaml/YamlRepresenter.java
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

import static org.spongepowered.configurate.loader.AbstractConfigurationLoader.CONFIGURATE_LINE_PATTERN;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNodeIntermediary;
import org.spongepowered.configurate.ConfigurationNode;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class YamlRepresenter extends Representer {

    private static final CommentLine BLANK_LINE = new CommentLine(null, null, "", CommentType.BLANK_LINE);

    private final BlankLineStyle blankLineStyle;
    private final boolean padComments;

    YamlRepresenter(final BlankLineStyle blankLineStyle, final boolean padComments, final DumperOptions options) {
        super(options);
        this.blankLineStyle = blankLineStyle;
        this.padComments = padComments;
        multiRepresenters.put(ConfigurationNode.class, new ConfigurationNodeRepresent());
        nullRepresenter = new EmptyNullRepresenter();
    }

    private final class ConfigurationNodeRepresent implements Represent {
        @Override
        public Node representData(final Object nodeObject) {
            final ConfigurationNode node = (ConfigurationNode) nodeObject;

            final @Nullable Integer blankLineCount = node.ownHint(YamlConfigurationLoader.BLANK_LINE_STYLE_OVERRIDE);

            final Node yamlNode;
            if (node.isMap()) {
                boolean first = true;
                boolean previousNested = false;

                final List<NodeTuple> children = new ArrayList<>();
                for (Map.Entry<Object, ? extends ConfigurationNode> ent : node.childrenMap().entrySet()) {
                    final ConfigurationNode child = ent.getValue();
                    // SnakeYAML supports both key and value comments. Add the comments on the key
                    final Node value = represent(child);
                    final Node key = represent(ent.getKey());
                    key.setBlockComments(value.getBlockComments());
                    value.setBlockComments(Collections.emptyList());

                    final @Nullable Integer childBlankLineCount = child.ownHint(YamlConfigurationLoader.BLANK_LINE_STYLE_OVERRIDE);
                    if (childBlankLineCount == null && !first && separateBefore(node, child, previousNested)) {
                        addBlockCommentBlankLine(key, 1);
                    }
                    first = false;
                    previousNested = isNested(child);

                    children.add(new NodeTuple(key, value));
                }
                yamlNode = new MappingNode(Tag.MAP, children, this.flowStyle(node));
            } else if (node.isList()) {
                final List<Node> children = new ArrayList<>();
                for (ConfigurationNode ent : node.childrenList()) {
                    children.add(represent(ent));
                }
                yamlNode = new SequenceNode(Tag.SEQ, children, this.flowStyle(node));
            } else {
                final Node optionNode = represent(node.rawScalar());
                final io.papermc.paper.configuration.yaml.@Nullable ScalarStyle requestedStyle
                    = node.ownHint(YamlConfigurationLoader.SCALAR_STYLE);
                if (optionNode instanceof ScalarNode && requestedStyle != null) {
                    final ScalarNode scalar = (ScalarNode) optionNode;
                    yamlNode = new ScalarNode(
                        scalar.getTag(),
                        scalar.getValue(),
                        scalar.getStartMark(),
                        scalar.getEndMark(),
                        io.papermc.paper.configuration.yaml.ScalarStyle.asSnakeYaml(requestedStyle, scalar.getScalarStyle())
                    );
                } else {
                    yamlNode = optionNode;
                }

            }

            if (node instanceof CommentedConfigurationNodeIntermediary<?>) {
                final @Nullable String nodeComment = ((CommentedConfigurationNodeIntermediary<?>) node).comment();
                if (nodeComment != null) {
                    yamlNode.setBlockComments(
                        Arrays.stream(CONFIGURATE_LINE_PATTERN.split(nodeComment, -1))
                            .map(this::commentLineFor)
                            .collect(Collectors.toList())
                    );
                }
            }

            if (blankLineCount != null && blankLineCount > 0) {
                addBlockCommentBlankLine(yamlNode, blankLineCount);
            }

            return yamlNode;
        }

        private FlowStyle flowStyle(final ConfigurationNode node) {
            final @Nullable NodeStyle requested = node.ownHint(YamlConfigurationLoader.NODE_STYLE);
            return NodeStyle.asSnakeYaml(requested);
        }

        private boolean separateBefore(final ConfigurationNode parent, final ConfigurationNode child, final boolean previousNested) {
            switch (YamlRepresenter.this.blankLineStyle) {
                case ROOT_CHILDREN:
                    return parent.parent() == null;
                case AFTER_NESTED:
                    return previousNested;
                case BEFORE_COMMENT:
                    return child instanceof CommentedConfigurationNodeIntermediary<?>
                        && ((CommentedConfigurationNodeIntermediary<?>) child).comment() != null
                        && !(parent.parent() != null && parent.parent().isList());
                default:
                    return false;
            }
        }

        private boolean isNested(final ConfigurationNode node) {
            return (node.isMap() || node.isList()) && !node.empty();
        }

        private void addBlockCommentBlankLine(final Node node, final int count) {
            @Nullable List<CommentLine> comments = node.getBlockComments();

            if (comments == null) {
                comments = new ArrayList<>();
            } else {
                comments = new ArrayList<>(comments);
            }

            node.setBlockComments(comments);
            comments.addAll(0, Collections.nCopies(count, BLANK_LINE));
        }

        private CommentLine commentLineFor(final String comment) {
            if (comment.length() == 1 && comment.charAt(0) == YamlConstructor.BLANK_LINE_COMMENT_INDICATOR) {
                return BLANK_LINE;
            }

            if (!YamlRepresenter.this.padComments || comment.isEmpty() || comment.charAt(0) == '#') {
                return new CommentLine(null, null, comment, CommentType.BLOCK);
            }

            // prepend a space before the comment:
            // before: #hello
            // after:  # hello
            return new CommentLine(null, null, " " + comment, CommentType.BLOCK);
        }
    }

    private static final class EmptyNullRepresenter implements Represent {
        @Override
        public Node representData(final Object data) {
            return new ScalarNode(Tag.NULL, "", null, null, ScalarStyle.PLAIN);
        }
    }

}
