/*
 * Copied from GeyserMC's Configurate fork:
 * https://github.com/GeyserMC/Configurate/blob/f6aa72e6099f956e78b638a2a817429007c6898a/format/yaml/src/main/java/org/spongepowered/configurate/yaml/ScalarStyle.java
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

import java.util.EnumMap;
import java.util.Map;

/**
 * Style that can be used to represent a scalar.
 *
 * @since 4.3.0
 */
public enum ScalarStyle {

    /**
     * A double-quoted string.
     *
     * <pre>"hello world"</pre>
     *
     * @since 4.3.0
     */
    DOUBLE_QUOTED(DumperOptions.ScalarStyle.DOUBLE_QUOTED),

    /**
     * A single-quoted string.
     *
     * <pre>'hello world'</pre>
     *
     * @since 4.3.0
     */
    SINGLE_QUOTED(DumperOptions.ScalarStyle.SINGLE_QUOTED),

    /**
     * String without any quotation.
     *
     * <p>This may be ambiguous with non-string types.</p>
     *
     * @since 4.3.0
     */
    UNQUOTED(DumperOptions.ScalarStyle.PLAIN),

    /**
     * Folded scalar.
     *
     * <pre>{@code
     * key: >
     *   folded scalar
     *   line breaks collapsed
     * }</pre>
     *
     * @since 4.3.0
     */
    FOLDED(DumperOptions.ScalarStyle.FOLDED),

    /**
     * Literal scalar.
     *
     * <pre>{@code
     * key: |
     *   literal scalar
     *   line breaks preserved
     * }</pre>
     *
     * @since 4.3.0
     */
    LITERAL(DumperOptions.ScalarStyle.LITERAL)
    ;

    private static final Map<DumperOptions.ScalarStyle, ScalarStyle> BY_SNAKE = new EnumMap<>(DumperOptions.ScalarStyle.class);
    private final DumperOptions.ScalarStyle snake;

    ScalarStyle(final DumperOptions.ScalarStyle snake) {
        this.snake = snake;
    }

    static DumperOptions.ScalarStyle asSnakeYaml(
        final @Nullable ScalarStyle style,
        final DumperOptions.@Nullable ScalarStyle fallback
    ) {
        if (style != null) {
            return style.snake;
        } else if (fallback != null) {
            return fallback;
        } else {
            return DumperOptions.ScalarStyle.PLAIN;
        }
    }

    static ScalarStyle fromSnakeYaml(final DumperOptions.ScalarStyle style) {
        return BY_SNAKE.getOrDefault(style, UNQUOTED);
    }

    static {
        for (final ScalarStyle style : values()) {
            BY_SNAKE.put(style.snake, style);
        }
    }

}
