/*
 * Copied from GeyserMC's Configurate fork:
 * https://github.com/GeyserMC/Configurate/blob/f6aa72e6099f956e78b638a2a817429007c6898a/format/yaml/src/main/java/org/spongepowered/configurate/yaml/NodeStyle.java
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

import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.DumperOptions;

/**
 * Representation of collections and mappings in a YAML document.
 *
 * @since 4.0.0
 */
public enum NodeStyle {

    /**
     * Always use <a href="https://yaml.org/spec/1.1/#id903421">block style</a>.
     */
    BLOCK(DumperOptions.FlowStyle.BLOCK),

    /**
     * Always use <a href="https://yaml.org/spec/1.1/#id902924">flow style</a>.
     */
    FLOW(DumperOptions.FlowStyle.FLOW)
    ;

    private final DumperOptions.FlowStyle snake;

    NodeStyle(final DumperOptions.FlowStyle snake) {
        this.snake = snake;
    }

    static DumperOptions.FlowStyle asSnakeYaml(final @Nullable NodeStyle style) {
        return style == null ? DumperOptions.FlowStyle.AUTO : style.snake;
    }

    static @Nullable NodeStyle fromSnakeYaml(final DumperOptions.FlowStyle style) {
        switch (style) {
            case AUTO:
                return null;
            case BLOCK:
                return BLOCK;
            case FLOW:
                return FLOW;
            default:
                throw new IllegalArgumentException("Unknown style " + style);
        }
    }

}
