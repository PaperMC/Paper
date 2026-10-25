/*
 * Copied from GeyserMC's Configurate fork:
 * https://github.com/GeyserMC/Configurate/blob/f6aa72e6099f956e78b638a2a817429007c6898a/format/yaml/src/main/java/org/spongepowered/configurate/yaml/BlankLineStyle.java
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

/**
 * Representation of blank lines in a YAML document.
 *
 * @since 4.3.0
 */
public enum BlankLineStyle {

    /**
     * Don't do any blank line styling.
     * This includes seeing blank lines during read as formatting, which will be stripped.
     * To keep those blank lines, use the {@link #KEEP} style.<br>
     * <br>
     * This is the default.<br>
     * <br>
     * Example: <pre>
     * {@code
     *  a: 1
     *  b:
     *    c: 1
     *  e:
     *    - hello
     *    - world
     *  f:
     *    - name: hello
     *    - name: world
     *  g:
     *    h:
     *      - name: hello
     *      - name: world
     *    i: 1
     *  j: 1
     * }</pre>
     *
     * @since 4.3.0
     */
    NONE,
    /**
     * Keep the blank lines just like they were read.<br>
     * <br>
     * Note that this option, unlike the others,
     * will also keep read blank lines (and store it in the comment.)
     * These blank lines are stored using a special character,
     * so if you write to a different format you may have to remove them manually.
     *
     * @since 4.3.0
     */
    KEEP,
    /**
     * Add a blank line after a nested node.<br>
     * <br>
     * Example: <pre>
     * {@code
     *  a: 1
     *  b:
     *    c: 1
     *
     *  e:
     *    - hello
     *    - world
     *
     *  f:
     *    - name: hello
     *    - name: world
     *
     *  g:
     *    h:
     *      - name: hello
     *      - name: world
     *
     *    i: 1
     *
     *  j: 1
     * }</pre>
     *
     * @since 4.3.0
     */
    AFTER_NESTED,
    /**
     * Add a blank after every root child, except the last.<br>
     * <br>
     * Example: <pre>
     * {@code
     *  a: 1
     *
     *  b:
     *    c: 1
     *
     *  e:
     *    - hello
     *    - world
     *
     *  f:
     *    - name: hello
     *    - name: world
     *
     *  g:
     *    h:
     *      - name: hello
     *      - name: world
     *    i: 1
     *
     *  j: 1
     * }</pre>
     *
     * @since 4.3.0
     */
    ROOT_CHILDREN,
    /**
     * Prepend a blank line before every node with a comment, except from the first child.<br>
     * <br>
     * Example: <pre>
     * {@code
     *  # Hello
     *  a: 1
     *
     *  # World
     *  b:
     *    # John
     *    c: 1
     *
     *  # Doe
     *  e:
     *    - hello
     *    - world
     *
     *  # Jane
     *  f:
     *    - name: hello
     *    - name: world
     *
     *  # Doe
     *  g:
     *    # Lorum
     *    h:
     *      - name: hello
     *      - name: world
     *
     *    # Ipsum
     *    i: 1
     *  j: 1
     * }</pre>
     *
     * @since 4.3.0
     */
    BEFORE_COMMENT

}
